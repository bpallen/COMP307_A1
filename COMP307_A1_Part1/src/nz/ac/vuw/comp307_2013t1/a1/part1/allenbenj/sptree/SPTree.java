package nz.ac.vuw.comp307_2013t1.a1.part1.allenbenj.sptree;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * N-dimensional space partition tree. Generalisation of BSP Tree, Quadtree, Octree, etc.
 *
 * @author Ben Allen
 *
 */
public class SPTree<T extends SPTree.Element> implements Iterable<T> {

	// TODO maybe have SPTree implement set

	public static final int MAX_LEAF_ELEMENTS = 8;
	public static final String INDENT_STRING = "    ";

	public static interface Element {

		public AABB getAABB();

	}

	protected static class OutOfBoundsException extends Exception {

		private static final long serialVersionUID = 1L;

		public OutOfBoundsException() {
			super();
		}

		public OutOfBoundsException(String arg0, Throwable arg1) {
			super(arg0, arg1);
		}

		public OutOfBoundsException(String arg0) {
			super(arg0);
		}

		public OutOfBoundsException(Throwable arg0) {
			super(arg0);
		}

	}

	protected static class Node<U extends SPTree.Element> implements Iterable<U> {

		private Node<U> parent;
		private AABB aabb;

		private boolean isleaf = true;
		private Set<U> elements = new HashSet<U>();

		private int count = 0;

		// up to pow(2, n) children
		private Map<BigInteger, Node<U>> children = new HashMap<BigInteger, Node<U>>();

		public Node(Node<U> parent_, AABB aabb_) {
			parent = parent_;
			aabb = aabb_;
		}

		public Node<U> getParent() {
			return parent;
		}

		public void setParent(Node<U> nu) {
			parent = nu;
		}

		public AABB getAABB() {
			return aabb;
		}

		public int count() {
			return count;
		}

		public int countRecursively() {
			int c = elements.size();
			for (Node<U> nu : children.values()) {
				c += nu.count();
			}
			return c;
		}

		private BigInteger childID(Vector v) {
			Vector centre = aabb.centre();
			BigInteger cid = BigInteger.ZERO;
			for (int i = aabb.size(); i-- > 0;) {
				if (v.get(i) >= centre.get(i)) cid = cid.setBit(i);
			}
			return cid;
		}

		private void unleafify() {
			if (isleaf) {
				// unleafify
				isleaf = false;
				Object[] temp = new Object[elements.size()];
				elements.toArray(temp);
				elements.clear();
				count -= temp.length;
				for (Object o : temp) {
					@SuppressWarnings("unchecked")
					U u2 = (U) o;
					try {
						add(u2);
					} catch (OutOfBoundsException e) {
						// this should NOT happen
						throw new AssertionError(e);
					}
				}
			}
		}

		private void leafify() {
			if (!isleaf) {
				// leafify
				isleaf = true;
				Set<U> elements2 = new HashSet<U>();
				for (U u : this) {
					elements2.add(u);
				}
				children.clear();
				elements = elements2;
			}
		}

		public boolean add(U u) throws OutOfBoundsException {
			AABB a = u.getAABB();
			if (!aabb.contains(a)) throw new OutOfBoundsException();
			if (isleaf && elements.size() < MAX_LEAF_ELEMENTS) {
				if (!elements.add(u)) return false;
			} else {
				// not a leaf or should not be. add to appropriate child node, or this if none.
				unleafify();
				final BigInteger cid_min = childID(a.min());
				final BigInteger cid_max = childID(a.max());
				if (!cid_min.equals(cid_max)) {
					// element spans multiple child nodes. add to this.
					if (!elements.add(u)) return false;
				} else {
					// element is contained in one child node. add to it, creating if necessary.
					Node<U> child = children.get(cid_min);
					if (child == null) {
						Vector centre = aabb.centre();
						// this always points to a corner of the current node's aabb
						Vector vr = aabb.max().sub(centre);
						for (int i = 0; i < aabb.size(); i++) {
							if (!cid_min.testBit(i)) vr.setNeg(i);
						}
						child = new Node<U>(this, AABB.fromExtremes(centre, centre.add(vr)));
						children.put(cid_min, child);
					}
					try {
						if (!child.add(u)) return false;
					} catch (OutOfBoundsException e) {
						// child doesnt want to accept it (fp inaccuracies maybe?)
						if (!elements.add(u)) return false;
					}
				}
			}
			count++;
			return true;
		}

		public void putChild(Node<U> child) {
			if (child == null) throw new NullPointerException();
			BigInteger cid = childID(child.getAABB().centre());
			children.put(cid, child);
			count += child.count();
			unleafify();
		}

		public boolean remove(U u) {
			// TODO SPTree.Node remove
			return false;
		}

