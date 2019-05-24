package org.insa.algo;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.NoSuchFileException;

public class WriteAlgoData {
	
	public static void writeAlgoData(String dataFilePath, int origin, int destination, float cost, long cpu_time, long explored_nb, long marked_nb, int bh_max_size) {
		String data_to_write;

		if (cost != -1) {
	    data_to_write = origin + " " +
	    			destination + " " +
	    			cost + " " +
	    			cpu_time + " " +
	    			explored_nb + " " +
	    			marked_nb + " " +
	    			bh_max_size;
		} else {
			data_to_write = "N N N N N N N";
		}

    	try {
    		
    		FileWriter fw = new FileWriter(dataFilePath, true);
    		fw.write(data_to_write);
    		fw.write(System.lineSeparator());
    		fw.flush();
    		fw.close();
    	}
    	catch (NoSuchFileException e) {
    		System.out.println(e);
    	}
    	catch (IOException i) {
    		System.out.println(i);
    	}
	}

}
