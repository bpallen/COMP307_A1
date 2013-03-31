package nz.ac.vuw.comp307.a1.part2.allenbenj;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws Exception {

		Scanner scan_training = new Scanner(new File(args[0]));
		Scanner scan_test = new Scanner(new File(args[1]));

		// class ids
		final int NOTSPAM = 0, SPAM = 1;

		// init all counts to 1 because magic

		// class counts
		int[] count = new int[2];
		Arrays.fill(count, 1);

		// present / absent feature counts
		int[][] count_fpos = new int[2][12];
		Arrays.fill(count_fpos[SPAM], 1);
		Arrays.fill(count_fpos[NOTSPAM], 1);
		int[][] count_fneg = new int[2][12];
		Arrays.fill(count_fneg[SPAM], 1);
		Arrays.fill(count_fneg[NOTSPAM], 1);

		// overall feature counts
		int[] tcount_fpos = new int[12];
		int[] tcount_fneg = new int[12];

		// parse training data
		while (scan_training.hasNext()) {
			// features
			boolean[] f = parseFeatures(scan_training);
			// class
			int c = scan_training.nextInt();
			// inc appropriate counts
			count[c]++;
			for (int i = 0; i < 12; i++) {
				if (f[i]) {
					count_fpos[c][i]++;
					tcount_fpos[i]++;
				} else {
					count_fneg[c][i]++;
					tcount_fneg[i]++;
				}
			}
		}

		// compute so-called probabilities

		// class probabilites
		double[] prob = new double[2];
		prob[SPAM] = count[SPAM] / (double) (count[SPAM] + count[NOTSPAM]);
		prob[NOTSPAM] = count[NOTSPAM] / (double) (count[SPAM] + count[NOTSPAM]);

		// feature probabilites
		double[][] prob_fpos = new double[2][12];
		double[][] prob_fneg = new double[2][12];

		// overall feature probabilites
		double[] tprob_fpos = new double[12];
		double[] tprob_fneg = new double[12];

		for (int i = 0; i < 12; i++) {
			prob_fpos[SPAM][i] = count_fpos[SPAM][i] / (double) (count_fpos[SPAM][i] + count_fneg[SPAM][i]);
			prob_fneg[SPAM][i] = count_fneg[SPAM][i] / (double) (count_fpos[SPAM][i] + count_fneg[SPAM][i]);
		}

		for (int i = 0; i < 12; i++) {
			prob_fpos[NOTSPAM][i] = count_fpos[NOTSPAM][i] / (double) (count_fpos[NOTSPAM][i] + count_fneg[NOTSPAM][i]);
			prob_fneg[NOTSPAM][i] = count_fneg[NOTSPAM][i] / (double) (count_fpos[NOTSPAM][i] + count_fneg[NOTSPAM][i]);
		}

		for (int i = 0; i < 12; i++) {
			tprob_fpos[i] = tcount_fpos[i] / (double) (tcount_fpos[i] + tcount_fneg[i]);
			tprob_fneg[i] = tcount_fneg[i] / (double) (tcount_fpos[i] + tcount_fneg[i]);
		}

		// print out classifier probabilites
		System.out.println("P(SPAM)       = " + prob[SPAM]);
		System.out.println("P(NOTSPAM)    = " + prob[NOTSPAM]);
		System.out.println("P(F)          = " + toString(tprob_fpos));
		System.out.println("P(!F)         = " + toString(tprob_fneg));
		System.out.println("P(F|SPAM)     = " + toString(prob_fpos[SPAM]));
		System.out.println("P(!F|SPAM)    = " + toString(prob_fneg[SPAM]));
		System.out.println("P(F|NOTSPAM)  = " + toString(prob_fpos[NOTSPAM]));
		System.out.println("P(!F|NOTSPAM) = " + toString(prob_fneg[NOTSPAM]));
		
		// classify the test data
		while (scan_test.hasNext()) {
			boolean[] f = parseFeatures(scan_test);

			double score_spam = partialScore(f, prob_fpos[SPAM], prob_fneg[SPAM]) * prob[SPAM]
					/ partialScore(f, tprob_fpos, tprob_fneg);
			double score_notspam = partialScore(f, prob_fpos[NOTSPAM], prob_fneg[NOTSPAM]) * prob[NOTSPAM]
					/ partialScore(f, tprob_fpos, tprob_fneg);

			if (score_spam > score_notspam) {
				System.out.printf("%s spam = %.4f, notspam = %.4f : SPAM\n", toString(f), score_spam, score_notspam);
			} else {
				System.out.printf("%s spam = %.4f, notspam = %.4f : NOT SPAM\n", toString(f), score_spam, score_notspam);
			}
		}

	}

	private static String toString(boolean[] a) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (boolean b : a) {
			sb.append(b ? 1 : 0);
			sb.append(" ");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("]");
		return sb.toString();
	}
	
	private static String toString(double[] a) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (double d : a) {
			sb.append(String.format("%.4f ", d));
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("]");
		return sb.toString();
	}

	private static double partialScore(boolean[] f, double[] prob_fpos, double[] prob_fneg) {
		double s = 1;
		for (int i = 0; i < 12; i++) {
			if (f[i]) {
				s *= prob_fpos[i];
			} else {
				s *= prob_fneg[i];
			}
		}
		return s;
	}

	private static boolean[] parseFeatures(Scanner scan) {
		boolean[] f = new boolean[12];
		for (int i = 0; i < 12; i++) {
			f[i] = scan.nextInt() != 0;
		}
		return f;
	}

}
