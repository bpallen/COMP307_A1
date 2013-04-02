package nz.ac.vuw.comp307_2013t1.a1.part3.allenbenj;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Instance {

	private static final List<String> att_names = new ArrayList<String>();
	
	private String cat;
	private List<Boolean> atts = new ArrayList<Boolean>();
	
	public static void useAttributeNames(List<String> att_names_) {
		att_names.clear();
		att_names.addAll(att_names_);
	}
	
	public static Set<String> getAttributeSet() {
		Set<String> s = new HashSet<String>();
		s.addAll(att_names);
		return s;
	}
	
	public static String getAttributeName(int i) {
		return att_names.get(i);
	}
	
	public static int getAttributeIndex(String att_name) {
		return att_names.indexOf(att_name);
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
		return atts.get(att_names.indexOf(att_name));
	}

	@Override
	public String toString() {
		return cat + atts;
	}

}
