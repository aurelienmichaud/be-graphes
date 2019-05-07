package org.insa.algo.shortestpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.insa.algo.utils.BinaryHeap;

import org.insa.algo.AbstractSolution.Status;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Label;
import org.insa.graph.Node;
import org.insa.graph.Path;
import org.insa.graph.RoadInformation;
import org.insa.graph.AccessRestrictions;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {
	
	Graph g;
	
	Node origin;
	Node destination;
	
	
	BinaryHeap<Label> bh;
	
	ArrayList<Label> labels;

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
    protected ShortestPathSolution doRun() {
        ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        
        Path solutionPath;
        ArrayList<Arc> solutionArcs;
        
        Label destination;
        
        Label currentLabel;
        Label successorLabel;
        
        while (!bh.isEmpty()) {
        	
        	currentLabel = bh.findMin();
        	
        	currentLabel.setMark(true);
        	
        	if (currentLabel.getNode().hasSuccessors()) {
        	
	        	for (Arc successorArc: currentLabel.getNode().getSuccessors()) {
	        		
	        		successorLabel = labels.get(successorArc.getDestination().getId());
	        		
	        		if (! successorLabel.isMarked()) {
	        			
		        		
		        		if (!(data.isAllowed(successorArc))) {
		        			
		        			System.out.println("yessss");
		        			continue;
		        			
		        		}
	        			
		        		else if(successorLabel.getCost() == -1 || successorLabel.getCost() > (currentLabel.getCost() + successorArc.getLength())) {
	        				
	        				successorLabel.setCost(currentLabel.getCost() + successorArc.getLength());
	        				
	        				successorLabel.setFatherArc(successorArc);
	        				
	        				bh.insert(successorLabel);
	        				
	        			}
	        		}
	        		
	        	}
        	}
        	
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
