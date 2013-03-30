package nz.ac.vuw.comp307.a1.part1.allenbenj.sptree;

/**
 * A n-dimensional vector.
 * 
 * @author Ben Allen
 * 
 */
public class Vector extends AABB {

	public static Vector zero(int n) {
		return new Vector(n);
	}

	public static Vector one(int n) {
		Vector v = new Vector(n);
		for (int i = 0; i < n; i++) {
			v.set(i, 1);
		}
		return v;
	}

	public static Vector fill(int n, double val) {
		Vector v = new Vector(n);
		for (int i = 0; i < n; i++) {
			v.set(i, val);
		}
		return v;
	}

	public static Vector basis(int n, int i) {
		Vector v = new Vector(n);
		v.set(i, 1);
		return v;
	}

	public static Vector positiveExtremes(Vector a, Vector b) {
		Vector v = new Vector(a.size());
		for (int i = 0; i < a.size(); i++) {
			v.set(i, Math.max(a.get(i), b.get(i)));
		}
		return v;
	}

	public static Vector negativeExtremes(Vector a, Vector b) {
		Vector v = new Vector(a.size());
		for (int i = 0; i < a.size(); i++) {
			v.set(i, Math.min(a.get(i), b.get(i)));
		}
		return v;
	}

	private double[] data;
	public boolean is_const = false;

	public Vector(int n) {
		data = new double[n];
	}

	public Vector(double... d) {
		data = d.clone();
	}

	/**
	 * @return A non-const copy of this Vector.
	 */
	public Vector copy() {
		// this relies on the constructor copying the array!
		return new Vector(data);
	}

	public void constify() {
		is_const = true;
	}

	public double get(int i) {
		return data[i];
	}

	public void set(int i, double d) {
		if (is_const) throw new IllegalStateException("Vector has been made const.");
		data[i] = d;
	}

	public void setNeg(int i) {
		if (is_const) throw new IllegalStateException("Vector has been made const.");
		data[i] = -data[i];
	}

	public int size() {
		return data.length;
	}

	public Vector add(Vector rhs) {
		Vector v = new Vector(this.size());
		for (int i = 0; i < data.length; i++) {
			v.set(i, this.get(i) + rhs.get(i));
		}
		return v;
	}

	public Vector sub(Vector rhs) {
		Vector v = new Vector(this.size());
		for (int i = 0; i < data.length; i++) {
			v.set(i, this.get(i) - rhs.get(i));
		}
		return v;
	}

	public Vector negate() {
		Vector v = new Vector(this.size());
		for (int i = 0; i < data.length; i++) {
			v.set(i, -this.get(i));
		}
		return v;
	}

	public Vector average(Vector other) {
		Vector v = new Vector(this.size());
		for (int i = 0; i < data.length; i++) {
			v.set(i, 0.5 * (this.get(i) + other.get(i)));
		}
		return v;
	}

	public Vector scale(double f) {
		Vector v = new Vector(this.size());
		for (int i = 0; i < data.length; i++) {
			v.set(i, this.get(i) * f);
		}
		return v;
	}

	public Vector mulEl(Vector rhs) {
		Vector v = new Vector(this.size());
		for (int i = 0; i < data.length; i++) {
			v.set(i, this.get(i) * rhs.get(i));
		}
		return v;
	}

	public Vector divEl(Vector rhs) {
		Vector v = new Vector(this.size());
		for (int i = 0; i < data.length; i++) {
			v.set(i, this.get(i) / rhs.get(i));
		}
		return v;
	}

	public double mag() {
		double t0 = 0;
		for (int i = 0; i < data.length; i++) {
			double t1 = this.get(i);
			t0 += t1 * t1;
		}
		return Math.sqrt(t0);
	}

	public double dot(Vector rhs) {
		double d = 0;
		for (int i = 0; i < data.length; i++) {
			d += this.get(i) * rhs.get(i);
		}
		return d;
	}

	public double dist(Vector other) {
		double t0 = 0;
		for (int i = 0; i < data.length; i++) {
			double t1 = this.get(i) - other.get(i);
			t0 += t1 * t1;
		}
		return Math.sqrt(t0);
	}

	@Override
	public Vector min() {
		return this;
	}

	@Override
	public Vector max() {
		return this;
	}

	@Override
	public Vector centre() {
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("(");
		for (int i = 0; i < data.length; i++) {
			sb.append(data[i]);
			sb.append(", ");
		}
		sb.delete(sb.length() - 2, sb.length());
		sb.append(")");
		return sb.toString();
	}

}
