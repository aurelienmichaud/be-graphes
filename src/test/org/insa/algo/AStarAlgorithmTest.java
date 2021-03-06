package org.insa.algo;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import org.insa.algo.shortestpath.DijkstraAlgorithm;
import org.insa.algo.shortestpath.AStarAlgorithm;
import org.insa.algo.shortestpath.ShortestPathData;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.Path;
import org.insa.graph.RoadInformation;
import org.insa.graph.RoadInformation.RoadType;
import org.insa.graph.io.BinaryGraphReader;
import org.insa.graph.io.GraphReader;

import org.junit.BeforeClass;
import org.junit.Test;
import org.insa.algo.AbstractInputData.Mode;
import org.insa.algo.CreateTestFile;
import org.insa.algo.ReadTestFile;

public class AStarAlgorithmTest {
	// Path to map directory
	// private String pathToMap = "/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/";
	private String pathToMap = "C:\\Users\\Alex\\Desktop\\map_be_graphe\\";

	private static Graph nullGraph, emptyGraph, singleNodeGraph, startEqualsEndGraph;
	
	private static Node[] nodes;
	
	// List of arcs in the graph, a2b is the arc from node A (0) to B (1).
    @SuppressWarnings("unused")
    private static Arc a2b, a2c, a2e, b2c, c2d_1, c2d_2, c2d_3, c2a, d2a, d2e, e2d;
	
	private static ArcInspector AI0;
	private static ArcInspector AI2;
	
	private static DijkstraAlgorithm bSingleNodeGraph, bStartEqualsEndGraph;
	
	private static AStarAlgorithm dNullGraph, dEmptyGraph, dEmptyGraphButNodes, dSingleNodeGraph, dStartEqualsEndGraph;
	
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
		
		Node n0 = new Node(0, null);
		
		Node.linkNodes(n0, n0, 0, speed10, null);

		// Nodes from JUnit Path tests
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
		singleNodeGraph     = new Graph("ID", "", Arrays.asList(new Node[] { n0 }), null);
		startEqualsEndGraph = new Graph("ID", "", Arrays.asList(nodes), null);
		
		AI0 = ArcInspectorFactory.getAllFilters().get(0); // All roads, shortest path
		AI2 = ArcInspectorFactory.getAllFilters().get(3); // Only car roads, fastest path
		
		// Previous tests to know how Dijkstra should behave
		//bNullGraph           = new DijkstraAlgorithm(new ShortestPathData(nullGraph, null, null, AI0));
		//bEmptyGraph          = new DijkstraAlgorithm(new ShortestPathData(emptyGraph, null, null, AI0));
		//bEmptyGraphButNodes  = new DijkstraAlgorithm(new ShortestPathData(emptyGraph, n0, n0, AI0));
		bSingleNodeGraph     = new DijkstraAlgorithm(new ShortestPathData(singleNodeGraph, n0, n0, AI0));
		bStartEqualsEndGraph = new DijkstraAlgorithm(new ShortestPathData(startEqualsEndGraph, n0, n0, AI0));
		