		public boolean contains(U u) {
			AABB a = u.getAABB();
			if (!aabb.contains(a)) return false;
			if (elements.contains(u)) return true;
			if (isleaf) return false;
			final BigInteger cid_min = childID(a.min());
			final BigInteger cid_max = childID(a.max());
			// if split across children, should be in elements
			if (!cid_min.equals(cid_max)) return false;
			Node<U> child = children.get(cid_min);
			// if the child it would be in doesnt exist
			if (child == null) return false;
			return child.contains(u);
		}

		public void find(List<? super U> lu, AABB a) {
			if (aabb.intersects(a)) {
				for (U u : elements) {
					if (u.getAABB().intersects(a)) lu.add(u);
				}
				if (isleaf) return;
				final BigInteger cid_min = childID(a.min());
				final BigInteger cid_max = childID(a.max());
				if (cid_min.equals(cid_max)) {
					// a is contained in one child
					Node<U> child = children.get(cid_min);
					if (child != null) child.find(lu, a);
				} else {
					for (Node<U> nu : children.values()) {
						nu.find(lu, a);
					}
				}
			}
		}

		public void print(String indent, String indent_delta) {
			System.out.println(indent + aabb);
			for (U u : elements) {
				System.out.println(indent + indent_delta + u);
			}
			for (Node<U> nu : children.values()) {
				nu.print(indent, indent_delta);
			}
		}

		public int height() {
			int h = 0;
			for (Node<U> nu : children.values()) {
				int hn = nu.height();
				h = Math.max(h, hn);
			}
			return h + 1;
		}

		public double heightAvg() {
			double h = 0;
			int i = 0;
			for (Node<U> nu : children.values()) {
				h += nu.heightAvg();
				i++;
			}
			return 1 + (i > 0 ? h / i : 0);
		}

		@Override
		public Iterator<U> iterator() {
			return new NodeIterator();
		}

		private class NodeIterator implements Iterator<U> {

			private Iterator<U> it_el = elements.iterator();
			private Iterator<Node<U>> it_ch = children.values().iterator();
			private U next_u = null;

			public NodeIterator() {
				next_u = findNext();
			}

			private U findNext() {
				// this is depth-first
				try {
					while (true) {
						if (it_el.hasNext()) return it_el.next();
						it_el = it_ch.next().iterator();
					}
				} catch (NoSuchElementException e) {
					return null;
				}
			}

			@Override
			public boolean hasNext() {
				return next_u != null;
			}

			@Override
			public U next() {
				try {
					return next_u;
				} finally {
					next_u = findNext();
				}
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

		}

	}

	private final int n;
	private Node<T> root;

	public SPTree(int n_) {
		if (n_ < 1) throw new IllegalArgumentException("Don't do that.");
		n = n_;
		root = new Node<T>(null, AABB.fromExtremes(Vector.zero(n), Vector.one(n)));
	}
	
	public SPTree(Vector root_p0, Vector root_p1) {
		// TODO what happens if you have a non-regular root node?
		if (root_p0 == null || root_p1 == null) throw new NullPointerException("Cut it out!");
		if (root_p0.size() != root_p1.size()) throw new IllegalArgumentException("Vectors aren't the same size.");
		if (root_p0.size() < 1) throw new IllegalArgumentException("Can't have less than 1 dimension.");
		n = root_p0.size();
		root = new Node<T>(null, AABB.fromExtremes(root_p0, root_p1));
	}

	public boolean add(T t) {
		if (t == null) throw new NullPointerException("Boo!");
		try {
			return root.add(t);
		} catch (OutOfBoundsException e) {
			// make new root
			Node<T> oldroot = root;
			AABB a = root.getAABB();
			// vector from centre to max of new root
			Vector vr = a.max().sub(a.min());
			// vector from centre of current root to centre of new element
			Vector vct = t.getAABB().centre().sub(a.centre());
			// vector from centre of current root to corner nearest centre of new element
			Vector corner = vr.scale(0.5);
			for (int i = 0; i < corner.size(); i++) {
				if (vct.get(i) < 0) corner.setNeg(i);
			}
			// centre of new root
			Vector newcentre = a.centre().add(corner);
			root = new Node<T>(null, AABB.fromExtremes(newcentre.sub(vr), newcentre.add(vr)));
			if (oldroot.count() > 0) {
				// only preserve the old root if it had elements
				oldroot.setParent(root);
				root.putChild(oldroot);
			}
			return add(t);
		}
	}

	public boolean remove(T t) {

		return false;
	}

	public boolean contains(T t) {
		return root.contains(t);
	}

	public List<T> find(AABB a) {
		if (a == null) throw new NullPointerException("Game over.");
		List<T> lt = new ArrayList<T>();
		root.find(lt, a);
		return lt;
	}

	public void print() {
		root.print("", INDENT_STRING);
	}

	public int height() {
		return root.height();
	}

	public double heightAvg() {
		return root.heightAvg();
	}

	public int count() {
		return root.count();
	}

	public int countRecursively() {
		return root.countRecursively();
	}

	@Override
	public Iterator<T> iterator() {
		return root.iterator();
	}
}
