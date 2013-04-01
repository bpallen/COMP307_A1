package nz.ac.vuw.comp307_2013t1.a1.part3.allenbenj;

import java.util.List;
import java.util.Set;

public class DecisionTree {

	private interface Node {

		public LeafNode categorise(Instance inst);

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

	}

	private class LeafNode implements Node {

		private String cat_name;
		private double prob;

		public LeafNode(String cat_name_, double prob_) {
			cat_name = cat_name_;
			prob = prob_;
		}

		@Override
		public LeafNode categorise(Instance inst) {
			return this;
		}

		public String getCategory() {
			return cat_name;
		}

		public double getProbability() {
			return prob;
		}

	}

	private Node root;
	private String baseline_cat;
	private double baseline_prob;

	public DecisionTree(List<Instance> instances_, Set<String> attributes_) {
		// TODO baseline

		root = buildNode(instances_, attributes_);
	}

	public String getBaselineCategory() {
		return baseline_cat;
	}

	public double getBaselineProbability() {
		return baseline_prob;
	}
	
	public String categorise(Instance inst) {
		return root.categorise(inst).getCategory();
	}

	private Node buildNode(List<Instance> instances, Set<String> attributes) {
		// TODO
		return null;
	}

}