		dNullGraph           = new AStarAlgorithm(new ShortestPathData(nullGraph, null, null, AI0));
		dEmptyGraph          = new AStarAlgorithm(new ShortestPathData(emptyGraph, null, null, AI0));
		dEmptyGraphButNodes  = new AStarAlgorithm(new ShortestPathData(emptyGraph, n0, n0, AI0));
		dSingleNodeGraph     = new AStarAlgorithm(new ShortestPathData(singleNodeGraph, n0, n0, AI0));
		dStartEqualsEndGraph = new AStarAlgorithm(new ShortestPathData(startEqualsEndGraph, n0, n0, AI0));
	}
	
	// =====================================================================
	//						Sensitive cases
	// =====================================================================

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
	
	@Test//(expected = ArrayIndexOutOfBoundsException.class)
	public void testSingleNodeGraph() {

		Path dp, bp;
		bp = bSingleNodeGraph.doRun().getPath();
		dp = dSingleNodeGraph.doRun().getPath();

		// bp == null && dp == null
		if (bp != dp) 
			fail();
		else
			return;
	}

	@Test
	public void testStartEqualsEndGraph() {
		dStartEqualsEndGraph.doRun();
		Path dp, bp;
		bp = bStartEqualsEndGraph.doRun().getPath();
		dp = dStartEqualsEndGraph.doRun().getPath();
		
		// bp == null && dp == null
		if(bp != dp)
			fail();
		else
			return;
	}	
	
	// =====================================================================
	//						Dijkstra test on real maps
	// =====================================================================

	@Test
	public void testRandomCarreMapA0() throws IOException {
		// Fetch the map
		String mapName = this.pathToMap + "carre.mapgr";
	
		GraphReader reader = new BinaryGraphReader(
				new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
		// Get the graph from the map
		Graph g = reader.read();

		Random r = new Random();

		Node origin      = g.get(r.nextInt(g.size()));
		Node destination = g.get(r.nextInt(g.size()));

		DijkstraAlgorithm b = new DijkstraAlgorithm(new ShortestPathData(g, origin, destination, AI0));
		AStarAlgorithm d    = new AStarAlgorithm(new ShortestPathData(g, origin, destination, AI0));
		
		Path dp, bp;
		
		bp = b.doRun().getPath();
		dp = d.doRun().getPath();
		
		if (bp != null && dp != null) {
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
		// If the two path are null, it's still the same so it's great
		else if (bp == null && dp == null)
			return;
		else
			fail();

	}
	
	@Test
	public void testRandomCarreMapA2() throws IOException {
		// Fetch the map
		String mapName = this.pathToMap + "carre.mapgr";
	
		GraphReader reader = new BinaryGraphReader(
				new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
		// Get the graph from the map
		Graph g = reader.read();

		Random r = new Random();

		Node origin      = g.get(r.nextInt(g.size()));
		Node destination = g.get(r.nextInt(g.size()));

		DijkstraAlgorithm b = new DijkstraAlgorithm(new ShortestPathData(g, origin, destination, AI2));
		AStarAlgorithm d    = new AStarAlgorithm(new ShortestPathData(g, origin, destination, AI2));
		
		Path dp, bp;
		
		bp = b.doRun().getPath();
		dp = d.doRun().getPath();
		
		if (bp != null && dp != null) {
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
		// If the two path are null, it's still the same so it's great
		else if (bp == null && dp == null)
			return;
		else
			fail();

	}
	
	@Test
	public void testRandomInsaMapA0() throws IOException {
		// Fetch the map
		String mapName = this.pathToMap + "insa.mapgr";
	
		GraphReader reader = new BinaryGraphReader(
				new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));

		// Get the graph from the map
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

		DijkstraAlgorithm b = new DijkstraAlgorithm(new ShortestPathData(g, origin, destination, AI0));
		AStarAlgorithm d    = new AStarAlgorithm(new ShortestPathData(g, origin, destination, AI0));
		
		Path dp, bp;
		
		bp = b.doRun().getPath();
		dp = d.doRun().getPath();
		
		if (bp != null && dp != null) {
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
		// If the two path are null, it's still the same so it's great
		else if (bp == null && dp == null)
			return;
		else
			fail();

	}
	
	@Test
	public void testRandomInsaMapA2() throws IOException {
		// Fetch the map
		String mapName = this.pathToMap + "insa.mapgr";
	
		GraphReader reader = new BinaryGraphReader(
				new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));

		// Get the graph from the map
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

		DijkstraAlgorithm b = new DijkstraAlgorithm(new ShortestPathData(g, origin, destination, AI2));
		AStarAlgorithm d    = new AStarAlgorithm(new ShortestPathData(g, origin, destination, AI2));
		
		Path dp, bp;
		
		bp = b.doRun().getPath();
		dp = d.doRun().getPath();
		
		if (bp != null && dp != null) {
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
		// If the two path are null, it's still the same so it's great
		else if (bp == null && dp == null)
			return;
		else
			fail();

	}
	
	@Test
	public void testRandomToulouseMapA0() throws IOException {
		// Fetch the map
		String mapName = this.pathToMap + "toulouse.mapgr";
	
		GraphReader reader = new BinaryGraphReader(
				new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));

		// Get the graph from the map
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

		DijkstraAlgorithm b = new DijkstraAlgorithm(new ShortestPathData(g, origin, destination, AI0));
		AStarAlgorithm d    = new AStarAlgorithm(new ShortestPathData(g, origin, destination, AI0));
		
		Path dp, bp;
		
		bp = b.doRun().getPath();
		dp = d.doRun().getPath();
		
		if (bp != null && dp != null) {
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
		// If the two path are null, it's still the same so it's great
		else if (bp == null && dp == null)
			return;
		else
			fail();

	}
	
	@Test
	public void testRandomToulouseMapA2() throws IOException {
		// Fetch the map
		String mapName = this.pathToMap + "toulouse.mapgr";
	
		GraphReader reader = new BinaryGraphReader(
				new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));

		// Get the graph from the map
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

		DijkstraAlgorithm b = new DijkstraAlgorithm(new ShortestPathData(g, origin, destination, AI2));
		AStarAlgorithm d    = new AStarAlgorithm(new ShortestPathData(g, origin, destination, AI2));
		
		Path dp, bp;
		
		bp = b.doRun().getPath();
		dp = d.doRun().getPath();
		
		if (bp != null && dp != null) {
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
		// If the two path are null, it's still the same so it's great
		else if (bp == null && dp == null)
			return;
		else
			fail();

	}
	
	@Test
	public void testRandomCarreDenseMapA0() throws IOException {
		// Fetch the map
		String mapName = this.pathToMap + "carre-dense.mapgr";
	
		GraphReader reader = new BinaryGraphReader(
				new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));

		// Get the graph from the map
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

		DijkstraAlgorithm b = new DijkstraAlgorithm(new ShortestPathData(g, origin, destination, AI0));
		AStarAlgorithm d    = new AStarAlgorithm(new ShortestPathData(g, origin, destination, AI0));
		
		Path dp, bp;
		
		bp = b.doRun().getPath();
		dp = d.doRun().getPath();
		
		if (bp != null && dp != null) {
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
		// If the two path are null, it's still the same so it's great
		else if (bp == null && dp == null)
			return;
		else
			fail();

	}
	
	@Test
	public void testRandomCarreDenseMapA2() throws IOException {
		// Fetch the map
		String mapName = this.pathToMap + "carre-dense.mapgr";
	
		GraphReader reader = new BinaryGraphReader(
				new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));

		// Get the graph from the map
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

		DijkstraAlgorithm b = new DijkstraAlgorithm(new ShortestPathData(g, origin, destination, AI2));
		AStarAlgorithm d    = new AStarAlgorithm(new ShortestPathData(g, origin, destination, AI2));
		
		Path dp, bp;
		
		bp = b.doRun().getPath();
		dp = d.doRun().getPath();
		
		if (bp != null && dp != null) {
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
		// If the two path are null, it's still the same so it's great
		else if (bp == null && dp == null)
			return;
		else
			fail();

	}

}
