package com.testlink.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.testlink.vo.InputVo;

public class CommonTools 
{
	public void exchangeXml(String filepath,String savepath) throws DocumentException, IOException, BiffException
	{
		//����Դ�ļ�����ȡԴ�ļ�������testcase��ȡֵ���������arraylist��
		CommonTools ct = new CommonTools();
		ArrayList<InputVo> iv = ct.analysisExcel(filepath);
		Document doc = DocumentHelper.createDocument();
		Util ut = new Util();
		//���ø��ڵ�
		Element firstpath = doc.addElement("testsuite");
		firstpath.addAttribute("name",iv.get(0).getFirstpath());
		Element node_order = firstpath.addElement("node_order");
		node_order.setText(ut.createId(0));
		
		//��arraylist��������в��ظ��Ķ���Ŀ¼
		ArrayList<Element> secondpathlist = new ArrayList<Element>();
		ArrayList<Element> thirdpathlist = new ArrayList<Element>();
		ArrayList<String> secondpathlistvalue = new ArrayList<String>();
		ArrayList<String> thirdpathlistvalue = new ArrayList<String>();
		
		//ArrayList<InputVo> iv��index
		int ivINDEX = 0;
		//ArrayList<String> secondpathlist��Index
		int secondpathlistINDEX = 0;
		//ArrayList<String> thirdpathlist ��index
		int thirdpathlistINDEX = 0;
		/**
		 * ѭ���������е�testcase����ϸ������£�
		 * 1������testcase��һ��Ŀ¼��������ͬ�ģ�ȡ��һ��vo��Firstpath���ɣ�
		 * 2������Ŀ¼�Ĵ���ϸ��ӣ�����Ŀ���ǰ�iv�����в��ظ���Ŀ¼ȡ������ΪXML�Ľڵ㣬һ����7�����������Ϸ��������崦���߼����£�
		 *    A. iv.index>1���ѭ�������inputvo.getSecondpath����secondpathlistvalue<j=0>��֤����ͬһ������Ŀ¼�������½��µĶ���xml�ڵ㣬
		 *       ���inputvo.getSecondpath������secondpathlist<j=0>��֤�����µĶ���Ŀ¼�����½��µĶ���xml�ڵ㣬
		 *       ��inputvo.getSecondpath��ֵ��secondpathlistvalue<j++>�������inputvo.getSecondpath����secondpathlistvalue<j++>���Աȣ�
		 *       ���Ұѵ�ǰ��element���浽secondpathlist�У���������Ŀ¼����ͨ��secondpathlist��ȡ����һ���ڵ��׼ȷλ��
		 *    ע�����ڶ���Ŀ¼��ͬ����һ��һ��Ŀ¼����ʵ�����һ�����Բ������������������ͬ�������ԣ��������ж���Ŀ¼��ͬ���������������ģ�������
		 *        secondpathlist<j++>��secondpathlist<i(i<j)>��ͬ�����������Ŀ¼Ҳ��
		 * 3������Ŀ¼�Ĵ���ͬ����Ŀ¼��
		 * 4��testcase�������ֶη�װ��һ����������insetSourceValueIntoTarget��ÿ��ѭ������һ�μ��ɡ�
		 */
		InputVo vifirst = iv.get(0);
		//secondpath��ֵ
		Element secondpathfirst = firstpath.addElement("testsuite");
		secondpathfirst.addAttribute("name", vifirst.getSecondpath());
		Element node_order_2_first = secondpathfirst.addElement("node_order");
		node_order_2_first.setText("10"+String.valueOf(secondpathlistINDEX+1));
		//secondpathlist��һ��Ԫ��
		secondpathlist.add(secondpathfirst);
		secondpathlistvalue.add(vifirst.getSecondpath());
		
		//thirdpath��ֵ
		Element thirdpath = secondpathlist.get(secondpathlistINDEX).addElement("testsuite");
		thirdpath.addAttribute("name", vifirst.getThirdpath());
		ct.insetSourceValueIntoTarget(vifirst, thirdpath,ivINDEX);
		//thirdpathlist��һ��Ԫ��
		thirdpathlist.add(thirdpath);
		thirdpathlistvalue.add(vifirst.getThirdpath());
		
		for (InputVo inputvo : iv)
		{
			if (ivINDEX == 0)
			{
				//do nothing
			}
			else if (ivINDEX != 0 && inputvo.getSecondpath().equals(secondpathlistvalue.get(secondpathlistINDEX)))
			{
				if(inputvo.getThirdpath().equals(thirdpathlistvalue.get(thirdpathlistINDEX)))
				{
					ct.insetSourceValueIntoTarget(inputvo, thirdpathlist.get(thirdpathlistINDEX),ivINDEX);
				}
				else
				{
					//thirdpath��ֵ
					Element thirdpathnext = secondpathlist.get(secondpathlistINDEX).addElement("testsuite");
					thirdpathnext.addAttribute("name", inputvo.getThirdpath());
					thirdpathlist.add(thirdpathnext);
					thirdpathlistvalue.add(inputvo.getThirdpath());
					thirdpathlistINDEX++;
					ct.insetSourceValueIntoTarget(inputvo, thirdpathlist.get(thirdpathlistINDEX),ivINDEX);
				}
			}
			else
			{
				Element secondpath = firstpath.addElement("testsuite");
				secondpath.addAttribute("name", inputvo.getSecondpath());
				Element node_order_2 = secondpath.addElement("node_order");
				node_order_2.setText("10"+String.valueOf(secondpathlistINDEX+2));
				secondpathlistvalue.add(inputvo.getSecondpath());
				secondpathlist.add(secondpath);
				secondpathlistINDEX++;
				
				Element thirdpathnext = secondpathlist.get(secondpathlistINDEX).addElement("testsuite");
				thirdpathnext.addAttribute("name", inputvo.getThirdpath());
				thirdpathlist.add(thirdpathnext);
				thirdpathlistvalue.add(inputvo.getThirdpath());
				thirdpathlistINDEX++;
				ct.insetSourceValueIntoTarget(inputvo, thirdpathlist.get(thirdpathlistINDEX),ivINDEX);
			}
			ivINDEX++;
		}
		//���Ϊ�ļ�
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		File file = new File(savepath);
		XMLWriter writer = new XMLWriter(new FileOutputStream(file),format);
		writer.write(doc);
	}
	
