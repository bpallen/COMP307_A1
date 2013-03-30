package nz.ac.vuw.comp307.a1.part1.allenbenj.sptree;

import java.util.List;
import java.util.Random;

public class Test {

	public static void main(String[] args) {

		final int DIM = 3;

		SPTree<PointElement> spt = new SPTree<PointElement>(Vector.fill(DIM, -10), Vector.fill(DIM, 10));

		Random rand = new Random();

		for (int i = 0; i < 1000000; i++) {

			Vector v = new Vector(DIM);
			for (int j = 0; j < DIM; j++) {
				v.set(j, rand.nextDouble() * 20 - 10);
			}
			v.constify();

			// int c0 = spt.count();
			spt.add(new PointElement(v));

			// if (c0 == spt.count()) {
			// System.out.println("Failed to add: " + v);
			// }
		}

		System.out.println("Count: " + spt.count());
		// spt.print();

		AABB a = AABB.fromExtremes(Vector.fill(DIM, -5), Vector.fill(DIM, -4.5));
		List<PointElement> l = spt.find(a);

		System.out.println("Searching " + a);
		for (PointElement pe : l) {
			System.out.println(pe);
		}

		System.out.print("Counting with iterator: ");
		int i = 0;
		for (PointElement pe : spt) {
			i++;
		}
		System.out.println(i);

		System.out.println("Testing contains on all elements");
		long nanos_contains = 0;
		for (PointElement pe : spt) {
			long nanos_start = System.nanoTime();
			boolean c = spt.contains(pe);
			nanos_contains += System.nanoTime() - nanos_start;
			if (!c) {
				System.out.println(pe + " NOT FOUND");
			}
		}

		System.out.println("Nanoseconds per contains call: " + nanos_contains / (double) spt.count());

		System.out.println("Tree height avg: " + spt.heightAvg());
	}

}
