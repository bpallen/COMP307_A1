package nz.ac.vuw.comp307_2013t1.a1.part3.allenbenj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Instance {

	private static final Map<String, Integer> att_indices = new HashMap<String, Integer>();
	
	private String cat;
	private List<Boolean> atts = new ArrayList<Boolean>();
	
	public static void useAttributeNames(List<String> att_names) {
		att_indices.clear();
		for (int i = 0; i < att_names.size(); i++) {
			att_indices.put(att_names.get(i), i);
		}
	}
	
	public static Set<String> getAttributeSet() {
		return Collections.unmodifiableSet(att_indices.keySet());
	}
	
	public static int getAttributeIndex(String att_name) {
		return att_indices.get(att_name);
	}

	public Instance(Scanner scan) {
		cat = scan.next().toLowerCase();
		while (scan.hasNextBoolean()) {
			atts.add(scan.nextBoolean());
		}
	}

	public String getCategory() {
		return cat;
	}

	public boolean getAttribute(int i) {
		return atts.get(i);
	}
	
	public boolean getAttribute(String att_name) {
		return atts.get(att_indices.get(att_name));
	}

	@Override
	public String toString() {
		return cat + atts;
	}

}
