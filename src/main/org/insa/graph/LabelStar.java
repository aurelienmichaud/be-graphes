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
	
	public int compareTo(LabelStar other) {
		// Since if the subtraction is between 0 and 1, returning a int will return 0
		if (this.getTotalCost() - other.getTotalCost() > 0.0)
			return 1;
		else if (this.getTotalCost() - other.getTotalCost() < 0.0)
			return -1;
		else {
			// If they are equal
			if (this.getEstimatedCostToGoal() > other.getEstimatedCostToGoal())
				return 1;
			else if (this.getEstimatedCostToGoal() < other.getEstimatedCostToGoal())
				return -1;
			else
				return 0;
		}
	}
}
