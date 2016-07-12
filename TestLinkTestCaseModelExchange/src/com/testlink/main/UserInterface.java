package com.testlink.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import jxl.read.biff.BiffException;

import org.dom4j.DocumentException;

public class UserInterface extends JFrame 
{
	public UserInterface() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
	{
		//set gui style
		UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		SwingUtilities.updateComponentTreeUI(UserInterface.this);
		
		//get screen dimensions
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;
		
		//set frame width,height and let platform pick screen location
		setSize(400,200);
		setLocation(screenWidth*3/8,screenHeight*3/8);
		setResizable(false);
		//set tool title
		setTitle("TestLink TestCase Exchange Tool");
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu helpMenu = new JMenu("选项");
		menuBar.add(helpMenu);
		
		JMenuItem helpItem = new JMenuItem("帮助");
		helpItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            	HelpDialog dialog = new HelpDialog(UserInterface.this);
            	dialog.setVisible(true);
            }
        });
		
		JMenuItem aboutItem = new JMenuItem("关于");
		aboutItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            	AboutDialog dialog = new AboutDialog(UserInterface.this);
            	dialog.setVisible(true);
            }
        });
		
		helpMenu.add(helpItem);
		helpMenu.add(aboutItem);
		
		final JPanel buttonPanel = new JPanel();
		add(buttonPanel);
		
		final JTextField textField = new JTextField("请导入Excel2003文件，以.xls后缀名结尾",50);
		textField.setEditable(false);
		buttonPanel.add(textField);
		
		final JTextField excuteresult = new JTextField("转换结果：",50);
		excuteresult.setEditable(false);
		buttonPanel.add(excuteresult);
		
		JButton importbutton = new JButton("导入文件");
		buttonPanel.add(importbutton);
        importbutton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            	JFileChooser file = new JFileChooser();
                Component frame = null;
    			file.showOpenDialog(frame);
    			File filechoose = file.getSelectedFile();
    			if (filechoose!=null)
    			{
    				textField.setText(filechoose.getPath());
    			}
            }
        });
        
        JButton excutebutton = new JButton("执行转换");
		buttonPanel.add(excutebutton);
		excutebutton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            	String sourcefilepath = textField.getText();
            	if(!sourcefilepath.equals("请导入Excel2003文件，以.xls后缀名结尾"))
            	{
            		CommonTools ct = new CommonTools();
                	try 
                	{
    					ct.exchangeXml(sourcefilepath, "testcase.xml");
    					excuteresult.setText("转换结果：成功！");
    				} 
                	catch (BiffException e1) 
    				{
                		excuteresult.setText("转换结果：失败！请导入Excel2003文件！");
    				} 
                	catch (DocumentException e1) 
    				{
                		excuteresult.setText("转换结果：失败！请检查excel格式！");
    				} 
                	catch (IOException e1) 
    				{
                		excuteresult.setText("转换结果：失败！请检查excel格式！");
    				}
            	}
            }
        });
	}
	
	class AboutDialog extends JDialog
	{
		public AboutDialog(JFrame owner)
		{
			super(owner,"关于作者",true);
			add(
					new JLabel("<html>" +"<p>Write By Keven Du</p><br/>"+"<p>Email:dldsryx@126.com</p>"+
							"</html>"),
					BorderLayout.CENTER);
			
			Toolkit kit = Toolkit.getDefaultToolkit();
			Dimension screenSize = kit.getScreenSize();
			int screenHeight = screenSize.height;
			int screenWidth = screenSize.width;
			
			setSize(150,100);
			setLocation(screenWidth*7/16,screenHeight*7/16);
			setResizable(false);
		}
	}
	
	class HelpDialog extends JDialog
	{
		public HelpDialog(JFrame owner)
		{
			super(owner,"帮助",true);
			add(
					new JLabel("<html>" +"<p>使用步骤：</p>"+
							"<p>1、安装jdk1.6以上的版本，否则工具无法使用；</p>"+
							"<p>2、导入excel2003文件，不支持更高的版本；</p>"+
							"<p>3、点击执行转换，程序会在和jar同级目录下生成testcase.xml，该文件即可直接导入TestLink。</p><br/>"+
							"<p>注意事项：</p>"+
							"<p>1、用例模板Excel格式，一共9个字段，分别为：</p>"+
							"<p>一级目录、二级目录、三级目录、用例名称、用例编号、用例级别、预置条件、操作步骤、预期结果。</p>"+
							"<p>2、用例级别字段需要TestLink中新增关键字testcase_level；</p>"+
							"<p>3、构造模板时，sheet页第一行填写上述字段名称，第二行为正式用例开始。</p>"+
							"<p>4、请保证excel文件中没有空行；</p>"+
							"<p>5、请保证每条用例没有空字段。</p>"+
							"</html>"),
					BorderLayout.CENTER);
			
			Toolkit kit = Toolkit.getDefaultToolkit();
			Dimension screenSize = kit.getScreenSize();
			int screenHeight = screenSize.height;
			int screenWidth = screenSize.width;
			
			setSize(screenWidth*1/4,screenHeight*5/16);
			setLocation(screenWidth*3/8,screenHeight*3/8);
			setResizable(false);
		}
	}
}
