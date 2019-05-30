package org.insa.algo.shortestpath;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.insa.algo.utils.BinaryHeap;
import org.insa.algo.AbstractInputData.Mode;
import org.insa.algo.AbstractSolution.Status;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Label;
import org.insa.graph.Node;
import org.insa.graph.Path;


public class DijkstraAlgorithm extends ShortestPathAlgorithm {
	
	private float cost;
	private long cpu_time;
	private long marked_nb;
	private long explored_nb;
	private int bh_max_size;

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
        this.marked_nb   = 0;
        this.explored_nb = 0;
        this.bh_max_size = 0;
    }
    
    public Label newLabel() {
    	return new Label();
    }
    
    public Label newLabel(Node n, ShortestPathData data) {
    	return new Label(n);
    }
    
    public Label newLabel(Node n, double cost, ShortestPathData data) {
    	return new Label(n, cost);
    }

    @Override
    public ShortestPathSolution doRun() throws ArrayIndexOutOfBoundsException, NullPointerException {
    	
    	long start_cpu_time;
    	
    	ShortestPathData data = getInputData();
    	
    	Graph g = data.getGraph();
    	
    	Node origin      = data.getOrigin();
    	Node destination = data.getDestination();
    	
    	BinaryHeap<Label> bh = new BinaryHeap<Label>();
    	ArrayList<Label> labels = new ArrayList<Label>();
    	
        ShortestPathSolution solution = null;
        
        Path solutionPath;
        ArrayList<Arc> solutionArcs;
        
        Label destinationLabel;
        
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
        
        bh.insert(newLabel(origin, 0.0, data));
          
        for (int i = 0; i < g.size(); i++) {
        	if (g.get(i).compareTo(origin) == 0) {
        		labels.add(bh.findMin());
        	}
        	else {
        		labels.add(newLabel(g.get(i), data));
        	}
        }

        start_cpu_time = System.nanoTime();
        while (!bh.isEmpty()) {
        	
        	currentLabel = bh.findMin();
        	
        	if (currentLabel.getNode().equals(destination)) {
        		notifyDestinationReached(destination);
    			break;
    		}
        	
        	currentLabel.setMark(true);
        	// We could not see the path because of the color
        	//notifyNodeMarked(currentLabel.getNode());
        	this.marked_nb++;
        	
        	if (currentLabel.getNode().hasSuccessors()) {
        	
	        	for (Arc successorArc: currentLabel.getNode().getSuccessors()) {
	        		
	        		this.explored_nb++;
	        		
	        		successorLabel = labels.get(successorArc.getDestination().getId());
	        		
	        		if (! successorLabel.isMarked()) {
		        		
		        		if (!(data.isAllowed(successorArc))) {	
		        			continue;
		        		}
		        		else if (successorLabel.getCost() > (currentLabel.getCost() + data.getCost(successorArc))) {
	        				
	        				successorLabel.setCost(currentLabel.getCost() + data.getCost(successorArc));
	        				
	        				successorLabel.setFatherArc(successorArc);

	        				bh.insert(successorLabel);
	        				
	        				if (bh.size() > this.bh_max_size)
	        					this.bh_max_size = bh.size();
	        			}
	        		}
	        	}
        	}
        	
        	notifyNodeReached(currentLabel.getNode());
        	
        	bh.remove(currentLabel);
        }
        
        this.cpu_time = System.nanoTime() - start_cpu_time;
        
        destinationLabel = labels.get(destination.getId());
        
        solutionArcs = new ArrayList<Arc>();
        
        
        if (destinationLabel.getFatherArc() == null) {
        	
        	solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        	
        } else {
        
	        while (!(destinationLabel.getNode().equals(origin))) {
	        	
	        	solutionArcs.add(destinationLabel.getFatherArc());
	        	destinationLabel = labels.get(destinationLabel.getFather().getId());
	        	
	        }
	        
	        Collections.reverse(solutionArcs);
	        solutionPath = new Path(g, solutionArcs);
	        
	        solution = new ShortestPathSolution(data, Status.OPTIMAL, solutionPath);
        
        }
        
        try {
        	this.cost = solution.getPath().getLength();
        } catch(NullPointerException e) {
        	// No solution
        	this.cost = -1;
        }
        
        return solution;
    }

    public float getCost() {
    	return this.cost;
    }
    
    public long getCPUTime() {
    	return this.cpu_time;
    }
    
    public long getExploredNb() {
    	return this.explored_nb;
    }
    
    public long getMarkedNb() {
    	return this.marked_nb;
    }
    
    public int getBHMaxSize() {
    	return this.bh_max_size;
    }
}
