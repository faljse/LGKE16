package org.omilab.robot.world;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CSVReadTool {
	private BufferedReader br;
	private String[] line;
	private int elementnumber;

	CSVReadTool(String csvFile){
		br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/"+csvFile)));
	}

	public boolean closeFile(String csvFile) {
		if (br != null) {
			try {
				br.close();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public String readLineElement() throws IOException {
		if (line != null && elementnumber >= line.length)
			nextLine();
		
		if (line != null && elementnumber < line.length) {
			return line[elementnumber++];
		}
		return null;
	}
	
	public boolean nextLine() throws IOException {
		String cvsSplitBy = ";";
		String wholeLine;

			if ((wholeLine = br.readLine()) != null) {
				line = wholeLine.split(cvsSplitBy);
				elementnumber = 0;
				return true;
			}
			else
				line = null;
				elementnumber = -1;
			
		System.out.println("");
		System.out.println("eof");
		System.out.println("");
		return false;
	}
 }

