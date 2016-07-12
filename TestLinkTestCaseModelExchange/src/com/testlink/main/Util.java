package com.testlink.main;

import java.util.Date;
import java.util.Scanner;

public class Util 
{
	public String createId(int i)
	{
		Date date = new Date();
		String currentTime = String.valueOf(date.getTime())+"1";
		return currentTime;
	}
	
	public String addHtmlElement(String value)
	{
		String result = "";
		Scanner scanner = new Scanner(value); 
		while (scanner.hasNext())
		{
			String line = scanner.nextLine();
			String linewithelement = "<p>"+line+"</p>";
			result = result + linewithelement;
		}
		return result;
	}
	
	public String addCDATA(String para)
	{
		String result = "<![CDATA["+para+"]]>";		
		return result;
	}
}
