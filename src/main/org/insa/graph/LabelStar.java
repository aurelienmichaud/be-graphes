package org.insa.graph;

public class LabelStar extends Label {
	
	public LabelStar() {
		super();
	}
	
	public LabelStar(Node current_node) {
		super(current_node);
	}
	
	public LabelStar(Node current_node, double cost) {
		super(current_node, cost);
	}

	public double getTotalCost() {
		return super.getCost();
	}
}
