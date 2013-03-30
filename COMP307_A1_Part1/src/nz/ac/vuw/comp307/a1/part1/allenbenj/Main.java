package nz.ac.vuw.comp307.a1.part1.allenbenj;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import nz.ac.vuw.comp307.a1.part1.allenbenj.sptree.*;

public class Main {

	public static void main(String[] args) throws Exception {

		Scanner scan_training = new Scanner(new FileInputStream(args[0]));
		Scanner scan_test = new Scanner(new FileInputStream(args[1]));

		final int k = args.length < 3 ? 1 : Integer.parseInt(args[2]);
		System.out.println("Using k: " + k);

		Vector range_max = Vector.zero(4);
		Vector range_min = Vector.zero(4);
		Vector range;

		List<Iris> iris_training = new ArrayList<Iris>();
		List<Iris> iris_test = new ArrayList<Iris>();

		// read in training data
		while (scan_training.hasNext()) {
			Iris iris = new Iris(scan_training.nextDouble(), scan_training.nextDouble(), scan_training.nextDouble(),
					scan_training.nextDouble(), scan_training.next().toLowerCase());
			iris_training.add(iris);
			range_max = Vector.positiveExtremes(range_max, iris.getFeatures());
			range_min = Vector.negativeExtremes(range_min, iris.getFeatures());
		}
		range = range_max.sub(range_min);

		// scale training data by 1 / range
		for (Iris iris : iris_training) {
			iris.setRange(range);
		}

		// read in test data and scale
		while (scan_test.hasNext()) {
			Iris iris = new Iris(scan_test.nextDouble(), scan_test.nextDouble(), scan_test.nextDouble(),
					scan_test.nextDouble(), scan_test.next().toLowerCase());
			iris_test.add(iris);
			iris.setRange(range);
		}

		// throw training data into a 4-dimensional SPACE PARTITION TREE
		SPTree<Iris> iris_spt = new SPTree<Iris>(4);
		for (Iris iris : iris_training) {
			iris_spt.add(iris);
		}

		// determine initial search radius from tree height
		final double searchrad0 = Math.pow(k, 1 / 4d) / Math.pow(2, (iris_spt.height() - 0.5));
		System.out.println("Searching in range-scaled data with initial radius: " + searchrad0);

		if (k > iris_spt.count()) throw new AssertionError("k > number of training data points.");

		// BEGIN CLASSIFICATION
		Map<Iris, String> fail_cnames = new HashMap<Iris, String>();
		for (final Iris iris0 : iris_test) {
			// search over an increasing radius until >= k irises have been found, then sort
			// take nearest k, take majority class
			double r = searchrad0;
			for (int i = 1;; i++) {

				AABB aabb = AABB.fromInnerSphere(iris0.getScaledFeatures(), r);
				List<Iris> iris_search0 = iris_spt.find(aabb);
				List<Iris> iris_search = new ArrayList<Iris>();

				// because we searched a box, only keep the ones that are actually within the current radius
				for (Iris iris : iris_search0) {
					if (iris.getScaledFeatures().dist(iris0.getScaledFeatures()) <= r) {
						iris_search.add(iris);
					}
				}

				if (iris_search.size() >= k) {
					// have enough points to classify test iris
					if (iris_search.size() > k) {
						// sort so only nearest k are considered
						Collections.sort(iris_search, new Comparator<Iris>() {

							@Override
							public int compare(Iris o1, Iris o2) {
								return Double.compare(o1.getScaledFeatures().dist(iris0.getScaledFeatures()), o2
										.getScaledFeatures().dist(iris0.getScaledFeatures()));
							}

						});
					}
					// count instances of each class
					Map<String, Integer> ccounts = new HashMap<String, Integer>();
					for (int j = 0; j < k; j++) {
						Iris iris = iris_search.get(j);
						String cname = iris.getName();
						if (ccounts.get(cname) == null) {
							ccounts.put(cname, 1);
						} else {
							ccounts.put(cname, ccounts.get(cname) + 1);
						}
					}
					// find majority class
					int cmax = 0;
					String c = null;
					for (Map.Entry<String, Integer> me : ccounts.entrySet()) {
						if (me.getValue() > cmax) {
							cmax = me.getValue();
							c = me.getKey();
						}
					}
					if (c.equals(iris0.getName())) {
						// correct!
						System.out.println(iris0 + " correctly classified.");
					} else {
						// not correct
						System.out.println(iris0 + " mis-classifed as " + c + ".");
						fail_cnames.put(iris0, c);
					}
					System.out.println("    " + iris_search.size() + " elements in sphere of radius " + r + ", "
							+ iris_search0.size() + " in AABB, " + i + " iterations.");
					break;
				}

				// have to increase radius, go round again
				r *= Math.pow(2, 1 / 4d);
			}

		}

		System.out.println();
		System.out.println(fail_cnames.size() + " irises misclassified.");
		for (Map.Entry<Iris, String> me : fail_cnames.entrySet()) {
			System.out.println(me.getKey() + " mis-classified as " + me.getValue() + ".");
		}

	}
}
