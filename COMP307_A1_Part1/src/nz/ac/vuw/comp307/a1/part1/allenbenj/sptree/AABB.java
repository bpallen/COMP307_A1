package nz.ac.vuw.comp307.a1.part1.allenbenj.sptree;

/**
 * Axis-Aligned Bounding Box
 * 
 * @author Ben Allen
 * 
 */
public abstract class AABB {

	public static AABB fromExtremes(Vector pos0, Vector pos1) {
		return new Impl(pos0, pos1);
	}

	public static AABB fromInnerSphere(Vector pos, double r) {
		Vector vr = Vector.fill(pos.size(), r);
		return new Impl(pos.sub(vr), pos.add(vr));
	}

	public static AABB fromOuterSphere(Vector pos, double r) {
		Vector vr = Vector.fill(pos.size(), r / Math.sqrt(pos.size()));
		return new Impl(pos.sub(vr), pos.add(vr));
	}

	public static AABB infinity(int n) {
		return new Impl(Vector.fill(n, Double.NEGATIVE_INFINITY), Vector.fill(n, Double.POSITIVE_INFINITY));
	}

	private static class Impl extends AABB {

		private Vector min, max;

		public Impl(Vector pos0, Vector pos1) {
			min = Vector.negativeExtremes(pos0, pos1);
			max = Vector.positiveExtremes(pos0, pos1);
		}

		@Override
		public Vector min() {
			return min;
		}

		@Override
		public Vector max() {
			return max;
		}
	}

	public abstract Vector min();

	public abstract Vector max();

	public Vector centre() {
		return min().average(max());
	}

	public int size() {
		return min().size();
	}

	public boolean contains(Vector v) {
		boolean inside = true;
		int n = size();
		for (int i = 0; i < n; i++) {
			inside = inside && (min().get(i) <= v.get(i) && v.get(i) <= max().get(i));
		}
		return inside;
	}

	public boolean contains(AABB a) {
		boolean inside = true;
		int n = size();
		for (int i = 0; i < n; i++) {
			inside = inside && (min().get(i) <= a.min().get(i) && a.max().get(i) <= max().get(i));
		}
		return inside;
	}

	public boolean intersects(AABB a) {
		boolean hit = true;
		int n = size();
		for (int i = 0; i < n; i++) {
			hit = hit && (min().get(i) <= a.max().get(i) && a.min().get(i) <= max().get(i));
		}
		return hit;
	}

	@Override
	public String toString() {
		return "AABB: " + min() + " <= x <= " + max();
	}
}