	public ArrayList<InputVo> analysisXml(String filepath) throws DocumentException
	{
		ArrayList<InputVo> iv = new ArrayList<InputVo>();
		SAXReader reader = new SAXReader();
		Document doc = reader.read(new File(filepath));
		Element root = doc.getRootElement();
		Iterator it = root.elementIterator();
		
		while (it.hasNext())
		{
			//��ȡÿ��testcase�������ֶ�ȡֵ����ֵ��InputVo��Ȼ�����List�д��
			Element element = (Element) it.next();
			InputVo inputvo = new InputVo();
			inputvo.setFirstpath(element.elementText("firstpath"));
			inputvo.setSecondpath(element.elementText("secondpath"));
			inputvo.setThirdpath(element.elementText("thirdpath"));
			inputvo.setName(element.elementText("name"));
			inputvo.setExternalid(element.elementText("externalid"));
			inputvo.setValue(element.elementText("value"));
			inputvo.setPreconditions(element.elementText("preconditions"));
			inputvo.setActions(element.elementText("actions"));
			inputvo.setExpectedresults(element.elementText("expectedresults"));
			iv.add(inputvo);
		}
		return iv;
	}
	
	public void insetSourceValueIntoTarget(InputVo inputvo,Element el,int testcaseid)
	{
		//�����ļ����������xml�ṹ
		Element testcase = el.addElement("testcase");
		//testcase xml�ṹ
		Element node_order = testcase.addElement("node_order");
		Element externalid = testcase.addElement("externalid");
		Element version = testcase.addElement("version");
		Element summary = testcase.addElement("summary");
		Element preconditions = testcase.addElement("preconditions");
		Element execution_type = testcase.addElement("execution_type");
		Element importance = testcase.addElement("importance");
		Element steps = testcase.addElement("steps");
		Element custom_fields = testcase.addElement("custom_fields");
		//step xml�ṹ
		Element step = steps.addElement("step");
		Element step_number = step.addElement("step_number");
		Element actions = step.addElement("actions");
		Element expectedresults = step.addElement("expectedresults");
		Element execution_type2 = step.addElement("execution_type");
		//custom_field xml�ṹ
		Element custom_field = custom_fields.addElement("custom_field");
		Element name = custom_field.addElement("name");
		Element value = custom_field.addElement("value");
		
		//����һ��ȫ��Ψһ��id��������Ҫ�õ�Id���ֶ�
		Util ul = new Util();
		String commonId = ul.createId(testcaseid);
		
		//��inputvo�е�ֵȡ��������Ҫ���xml�ڵ��ֵ
		testcase.addAttribute("id", commonId);
		testcase.addAttribute("name", inputvo.getName());
		node_order.setText("");
		externalid.setText(inputvo.getExternalid());
		version.setText(ul.addCDATA(commonId));
		summary.setText(ul.addCDATA(inputvo.getName()));
		preconditions.setText(ul.addHtmlElement(inputvo.getPreconditions()));
		execution_type.setText(ul.addCDATA("1"));
		importance.setText(ul.addCDATA("2"));
		//step�ṹ�帳ֵ
		step_number.setText("1");
		actions.setText(ul.addHtmlElement(inputvo.getActions()));
		expectedresults.setText(ul.addHtmlElement(inputvo.getExpectedresults()));
		execution_type2.setText(ul.addCDATA("1"));
		//custom_field�ṹ�帳ֵ
		name.setText("testcase_level");
		value.setText(inputvo.getValue());
	}
	
	public ArrayList<InputVo> analysisExcel(String filepath) throws BiffException, IOException
	{
		ArrayList<InputVo> iv = new ArrayList<InputVo>();
		Workbook workbook = Workbook.getWorkbook(new File(filepath));
		Sheet sheet = workbook.getSheet(0);
		for (int role = 1;role<sheet.getRows();role++)
		{
			Cell firstpath = sheet.getCell(0, role);
			if (firstpath.getContents()!=null&&!firstpath.getContents().equals(""))
			{
				Cell secondpath = sheet.getCell(1, role);
				Cell thirdpath = sheet.getCell(2, role);
				Cell name = sheet.getCell(3, role);
				Cell externalid = sheet.getCell(4, role);
				Cell value = sheet.getCell(5, role);
				Cell preconditions = sheet.getCell(6, role);
				Cell actions = sheet.getCell(7, role);
				Cell expectedresults = sheet.getCell(8, role);
				
				InputVo inputvo = new InputVo();
				inputvo.setFirstpath(firstpath.getContents());
				inputvo.setSecondpath(secondpath.getContents());
				inputvo.setThirdpath(thirdpath.getContents());
				inputvo.setName(name.getContents());
				inputvo.setExternalid(externalid.getContents());
				inputvo.setValue(value.getContents());
				inputvo.setPreconditions(preconditions.getContents());
				inputvo.setActions(actions.getContents());
				inputvo.setExpectedresults(expectedresults.getContents());
				iv.add(inputvo);
			}
		}
		return iv;
	}
}
