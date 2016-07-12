package com.testlink.main;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.UnsupportedLookAndFeelException;

import jxl.read.biff.BiffException;

import org.dom4j.DocumentException;

public class Test 
{
	public static void main(String args[]) throws DocumentException, IOException, BiffException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
	{
//		String inputPath = "e:\\test.xls";
//		String outputPath = "e:\\testcase.xml";
//		CommonTools ct = new CommonTools();
//		ct.exchangeXml(inputPath, outputPath);
//		System.out.println(ct.analysisExcel("e:\\test.xls"));
//		System.out.println(ct.analysisXml("e:\\export_test02.xml"));
		UserInterface ui = new UserInterface();
		ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ui.setVisible(true);
	}
}
