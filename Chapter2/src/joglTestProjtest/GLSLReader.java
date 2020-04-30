package joglTestProjtest;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

public class GLSLReader 
{
	private int linenum = 0;
	
	public String[] readShaderSource(String filename)
	{
		linenum = 0;
		Vector<String> lines = new Vector<String>();
		Scanner sc;
		
		try
		{ 
			sc = new Scanner(new File(filename)); 
		}
		catch (IOException e)
		{ 
			System.err.println("IOException reading file: " + e);
			return null;
		}
		
		while (sc.hasNext())
		{ 
			linenum++;
			lines.addElement(sc.nextLine());
		}
		String[ ] program = new String[lines.size()];
		
		for (int i = 0; i < lines.size(); i++)
		{ 
			program[i] = (String) lines.elementAt(i) + "\n";
		}
		return program;
	}

	public int getLines() {
		return linenum;
	}

}
