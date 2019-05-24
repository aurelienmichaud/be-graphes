package org.insa.algo;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Random;

import org.insa.algo.shortestpath.DijkstraAlgorithm;
import org.insa.algo.shortestpath.ShortestPathData;
import org.insa.algo.shortestpath.AStarAlgorithm;

import org.insa.algo.AbstractInputData.Mode;
import org.insa.graph.Graph;
import org.insa.graph.io.BinaryGraphReader;
import org.insa.graph.io.GraphReader;

public class ReadTestFile {
	
	private static ArcInspector AI0;
	private static ArcInspector AI2;
	
	public static int readTestFile(String file_path, String map_path) throws IOException {
		
		String map_name;
		
		Mode m;
		
		int test_nb;
		
		DijkstraAlgorithm d;
		AStarAlgorithm a;
		
		GraphReader reader = new BinaryGraphReader(
					new DataInputStream(new BufferedInputStream(new FileInputStream(map_path))));

		// Get the graph from the map
		Graph g = reader.read();
		int r1;
		int r2;
				
		try (BufferedReader br = new BufferedReader(new FileReader(file_path))) {
			
			String l = br.readLine();
			String[] node_ids;
			
			map_name = l;
			
			br.readLine();
			
			if (l.compareTo("0") == 0)
				m = Mode.LENGTH;
			else
				m = Mode.TIME;
			
			l = br.readLine();
			
			test_nb = Integer.parseInt(l);
			
			try (Writer w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/home/a_michau/Documents/BE_graphe/" + map_name + "_" + test_nb + "_" + "dijkstra"), "utf-8"))) {
			
				w.write(map_name);
				w.write(System.lineSeparator());
				
				if (m == Mode.LENGTH)
					w.write("0");
				else
					w.write("1");
				
				w.write(System.lineSeparator());
				
				w.write(Integer.toString(test_nb));
				w.write(System.lineSeparator());
				w.close();
			}

			l = br.readLine();
			
			AI0 = ArcInspectorFactory.getAllFilters().get(0); // All roads, shortest path
			AI2 = ArcInspectorFactory.getAllFilters().get(3); // Only car roads, fastest path

			while(l != null) {
				
				node_ids = l.split("\\s+");
				
				r1 = Integer.parseInt(node_ids[0]);
				r2 = Integer.parseInt(node_ids[1]);
				
				if (m == Mode.LENGTH) {
					d = new DijkstraAlgorithm(new ShortestPathData(g, g.get(r1), g.get(r2), AI0));
					a = new AStarAlgorithm(new ShortestPathData(g, g.get(r1), g.get(r2), AI0));
				}
				else {
					d = new DijkstraAlgorithm(new ShortestPathData(g, g.get(r1), g.get(r2), AI2));
					a = new AStarAlgorithm(new ShortestPathData(g, g.get(r1), g.get(r2), AI2));
				}
				
				d.doRun();
				a.doRun();
				
				l = br.readLine();
			}
			
			br.close();
		}
		
		return 0;
	}

}
