package org.insa.algo;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

import java.util.Random;
import java.util.Random.*;

import org.junit.BeforeClass;
import org.junit.Test;

import org.insa.algo.ArcInspector;
import org.insa.algo.ArcInspectorFactory;

import org.insa.graph.*;
import org.insa.graph.RoadInformation.RoadType;
import org.insa.algo.shortestpath.*;

import org.insa.graph.Graph;
import org.insa.graph.Path;
import org.insa.graph.io.BinaryGraphReader;
import org.insa.graph.io.BinaryPathReader;
import org.insa.graph.io.GraphReader;
import org.insa.graph.io.PathReader;


public class DijkstraTest {
	
	private static Graph graph;
	private static Graph nullGraph, emptyGraph, singleNodeGraph, loopGraph, startEqualsEndGraph;
	
	private static Node[] nodes;
	
	// List of arcs in the graph, a2b is the arc from node A (0) to B (1).
    @SuppressWarnings("unused")
    private static Arc a2b, a2c, a2e, b2c, c2d_1, c2d_2, c2d_3, c2a, d2a, d2e, e2d;
	
	private static ArcInspector AI0;
	private static ArcInspector AI3;
	
	private static BellmanFordAlgorithm bEmptyGraph, bEmptyGraphButNodes, bSingleNodeGraph, bLoopGraph, bStartEqualsEndGraph;
	
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
		
		// Previous tests to know how Dijkstra should behave
		//bNullGraph           = new BellmanFordAlgorithm(new ShortestPathData(nullGraph, null, null, AI0));
		//bEmptyGraph          = new BellmanFordAlgorithm(new ShortestPathData(emptyGraph, null, null, AI0));
		bEmptyGraphButNodes  = new BellmanFordAlgorithm(new ShortestPathData(emptyGraph, nodes[0], nodes[0], AI0));
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

	@Test
	public void testStartEqualsEndGraph() {
		bStartEqualsEndGraph.doRun();
		//dStartEqualsEndGraph.doRun();
		//assertEquals(dStartEqualsEndGraph.doRun(), bStartEqualsEndGraph.doRun());
	}	

	@Test
	public void testRandomCarreMap() throws IOException {
		String mapName = "/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/carre.mapgr";
	
		GraphReader reader = new BinaryGraphReader(
				new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));

		Graph g = reader.read();

		Random r = new Random();

		Node origin      = g.get(r.nextInt(g.size()));
		Node destination = g.get(r.nextInt(g.size()));

		BellmanFordAlgorithm b = new BellmanFordAlgorithm(new ShortestPathData(g, origin, destination, AI0));
		DijkstraAlgorithm d    = new DijkstraAlgorithm(new ShortestPathData(g, origin, destination, AI0));
		
		Path dp, bp;
		
		bp = b.doRun().getPath();
		dp = d.doRun().getPath();
		
		// Test if the arcs are the same
		Iterator<Arc> ba = bp.getArcs().iterator();
		Iterator<Arc> da = dp.getArcs().iterator();
		
		while (ba.hasNext() && da.hasNext()) {
			if(ba.next().getOrigin().equals(da.next().getDestination()))
				fail();
		}
		
		// One has more arcs than the other one
		if (ba.hasNext() || da.hasNext())
			fail();
		
		return;

	}
	
	@Test
	public void testRandomInsaMap() throws IOException {
		String mapName = "/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/insa.mapgr";
	
		GraphReader reader = new BinaryGraphReader(
				new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));

		Graph g = reader.read();

		Random r = new Random();
		int r1 = 0;
		int r2 = r1;
		
		while (r1 == r2) {
			r1 = r.nextInt(g.size());
			r2 = r.nextInt(g.size());
		}

		Node origin      = g.get(r1);
		Node destination = g.get(r2);

		BellmanFordAlgorithm b = new BellmanFordAlgorithm(new ShortestPathData(g, origin, destination, AI0));
		DijkstraAlgorithm d    = new DijkstraAlgorithm(new ShortestPathData(g, origin, destination, AI0));
		
		Path dp, bp;
		
		bp = b.doRun().getPath();
		dp = d.doRun().getPath();
		
		// Test if the arcs are the same
		Iterator<Arc> ba = bp.getArcs().iterator();
		Iterator<Arc> da = dp.getArcs().iterator();
		
		while (ba.hasNext() && da.hasNext()) {
			if(ba.next().getOrigin().equals(da.next().getDestination()))
				fail();
		}
		
		// One has more arcs than the other one
		if (ba.hasNext() || da.hasNext())
			fail();
		
		return;

	}
	
	@Test
	public void testRandomCarreDenseMap() throws IOException {
		String mapName = "/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/carre-dense.mapgr";
	
		GraphReader reader = new BinaryGraphReader(
				new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));

		Graph g = reader.read();

		Random r = new Random();
		int r1 = 0;
		int r2 = r1;
		
		while (r1 == r2) {
			r1 = r.nextInt(g.size());
			r2 = r.nextInt(g.size());
		}

		Node origin      = g.get(r1);
		Node destination = g.get(r2);

		BellmanFordAlgorithm b = new BellmanFordAlgorithm(new ShortestPathData(g, origin, destination, AI0));
		DijkstraAlgorithm d    = new DijkstraAlgorithm(new ShortestPathData(g, origin, destination, AI0));
		
		Path dp, bp;
		
		bp = b.doRun().getPath();
		System.out.println("Bellman Ford just finished !");
		dp = d.doRun().getPath();
		System.out.println("Dijkstra just finished !");
		
		// Test if the arcs are the same
		Iterator<Arc> ba = bp.getArcs().iterator();
		Iterator<Arc> da = dp.getArcs().iterator();
		
		while (ba.hasNext() && da.hasNext()) {
			if(ba.next().getOrigin().equals(da.next().getDestination()))
				fail();
		}
		
		// One has more arcs than the other one
		if (ba.hasNext() || da.hasNext())
			fail();
		
		return;

	}
}
