package nz.ac.vuw.comp307_2013t1.a1.part3.allenbenj;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class SplitDatafile {

	public static void main(String[] args) throws Exception {

		Scanner data = new Scanner(new File(args[0] + ".data"));

		int train_count = Integer.parseInt(args[1]);

		String suffix = args[2].equals("") ? "" : ("-" + args[2]);

		Writer train = new FileWriter(args[0] + "-training" + suffix + ".data");
		Writer test = new FileWriter(args[0] + "-test" + suffix + ".data");

		String head = data.nextLine() + "\n" + data.nextLine() + "\n";

		train.write(head);
		test.write(head);

		List<String> lines = new ArrayList<String>();
		while (data.hasNext()) {
			lines.add(data.nextLine());
		}

		Collections.shuffle(lines);

		int i = 0;
		for (; i < train_count && i < lines.size(); i++) {
			train.write(lines.get(i) + "\n");
		}
		for (; i < lines.size(); i++) {
			test.write(lines.get(i) + "\n");
		}

		data.close();
		train.close();
		test.close();

	}

}
