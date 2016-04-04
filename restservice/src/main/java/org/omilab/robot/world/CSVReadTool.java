package org.omilab.robot.world;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CSVReadTool {
	private BufferedReader br;
	private String[] Line;
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
		if (Line != null && elementnumber >= Line.length) 
			nextLine();
		
		if (Line != null && elementnumber < Line.length) {
			return Line[elementnumber++];
		}
		
		return null;
	}
	
	public boolean nextLine() {
		String cvsSplitBy = ";";
		String WholeLine;
		try {
			
			if ((WholeLine = br.readLine()) != null) {
				Line = WholeLine.split(cvsSplitBy);
				elementnumber = 0;
				return true;
			}
			else
				Line = null;
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

