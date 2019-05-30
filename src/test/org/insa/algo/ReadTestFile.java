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
	
	public static int readTestFile(String path_rw, String file_name, String map_path) throws IOException {
		
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
				
		// Read from the test configuration file
		try (BufferedReader br = new BufferedReader(new FileReader(path_rw + file_name))) {
			
			String l = br.readLine();
			String[] node_ids;
			String pathDijkstra;
			String pathAStar;
			
			map_name = l;
			
			l = br.readLine();
			
			if (l.compareTo("0") == 0)
				m = Mode.LENGTH;
			else
				m = Mode.TIME;
			
			l = br.readLine();
			
			test_nb = Integer.parseInt(l);
			
			if (m == Mode.LENGTH) {
	    		pathDijkstra = path_rw + map_name + "_" + "distance_" + test_nb + "_" + "dijkstra" + ".txt";
	    		pathAStar = path_rw + map_name + "_" + "distance_" + test_nb + "_" + "astar" + ".txt";
			}
	    	else {
	    		pathDijkstra = path_rw + map_name + "_" + "temps_" + test_nb + "_" + "dijkstra" + ".txt";
	    		pathAStar = path_rw + map_name + "_" + "temps_" + test_nb + "_" + "astar" + ".txt";
	    	}
			
			// We start to write the map name and the mode in the result file
			try (Writer w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathDijkstra), "utf-8"))) {
			
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
			
			try (Writer w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathAStar), "utf-8"))) {
				
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

			// Main loop which reads the configuration file and operates
			// the tests launching Dijkstra and AStar
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
				
				// Launch the algorithms
				d.doRun();
				a.doRun();
				
				// Fetch the data and write them in results file
				WriteAlgoData.writeAlgoData(pathDijkstra,
						g.get(r1).getId(),
						g.get(r2).getId(),
						d.getCost(),
						d.getCPUTime(),
						d.getExploredNb(),
						d.getMarkedNb(),
						d.getBHMaxSize());
				
				WriteAlgoData.writeAlgoData(pathAStar,
						g.get(r1).getId(),
						g.get(r2).getId(),
						a.getCost(),
						a.getCPUTime(),
						a.getExploredNb(),
						a.getMarkedNb(),
						a.getBHMaxSize());

				l = br.readLine();
			}
			
			br.close();
		}
		
		return 0;
	}

}
