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
    public Label newLabel() {
    	return new LabelStar();
    }
    
    @Override
    public Label newLabel(Node n, ShortestPathData data) {
    	return new LabelStar(n, this.getInputData());
    }
    
    @Override
    public Label newLabel(Node n, double cost, ShortestPathData data) {
    	return new LabelStar(n, cost, this.getInputData());
    }
}
