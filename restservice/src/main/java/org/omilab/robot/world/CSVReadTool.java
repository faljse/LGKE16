package org.omilab.robot.world;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CSVReadTool implements AutoCloseable{
	private BufferedReader br;
	private String[] line;
	private int elementnumber;

	CSVReadTool(String csvFile) throws IOException {
		br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/"+csvFile)));
		nextLine();
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
		final String csvSplitBy = ";";
		String wholeLine;

		if ((wholeLine = br.readLine()) != null) {
			line = wholeLine.split(csvSplitBy);
			elementnumber = 0;
			return true;
		}
		else
			line = null;
			elementnumber = -1;
		return false;
	}

	@Override
	public void close() throws Exception {
		try{
			br.close();
		} catch (IOException e){}
	}
}

