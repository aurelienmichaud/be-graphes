package org.insa.graph;

public class LabelStar extends Label implements Comparable<Label> {
	
	private double estimatedCostToGoal;
	
	public LabelStar() {
		super();
		estimatedCostToGoal = Double.POSITIVE_INFINITY;
	}
	
	public LabelStar(Node current_node) {
		super(current_node);
		estimatedCostToGoal = Double.POSITIVE_INFINITY;
	}
	
	public LabelStar(Node current_node, double cost) {
		super(current_node, cost);
		estimatedCostToGoal = Double.POSITIVE_INFINITY;
	}
	
	public double getEstimatedCostToGoal() {
		return this.estimatedCostToGoal;
	}
	
	public void setEstimatedCostToGoal(double cost) {
		estimatedCostToGoal = cost;
	}

	public double getTotalCost() {
		return super.getCost() + this.estimatedCostToGoal;
	}
}
