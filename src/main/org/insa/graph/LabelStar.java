package org.insa.graph;

import org.insa.algo.AbstractInputData.Mode;
import org.insa.algo.shortestpath.ShortestPathData;

public class LabelStar extends Label implements Comparable<Label> {
	
	private double estimatedCostToGoal;
	
	public LabelStar() {
		super();
		this.estimatedCostToGoal = Double.POSITIVE_INFINITY;
	}
	
	public LabelStar(Node current_node, ShortestPathData data) {
		super(current_node);
		if (data.getMode() == Mode.LENGTH)
			this.setEstimatedCostToGoal(data.getDestination().getPoint().distanceTo(current_node.getPoint()));
		else
			this.setEstimatedCostToGoal(data.getDestination().getPoint().distanceTo(current_node.getPoint()) / 130);
	}
	
	public LabelStar(Node current_node, double cost, ShortestPathData data) {
		super(current_node, cost);
		if (data.getMode() == Mode.LENGTH)
			this.setEstimatedCostToGoal(data.getDestination().getPoint().distanceTo(current_node.getPoint()));
		else
			this.setEstimatedCostToGoal(data.getDestination().getPoint().distanceTo(current_node.getPoint()) / 130);
	}
	
	public double getEstimatedCostToGoal() {
		return this.estimatedCostToGoal;
	}
	
	public void setEstimatedCostToGoal(double cost) {
		this.estimatedCostToGoal = cost;
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
