package org.insa.algo.shortestpath;

import java.util.ArrayList;
import java.util.Collections;

import org.insa.algo.utils.BinaryHeap;

import org.insa.algo.AbstractSolution.Status;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Label;
import org.insa.graph.Node;
import org.insa.graph.Path;


public class DijkstraAlgorithm extends ShortestPathAlgorithm {
	
	private Graph g;
	
	private Node origin;
	private Node destination;
	
	
	private BinaryHeap<Label> bh;
	
	private ArrayList<Label> labels;

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
        
        this.g = data.getGraph();
        
        this.origin = data.getOrigin();
        this.destination = data.getDestination();
        
        bh = new BinaryHeap<Label>();
        bh.insert(new Label(this.origin, 0.0));
        
        labels = new ArrayList<Label>();
        
        for (int i = 0; i < g.size(); i++) {
        	labels.add(new Label(g.get(i)));
        }
        
    }

    @Override
    public ShortestPathSolution doRun() throws ArrayIndexOutOfBoundsException, NullPointerException {
        ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        
        Path solutionPath;
        ArrayList<Arc> solutionArcs;
        
        Label destination;
        
        Label currentLabel;
        Label successorLabel;
        
        if (this.origin == null || this.destination == null)
        	throw new NullPointerException();
        
        if (this.g.size() <= 0)
        	throw new ArrayIndexOutOfBoundsException();
        
        if (this.g.size() == 1 || this.origin.equals(this.destination)) {
        	
        	solutionArcs = null;
        	
        	solutionPath = null;
	        
	        solution = new ShortestPathSolution(data, Status.INFEASIBLE, solutionPath);
	        
	        return solution;
        }
        
        if (this.destination.equals(this.origin)) {
        	
        	solutionArcs = new ArrayList<Arc>();
        	
        	solutionPath = new Path(g, solutionArcs);
	        
	        solution = new ShortestPathSolution(data, Status.OPTIMAL, solutionPath);
	        
	        return solution;
        	
        }

        while (!bh.isEmpty()) {
        	
        	currentLabel = bh.findMin();
        	
        	currentLabel.setMark(true);
        	
        	if (currentLabel.getNode().hasSuccessors()) {
        	
	        	for (Arc successorArc: currentLabel.getNode().getSuccessors()) {
	        		
	        		successorLabel = labels.get(successorArc.getDestination().getId());
	        		
	        		if (! successorLabel.isMarked()) {
	        			
		        		
		        		if (!(data.isAllowed(successorArc))) {	
		        			continue;
		        		}
		        		else if(successorLabel.getCost() == -1 || successorLabel.getCost() > (currentLabel.getCost() + data.getCost(successorArc))) {
	        				
	        				successorLabel.setCost(currentLabel.getCost() + data.getCost(successorArc));
	        				
	        				successorLabel.setFatherArc(successorArc);

	        				bh.insert(successorLabel);
	        			}
	        		}
	        	}
        	}
        	
        	notifyNodeReached(currentLabel.getNode());
        	
        	bh.remove(currentLabel);
        }
        
        destination = labels.get(this.destination.getId());
        
        solutionArcs = new ArrayList<Arc>();
        
        
        if (destination.getFatherArc() == null) {
        	
        	solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        	
        } else {
        
	        while (!(destination.getNode().equals(this.origin))) {
	        	
	        	solutionArcs.add(destination.getFatherArc());
	        	destination = labels.get(destination.getFather().getId());
	        	
	        }
	        
	        Collections.reverse(solutionArcs);
	        solutionPath = new Path(g, solutionArcs);
	        
	        solution = new ShortestPathSolution(data, Status.OPTIMAL, solutionPath);
        
        }
        
        return solution;
    }
}
