package nz.ac.vuw.comp307_2013t1.a1.part3.allenbenj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DecisionTree {

	public interface Categorisation {

		public String getCategory();

		public double getProbability();

		public int getInstanceCount();

	}

	private interface Node {

		public LeafNode categorise(Instance inst);

		public void print(String indent);

	}

	private class InternalNode implements Node {

		private String att_name;
		private Node child_true, child_false;

		public InternalNode(String att_name_, Node child_true_, Node child_false_) {
			att_name = att_name_;
			child_true = child_true_;
			child_false = child_false_;
		}

		@Override
		public LeafNode categorise(Instance inst) {
			if (inst.getAttribute(att_name)) {
				return child_true.categorise(inst);
			} else {
				return child_false.categorise(inst);
			}
		}

		@Override
		public void print(String indent) {
			System.out.println(indent + att_name + " = True:");
			child_true.print(indent + "    ");
			System.out.println(indent + att_name + " = False:");
			child_false.print(indent + "    ");
		}

	}

	private class LeafNode implements Node, Categorisation {

		private String cat_name;
		private double prob;
		private int inst_count;

		public LeafNode(String cat_name_, double prob_, int inst_count_) {
			cat_name = cat_name_;
			prob = prob_;
			inst_count = inst_count_;
		}

		@Override
		public LeafNode categorise(Instance inst) {
			return this;
		}

		@Override
		public String getCategory() {
			return cat_name;
		}

		@Override
		public double getProbability() {
			return prob;
		}

		@Override
		public int getInstanceCount() {
			return inst_count;
		}

		@Override
		public void print(String indent) {
			System.out.println(indent + this);
		}
		
		@Override
		public String toString() {
			return String.format("Category: %s, Probability: %.3f", cat_name, prob);
		}

	}

	private Node root;
	private LeafNode baseline;

	public DecisionTree(List<Instance> instances_) {
		// baseline
		baseline = majority(instances_);
		// build the tree!
		root = buildNode(instances_, Instance.getAttributeSet());
	}

	public Categorisation getBaseline() {
		return baseline;
	}

	public Categorisation categorise(Instance inst) {
		return root.categorise(inst);
	}
	
	public void print() {
		root.print("");
	}

	private Map<String, Integer> categoryCounts(List<Instance> instances) {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		for (Instance inst : instances) {
			String cat = inst.getCategory();
			if (!counts.containsKey(cat)) {
				counts.put(cat, 1);
			} else {
				counts.put(cat, counts.get(cat) + 1);
			}
		}
		return counts;
	}

	private LeafNode majority(List<Instance> instances) {
		Map<String, Integer> counts = categoryCounts(instances);
		double prob = 0;
		String cat = null;
		for (Map.Entry<String, Integer> me : counts.entrySet()) {
			double p = me.getValue() / (double) instances.size();
			// TODO randomisation if counts equal
			if (p > prob) {
				prob = p;
				cat = me.getKey();
			}
		}
		return new LeafNode(cat, prob, instances.size());
	}

	private Node buildNode(List<Instance> instances, Set<String> attributes) {
		if (instances.isEmpty()) {
			return baseline;
		} else if (ispure(instances)) {
			return new LeafNode(instances.get(0).getCategory(), 1, instances.size());
		} else if (attributes.isEmpty()) {
			return majority(instances);
		} else {
			double best_wimp = Double.POSITIVE_INFINITY;
			String best_att = null;
			List<Instance> best_inst_true = null;
			List<Instance> best_inst_false = null;
			// find best attribute to split on
			for (String att : attributes) {
				List<Instance> inst_true = new ArrayList<Instance>();
				List<Instance> inst_false = new ArrayList<Instance>();
				// split into attribute true and attribute false
				for (Instance inst : instances) {
					if (inst.getAttribute(att)) {
						inst_true.add(inst);
					} else {
						inst_false.add(inst);
					}
				}
				// weighted impurity
				double wimp = (inst_true.size() / (double) instances.size() * impurity(inst_true))
						+ (inst_false.size() / (double) instances.size() * impurity(inst_false));
				if (wimp < best_wimp) {
					best_wimp = wimp;
					best_att = att;
					best_inst_true = inst_true;
					best_inst_false = inst_false;
				}
			}
			attributes.remove(best_att);
			Set<String> attributes2 = new HashSet<String>(attributes);
			// dont need to create a new set for the other child
			Node child_true = buildNode(best_inst_true, attributes);
			Node child_false = buildNode(best_inst_false, attributes2);
			return new InternalNode(best_att, child_true, child_false);
		}
	}

	private boolean ispure(List<Instance> instances) {
		if (instances.isEmpty()) return true;
		String cat = instances.get(0).getCategory();
		for (Instance inst : instances) {
			if (!cat.equals(inst.getCategory())) return false;
		}
		return true;
	}

	private double impurity(List<Instance> instances) {
		// http://en.wikipedia.org/wiki/Decision_tree_learning#Formulae
		// Gini impurity
		// this should be able to handle more than two categories
		Map<String, Integer> counts = categoryCounts(instances);
		double imp = 1;
		for (Map.Entry<String, Integer> me : counts.entrySet()) {
			double fi = me.getValue() / (double) instances.size();
			imp -= fi * fi;
		}
		return imp;
	}

}
