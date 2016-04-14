package org.omilab.robot.world;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CSVReadTool {
	private BufferedReader br;
	private String[] line;
	private int elementnumber;
	
	public boolean openFile(String csvFile) {
		br = null;

		br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/"+csvFile)));
		
		if (br != null)
			return nextLine();
		return false;
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
	
	//only to be called after openFile()
	public String readLineElement() {
		if (line != null && elementnumber >= line.length)
			nextLine();
		
		if (line != null && elementnumber < line.length) {
			return line[elementnumber++];
		}
		
		return null;
	}
	
	public boolean nextLine() {
		String cvsSplitBy = ";";
		String wholeLine;
		try {
			
			if ((wholeLine = br.readLine()) != null) {
				line = wholeLine.split(cvsSplitBy);
				elementnumber = 0;
				return true;
			}
			else
				line = null;
				elementnumber = -1;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("");
		System.out.println("eof");
		System.out.println("");
		return false;
	}
 }

