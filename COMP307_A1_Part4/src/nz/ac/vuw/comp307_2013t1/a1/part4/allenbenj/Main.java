package nz.ac.vuw.comp307_2013t1.a1.part4.allenbenj;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) throws Exception {

		List<Image> images_raw = new ArrayList<Image>();

		// 1st arg - image file to load
		BufferedReader br = new BufferedReader(new FileReader(args[0]));
		while (br.ready()) {
			images_raw.add(new Image(br));
		}

		// 2nd arg (if present) - features count
		final int FEATURE_COUNT = args.length > 1 ? Integer.parseInt(args[1]) : 50;

		// 3rd arg (if present) - max epochs
		final int MAX_EPOCHS = args.length > 2 ? Integer.parseInt(args[2]) : 1000;

		System.out.println("Using FEATURE_COUNT : " + FEATURE_COUNT);
		System.out.println("Using MAX_EPOCHS    : " + MAX_EPOCHS);

		System.out.println("Read " + images_raw.size() + " images.");

		List<Feature> features = new ArrayList<Feature>();
		for (int i = 0; i < FEATURE_COUNT; i++) {
			features.add(new Feature());
		}
		
		System.out.println(" -- GENERATED FEATURES --");
		for (Feature f : features) {
			System.out.println(f);
		}

		// calculate feature values on each image
		List<double[]> images_features = new ArrayList<double[]>();
		for (Image img : images_raw) {
			images_features.add(evalFeatures(img, features));
		}

		// init weights to small random values
		double[] weights = new double[features.size() + 1];
		for (int i = 0; i < weights.length; i++) {
			// TODO good enough?
			weights[i] = Math.random() - 0.5;
		}

		// run the algorithm!
		System.out.println(" -- BEGIN TRAINING --");
		int count_correct = 0;
		int epoch = 0;
		for (; epoch < MAX_EPOCHS && count_correct < images_raw.size(); epoch++) {
			// print out some progress
			if (epoch % 10 == 0) {
				System.out.printf("Correct after %4d epochs : %d / %d (%.1f%%)\n", epoch, count_correct,
						images_raw.size(), 100 * count_correct / (double) images_raw.size());
			}
			// iterate over the images and change the weights
			for (int i = 0; i < images_raw.size(); i++) {
				double[] f = images_features.get(i);
				double p = evalPerceptron(weights, f);
				boolean positive = images_raw.get(i).getCategory().equals("yes");
				if ((p > 0 && positive) || (p <= 0 && !positive)) {
					// correct
					// cant inc count_correct here because we need to measure it
					// with 1 weights vector
				} else if (positive) {
					// wrong, positive example
					// add features to weights
					for (int j = 0; j < weights.length; j++) {
						weights[j] += f[j];
					}
				} else {
					// wrong, negative example
					// subtract features from weights
					for (int j = 0; j < weights.length; j++) {
						weights[j] -= f[j];
					}
				}
			}
			// test correctness
			count_correct = 0;
			for (int i = 0; i < images_raw.size(); i++) {
				double[] f = images_features.get(i);
				double p = evalPerceptron(weights, f);
				boolean positive = images_raw.get(i).getCategory().equals("yes");
				if ((p > 0 && positive) || (p <= 0 && !positive)) {
					// correct
					count_correct++;
				}
			}
		}
		
		System.out.println(" -- FINAL PERCEPTRON WEIGHTS --");
		for (double d : weights) {
			System.out.println(d);
		}

		// classify examples
		System.out.println(" -- BEGIN CLASSIFICATION --");
		count_correct = 0;
		for (int i = 0; i < images_raw.size(); i++) {
			double[] f = images_features.get(i);
			double p = evalPerceptron(weights, f);
			boolean positive = images_raw.get(i).getCategory().equals("yes");
			if ((p > 0 && positive) || (p <= 0 && !positive)) {
				// correct
				count_correct++;
			}
			System.out.println("Image " + i + " <" + images_raw.get(i).getCategory() + "> : "
					+ (p > 0 ? "yes" : "other"));
		}
		
		System.out.println(" -- CLASSIFICATION SUMMARY --");
		System.out.println("Features : " + features.size());
		System.out.println("Epochs   : " + epoch);
		System.out.printf("Correct  : %d / %d (%.1f%%)\n", count_correct, images_raw.size(), 100 * count_correct
				/ (double) images_raw.size());
		
	}

	private static double evalPerceptron(double[] weights, double[] f) {
		double p = 0;
		for (int i = 0; i < weights.length; i++) {
			p += weights[i] * f[i];
		}
		return p;
	}

	private static double[] evalFeatures(Image img, List<Feature> features) {
		double[] fv = new double[features.size() + 1];
		// dummy feature
		fv[0] = 1;
		int i = 1;
		for (Feature f : features) {
			fv[i++] = f.value(img);
		}
		return fv;
	}

}
