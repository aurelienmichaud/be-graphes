package org.insa.algo;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

import org.insa.algo.ArcInspector;
import org.insa.algo.ArcInspectorFactory;

import org.insa.graph.*;
import org.insa.graph.RoadInformation.RoadType;
import org.insa.algo.shortestpath.*;

public class DijkstraTest {
	
	private static Graph graph;
	private static Graph nullGraph, emptyGraph, singleNodeGraph, loopGraph, startEqualsEndGraph;
	
	private static Node[] nodes;
	
	// List of arcs in the graph, a2b is the arc from node A (0) to B (1).
    @SuppressWarnings("unused")
    private static Arc a2b, a2c, a2e, b2c, c2d_1, c2d_2, c2d_3, c2a, d2a, d2e, e2d;
	
	private static ArcInspector AI0;
	private static ArcInspector AI3;
	
	private static BellmanFordAlgorithm bEmptyGraph, bSingleNodeGraph, bLoopGraph, bStartEqualsEndGraph;
	
	private static ShortestPathSolution sps;
	
	private static DijkstraAlgorithm dNullGraph, dEmptyGraph, dEmptyGraphButNodes, dSingleNodeGraph, dLoopGraph, dStartEqualsEndGraph;
	
	@BeforeClass
	public static void initAll() throws IOException {
		
		// 10 and 20 meters per seconds
        RoadInformation speed10 = new RoadInformation(RoadType.MOTORWAY, null, true, 36, ""),
                        speed20 = new RoadInformation(RoadType.MOTORWAY, null, true, 72, "");

		// Create nodes
		nodes = new Node[5];
		for (int i = 0; i < nodes.length; ++i) {
			nodes[i] = new Node(i, null);
		}

		// Add arcs...
		a2b   = Node.linkNodes(nodes[0], nodes[1], 10, speed10, null);
		a2c   = Node.linkNodes(nodes[0], nodes[2], 15, speed10, null);
		a2e   = Node.linkNodes(nodes[0], nodes[4], 15, speed20, null);
		b2c   = Node.linkNodes(nodes[1], nodes[2], 10, speed10, null);
		c2d_1 = Node.linkNodes(nodes[2], nodes[3], 20, speed10, null);
		c2d_2 = Node.linkNodes(nodes[2], nodes[3], 10, speed10, null);
		c2d_3 = Node.linkNodes(nodes[2], nodes[3], 15, speed20, null);
		d2a   = Node.linkNodes(nodes[3], nodes[0], 15, speed10, null);
		d2e   = Node.linkNodes(nodes[3], nodes[4], 22.8f, speed20, null);
		e2d   = Node.linkNodes(nodes[4], nodes[0], 10, speed10, null);

		nullGraph           = new Graph(null, null, new ArrayList<Node>(), null);
		emptyGraph          = new Graph("ID", "", new ArrayList<Node>(), null);
		singleNodeGraph     = new Graph("ID", "", Arrays.asList(new Node[] { nodes[0] }), null);
		startEqualsEndGraph = new Graph("ID", "", Arrays.asList(nodes), null);

		AI0 = ArcInspectorFactory.getAllFilters().get(0);
		AI3 = ArcInspectorFactory.getAllFilters().get(3);
		
		// Nothing
		// Nothing
		// Nothing
		bSingleNodeGraph     = new BellmanFordAlgorithm(new ShortestPathData(singleNodeGraph, nodes[0], nodes[0], AI0));
		bStartEqualsEndGraph = new BellmanFordAlgorithm(new ShortestPathData(startEqualsEndGraph, nodes[0], nodes[0], AI0));
		
		dNullGraph           = new DijkstraAlgorithm(new ShortestPathData(nullGraph, null, null, AI0));
		dEmptyGraph          = new DijkstraAlgorithm(new ShortestPathData(emptyGraph, null, null, AI0));
		dEmptyGraphButNodes  = new DijkstraAlgorithm(new ShortestPathData(emptyGraph, nodes[0], nodes[0], AI0));
		dSingleNodeGraph     = new DijkstraAlgorithm(new ShortestPathData(singleNodeGraph, nodes[0], nodes[0], AI0));
		dStartEqualsEndGraph = new DijkstraAlgorithm(new ShortestPathData(startEqualsEndGraph, nodes[0], nodes[0], AI0));
	}

	@Test(expected = NullPointerException.class)
	public void testNullGraph() {
		dNullGraph.doRun();
	}
	
	@Test(expected = NullPointerException.class)
	public void testEmptyGraph() {
		dEmptyGraph.doRun();
	}
	
	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testEmptyGraphButNodes() {
		dEmptyGraphButNodes.doRun();
	}
	
	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testSingleNodeGraph() {
		bSingleNodeGraph.doRun();
	}
}
