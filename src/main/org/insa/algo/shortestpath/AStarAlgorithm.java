package org.insa.algo.shortestpath;

import java.util.ArrayList;
import java.util.Collections;

import org.insa.algo.AbstractInputData.Mode;
import org.insa.algo.AbstractSolution.Status;
import org.insa.algo.utils.BinaryHeap;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Label;
import org.insa.graph.LabelStar;
import org.insa.graph.Node;
import org.insa.graph.Path;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    public ShortestPathSolution doRun() throws ArrayIndexOutOfBoundsException, NullPointerException {
    	
    	ShortestPathData data = getInputData();
    	
    	Graph g = data.getGraph();
    	
    	Node origin      = data.getOrigin();
    	Node destination = data.getDestination();
    	
    	BinaryHeap<Label> bh = new BinaryHeap<Label>();
    	ArrayList<LabelStar> labels = new ArrayList<LabelStar>();
    	
        ShortestPathSolution solution = null;
        
        Path solutionPath;
        ArrayList<Arc> solutionArcs;
        
        LabelStar destinationL;
        
        LabelStar currentLabel;
        LabelStar successorLabel;

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
        
        bh.insert(new LabelStar(origin, 0.0));
        // Calculate the estimated path to the destination
        ((LabelStar)(bh.findMin())).setEstimatedCostToGoal(origin.getPoint().distanceTo(destination.getPoint()));
        
        for (int i = 0; i < g.size(); i++) {
        	labels.add(new LabelStar(g.get(i)));
        	// Calculate the estimated path to the destination
        	if (data.getMode() == Mode.LENGTH)
        		labels.get(i).setEstimatedCostToGoal(labels.get(i).getNode().getPoint().distanceTo(destination.getPoint()));
        	else
        		labels.get(i).setEstimatedCostToGoal(labels.get(i).getNode().getPoint().distanceTo(destination.getPoint()) / data.getMaximumSpeed());
        }

        while (!bh.isEmpty()) {
        	
        	currentLabel = (LabelStar)bh.findMin();
        	
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
		        		else if (successorLabel.getTotalCost() > (currentLabel.getCost() + data.getCost(successorArc) + successorLabel.getEstimatedCostToGoal())/*successorLabel.compareTo(currentLabel) < 0*/) {	
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
