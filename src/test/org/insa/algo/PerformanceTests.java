package org.insa.algo;

import static org.junit.Assert.*;

import org.insa.algo.AbstractInputData.Mode;

import org.insa.algo.CreateTestFile;
import org.insa.algo.ReadTestFile;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

public class PerformanceTests {
	// Path to results file
	//private String pathToResults = "/home/a_michau/Documents/BE_graphe/test/";
	private String pathToResults = "C:\\Users\\Alex\\Desktop\\be_graphes_results\\";
	// Path to map directory
	//private String pathToMap = "/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/";
	private String pathToMap = "C:\\Users\\Alex\\Desktop\\map_be_graphe\\";
	
	@Test
	public void testAStar() throws IOException {
		CreateTestFile.createTestFile(this.pathToResults + "carre-dense_distance_100_data.txt", this.pathToMap + "carre-dense.mapgr", "carre-dense", 100, Mode.LENGTH);
		ReadTestFile.readTestFile(this.pathToResults, "carre-dense_distance_100_data.txt", this.pathToMap + "carre-dense.mapgr");
	}
}
