package nz.ac.vuw.comp307_2013t1.a1.part3.allenbenj;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	private static List<String> catNames;
	private static List<String> attNames;
	private static List<Instance> allInstances;

	public static void main(String[] args) throws Exception {

		readDataFile(args[0]);

	}

	private static void readDataFile(String fname) throws IOException {
		// format of names file:
		// names of categories, separated by spaces
		// names of attributes category followed by true's and false's for each instance

		System.out.println("Reading data from file " + fname);
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
		allInstances = new ArrayList<Instance>();
		while (din.hasNext()) {
			allInstances.add(new Instance(new Scanner(din.nextLine())));
		}
		System.out.println("Read " + allInstances.size() + " instances.");

		din.close();
	}
}
