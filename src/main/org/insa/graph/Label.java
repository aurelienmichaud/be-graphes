package org.insa.graph;

public class Label implements Comparable<Label> {
	
	private Node current_node;
	
	private boolean marked;
	
	private double cost;
	
	private Arc fatherArc;
	
	public Label() {
		
		this.current_node = null;
		
		this.marked = false;
		
		this.cost = Double.POSITIVE_INFINITY;
		
		this.fatherArc = null;
		
	}
	
	public Label(Node current_node) {
		
		this.current_node = current_node;
		
		this.marked = false;
		
		this.cost = Double.POSITIVE_INFINITY;
		
		this.fatherArc = null;
		
	}
	
	/* If the cost is provided */
	public Label(Node current_node, double cost) {
		
		this.current_node = current_node;
		
		this.marked = false;
		
		this.cost = cost;
		
		this.fatherArc = null;
		
	}
	
	public Node getNode() {
		return this.current_node;
	}
	
	public double getCost() {
		return this.cost;
	}
	
	public void setCost(double cost) {
		this.cost = cost;
	}
	
	public boolean isMarked() {
		return this.marked;
	}
	
	public void setMark(boolean b) {
		this.marked = b;
	}
	
	public Arc getFatherArc() {
		return this.fatherArc;
	}
	
	public Node getFather() {
		return this.fatherArc.getOrigin();
	}
	
	public void setFatherArc(Arc Father) {
		this.fatherArc = Father;
	}
	
	public boolean equals(Label other) {
		if (this.current_node.equals(other.current_node))
			return true;
		return false;
	}
	
	public int compareTo(Label other) {
		return (int) this.getCost() - (int) other.getCost();
	}

}
