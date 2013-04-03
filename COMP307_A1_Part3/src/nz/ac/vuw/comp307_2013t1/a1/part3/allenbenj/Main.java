package nz.ac.vuw.comp307_2013t1.a1.part3.allenbenj;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class Main {

	private static List<String> catNames;
	private static List<String> attNames;
	private static List<Instance> trainingInstances;

	public static void main(String[] args) throws Exception {

		readTrainingData(args[0]);
		
		DecisionTree dt = new DecisionTree(trainingInstances);
		
		System.out.println();
		System.out.println("Baseline: " + dt.getBaseline());
		System.out.println();
		
		List<Instance> testInstances = readTestData(args[1]);
		
		System.out.println();
		
		Map<String, Integer> count_correct = new HashMap<String, Integer>();
		Map<String, Integer> count_incorrect = new HashMap<String, Integer>();
		Map<String, Integer> count_total = new HashMap<String, Integer>();
		int tc_correct = 0;
		
		for (Instance inst : testInstances) {
			String dt_cat = dt.categorise(inst).getCategory();
			String cat = inst.getCategory();
			// total count for each category
			if (!count_total.containsKey(cat)) {
				count_total.put(cat, 1);
			} else {
				count_total.put(cat, count_total.get(cat) + 1);
			}
			if (cat.equals(dt_cat)) {
				// correct
				if (!count_correct.containsKey(cat)) {
					count_correct.put(cat, 1);
				} else {
					count_correct.put(cat, count_correct.get(cat) + 1);
				}
				tc_correct++;
			} else {
				// incorrect
				if (!count_incorrect.containsKey(cat)) {
					count_incorrect.put(cat, 1);
				} else {
					count_incorrect.put(cat, count_incorrect.get(cat) + 1);
				}
			}
		}
		
		System.out.println("Correct     : " + count_correct);
		System.out.println("Incorrect   : " + count_incorrect);
		System.out.println("Total       : " + count_total);
		System.out.println("DT Accuracy : " + tc_correct / (double) testInstances.size());
		System.out.println();
		
		dt.print();

	}

	private static void readTrainingData(String fname) throws IOException {
		// format of names file:
		// names of categories, separated by spaces
		// names of attributes category followed by true's and false's for each instance
		System.out.println("Reading training data from file " + fname);
		Scanner din = new Scanner(new File(fname));
		catNames = new ArrayList<String>();
		for (Scanner s = new Scanner(din.nextLine()); s.hasNext();) {
			catNames.add(s.next().toLowerCase());
		}
		System.out.println("categories (" + catNames.size() + ") : " + catNames);
		attNames = new ArrayList<String>();
		for (Scanner s = new Scanner(din.nextLine()); s.hasNext();) {
			attNames.add(s.next().toLowerCase());
		}
		System.out.println("attributes (" + attNames.size() + ") : " + attNames);
		// instance = classname and space separated attribute values
		Instance.useAttributeNames(attNames);
		trainingInstances = new ArrayList<Instance>();
		while (din.hasNext()) {
			trainingInstances.add(new Instance(new Scanner(din.nextLine())));
		}
		System.out.println("Read " + trainingInstances.size() + " instances.");
		din.close();
	}
	
	private static List<Instance> readTestData(String fname) throws IOException {
		System.out.println("Reading test data from file " + fname);
		Scanner din = new Scanner(new File(fname));
		din.nextLine();
		din.nextLine();
		// instance = classname and space separated attribute values
		List<Instance> instances = new ArrayList<Instance>();
		while (din.hasNext()) {
			instances.add(new Instance(new Scanner(din.nextLine())));
		}
		System.out.println("Read " + instances.size() + " instances.");
		din.close();
		return instances;
	}
}
