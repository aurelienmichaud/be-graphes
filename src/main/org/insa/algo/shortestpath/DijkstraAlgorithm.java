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
	
    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    public ShortestPathSolution doRun() throws ArrayIndexOutOfBoundsException, NullPointerException {
    	
    	ShortestPathData data = getInputData();
    	
    	Graph g = data.getGraph();
    	
    	Node origin      = data.getOrigin();
    	Node destination = data.getDestination();
    	
    	BinaryHeap<Label> bh = new BinaryHeap<Label>();
    	ArrayList<Label> labels = new ArrayList<Label>();
    	
        ShortestPathSolution solution = null;
        
        Path solutionPath;
        ArrayList<Arc> solutionArcs;
        
        Label destinationL;
        
        Label currentLabel;
        Label successorLabel;
        
        if (origin == null || destination == null)
        	throw new NullPointerException();
        
        if (g.size() <= 0)
        	throw new ArrayIndexOutOfBoundsException();
        
        if (g.size() == 1 || origin.equals(destination)) {
        	
        	solutionArcs = null;
        	
        	solutionPath = null;
	        
	        solution = new ShortestPathSolution(data, Status.INFEASIBLE, solutionPath);
	        
	        return solution;
        }
        
        if (destination.equals(origin)) {
        	
        	solutionArcs = new ArrayList<Arc>();
        	
        	solutionPath = new Path(g, solutionArcs);
	        
	        solution = new ShortestPathSolution(data, Status.OPTIMAL, solutionPath);
	        
	        return solution;
        	
        }
        
        bh.insert(new Label(origin, 0.0));
          
        for (int i = 0; i < g.size(); i++) {
        	if (g.get(i).compareTo(origin) == 0) {
        		labels.add(bh.findMin());
        	}
        	else {
        		labels.add(new Label(g.get(i)));
        	}
        }

        while (!bh.isEmpty()) {
        	
        	currentLabel = bh.findMin();
        	
        	if (currentLabel.getNode().equals(destination)) {
    			break;
    		}
        	
        	currentLabel.setMark(true);
        	
        	if (currentLabel.getNode().hasSuccessors()) {
        	
	        	for (Arc successorArc: currentLabel.getNode().getSuccessors()) {
	        		
	        		successorLabel = labels.get(successorArc.getDestination().getId());
	        		
	        		if (! successorLabel.isMarked()) {
		        		
		        		if (!(data.isAllowed(successorArc))) {	
		        			continue;
		        		}
		        		else if (successorLabel.getTotalCost() > (currentLabel.getTotalCost() + data.getCost(successorArc))) {
	        				
	        				successorLabel.setCost(currentLabel.getTotalCost() + data.getCost(successorArc));
	        				
	        				successorLabel.setFatherArc(successorArc);

	        				bh.insert(successorLabel);
	        			}
	        		}
	        	}
        	}
        	
        	notifyNodeReached(currentLabel.getNode());
        	
        	bh.remove(currentLabel);
        }
        
        destinationL = labels.get(destination.getId());
        
        solutionArcs = new ArrayList<Arc>();
        
        
        if (destinationL.getFatherArc() == null) {
        	
        	solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        	
        } else {
        
	        while (!(destinationL.getNode().equals(origin))) {
	        	
	        	solutionArcs.add(destinationL.getFatherArc());
	        	destinationL = labels.get(destinationL.getFather().getId());
	        	
	        }
	        
	        Collections.reverse(solutionArcs);
	        solutionPath = new Path(g, solutionArcs);
	        
	        solution = new ShortestPathSolution(data, Status.OPTIMAL, solutionPath);
        
        }
        
        return solution;
    }
}
