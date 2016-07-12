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
		JMenu helpMenu = new JMenu("ѡ��");
		menuBar.add(helpMenu);
		
		JMenuItem helpItem = new JMenuItem("����");
		helpItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            	HelpDialog dialog = new HelpDialog(UserInterface.this);
            	dialog.setVisible(true);
            }
        });
		
		JMenuItem aboutItem = new JMenuItem("����");
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
		
		final JTextField textField = new JTextField("�뵼��Excel2003�ļ�����.xls��׺����β",50);
		textField.setEditable(false);
		buttonPanel.add(textField);
		
		final JTextField excuteresult = new JTextField("ת�������",50);
		excuteresult.setEditable(false);
		buttonPanel.add(excuteresult);
		
		JButton importbutton = new JButton("�����ļ�");
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
        
        JButton excutebutton = new JButton("ִ��ת��");
		buttonPanel.add(excutebutton);
		excutebutton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            	String sourcefilepath = textField.getText();
            	if(!sourcefilepath.equals("�뵼��Excel2003�ļ�����.xls��׺����β"))
            	{
            		CommonTools ct = new CommonTools();
                	try 
                	{
    					ct.exchangeXml(sourcefilepath, "testcase.xml");
    					excuteresult.setText("ת��������ɹ���");
    				} 
                	catch (BiffException e1) 
    				{
                		excuteresult.setText("ת�������ʧ�ܣ��뵼��Excel2003�ļ���");
    				} 
                	catch (DocumentException e1) 
    				{
                		excuteresult.setText("ת�������ʧ�ܣ�����excel��ʽ��");
    				} 
                	catch (IOException e1) 
    				{
                		excuteresult.setText("ת�������ʧ�ܣ�����excel��ʽ��");
    				}
            	}
            }
        });
	}
	
	class AboutDialog extends JDialog
	{
		public AboutDialog(JFrame owner)
		{
			super(owner,"��������",true);
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
			super(owner,"����",true);
			add(
					new JLabel("<html>" +"<p>ʹ�ò��裺</p>"+
							"<p>1����װjdk1.6���ϵİ汾�����򹤾��޷�ʹ�ã�</p>"+
							"<p>2������excel2003�ļ�����֧�ָ��ߵİ汾��</p>"+
							"<p>3�����ִ��ת����������ں�jarͬ��Ŀ¼������testcase.xml�����ļ�����ֱ�ӵ���TestLink��</p><br/>"+
							"<p>ע�����</p>"+
							"<p>1������ģ��Excel��ʽ��һ��9���ֶΣ��ֱ�Ϊ��</p>"+
							"<p>һ��Ŀ¼������Ŀ¼������Ŀ¼���������ơ�������š���������Ԥ���������������衢Ԥ�ڽ����</p>"+
							"<p>2�����������ֶ���ҪTestLink�������ؼ���testcase_level��</p>"+
							"<p>3������ģ��ʱ��sheetҳ��һ����д�����ֶ����ƣ��ڶ���Ϊ��ʽ������ʼ��</p>"+
							"<p>4���뱣֤excel�ļ���û�п��У�</p>"+
							"<p>5���뱣֤ÿ������û�п��ֶΡ�</p>"+
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
