package org.insa.algo;

import org.insa.algo.AbstractInputData.Mode;

import org.insa.algo.CreateTestFile;
import org.insa.algo.ReadTestFile;

import java.io.IOException;

public class PerformanceTests {
	
	public PerformanceTests() {
		
	}
	
	public void testAStar() throws IOException {
		CreateTestFile.createTestFile("/home/a_michau/Documents/BE_graphe/test_data.txt", "/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/toulouse.mapgr", 10, Mode.LENGTH);
		ReadTestFile.readTestFile("/home/a_michau/Documents/BE_graphe/test_data.txt", "/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/toulouse.mapgr");
	}
}
