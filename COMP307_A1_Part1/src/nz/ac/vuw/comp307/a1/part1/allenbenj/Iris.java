package nz.ac.vuw.comp307.a1.part1.allenbenj;

import nz.ac.vuw.comp307.a1.part1.allenbenj.sptree.*;

public class Iris implements SPTree.Element {

	private final String name;
	private final Vector f;

	private Vector f_scaled = null;

	public Iris(double f0, double f1, double f2, double f3, String name_) {
		name = name_;
		f = new Vector(f0, f1, f2, f3);
		f.constify();
	}

	public String getName() {
		return name;
	}

	public Vector getFeatures() {
		return f;
	}

	public void setRange(Vector r) {
		f_scaled = f.divEl(r);
		f_scaled.constify();
	}

	public Vector getScaledFeatures() {
		return f_scaled;
	}

	@Override
	public AABB getAABB() {
		return f_scaled;
	}

	@Override
	public String toString() {
		return name + "[Features:" + f + "]";
	}
}
