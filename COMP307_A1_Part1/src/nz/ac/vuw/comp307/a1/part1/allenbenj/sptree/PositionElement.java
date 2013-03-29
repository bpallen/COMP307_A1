package nz.ac.vuw.comp307.a1.part1.allenbenj.sptree;

public class PositionElement implements SPTree.Element {

	private final Vector v;

	public PositionElement(Vector v_) {
		v = v_;
	}

	public Vector get() {
		return v;
	}

	@Override
	public AABB getAABB() {
		return v;
	}

	@Override
	public String toString() {
		return v.toString();
	}

}