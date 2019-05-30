package org.insa.algo;

import java.io.Writer;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Random;

import org.insa.graph.*;
import org.insa.graph.io.BinaryGraphReader;
import org.insa.graph.io.GraphReader;
import org.insa.algo.AbstractInputData.Mode;


public class CreateTestFile<E> {	
	
	public static int createTestFile(String file_path, String map_path, String map_name, int test_nb, Mode m) throws IOException {
			
		GraphReader reader = new BinaryGraphReader(
					new DataInputStream(new BufferedInputStream(new FileInputStream(map_path))));

		// Get the graph from the map
		Graph g = reader.read();
		
		Random r = new Random();
		int r1 = 0;
		int r2 = r1;
				
		// Write everything in a results file
		try (Writer w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file_path), "utf-8"))) {
			
			w.write(map_name);
			w.write(System.lineSeparator());
			
			if (m == Mode.LENGTH)
				w.write("0");
			else
				w.write("1");
			
			w.write(System.lineSeparator());
			
			w.write(Integer.toString(test_nb));
			w.write(System.lineSeparator());
			
			for (int i = 0; i < test_nb; i++, r1 = r2) {
			
				while (r1 == r2) {
					r1 = r.nextInt(g.size());
					r2 = r.nextInt(g.size());
				}
				
				w.write(r1 + " " + r2);
				w.write(System.lineSeparator());
			}
			
			w.close();
		}
		
		return 0;
	}

}
