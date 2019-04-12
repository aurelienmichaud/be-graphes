package org.insa.graph;

public class Label {
	
	private Node current_node;
	
	private boolean marked;
	
	private double cost;
	
	private Arc fatherArc;
	
	public Label(Node current_node) {
		
		this.current_node = current_node;
		
		this.marked = false;
		
		this.cost = 0.0;
		
		this.fatherArc = null;
		
	}
	
	/* If the cost is provided */
	public Label(Node current_node, double cost) {
		
		this.current_node = current_node;
		
		this.marked = false;
		
		this.cost = cost;
		
		this.fatherArc = null;
		
	}
	
	public double getCost() {
		return this.cost;
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

}
