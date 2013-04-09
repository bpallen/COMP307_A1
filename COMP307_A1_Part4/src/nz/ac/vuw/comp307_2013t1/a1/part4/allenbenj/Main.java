package nz.ac.vuw.comp307_2013t1.a1.part4.allenbenj;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) throws Exception {

		List<Image> images_raw = new ArrayList<Image>();

		// interpret all args as image files to load
		for (String s : args) {
			BufferedReader br = new BufferedReader(new FileReader(s));
			while (br.ready()) {
				images_raw.add(new Image(br));
			}
		}

		System.out.println("Read " + images_raw.size() + " images.");

		List<Feature> features = new ArrayList<Feature>();
		for (int i = 0; i < 50; i++) {
			features.add(new Feature());
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

	}

	private static double evalPerceptron(double[] weights, double[] features) {
		// TODO

		return 0;
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
