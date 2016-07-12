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
		//解析源文件，获取源文件中所有testcase的取值，并存放在arraylist中
		CommonTools ct = new CommonTools();
		ArrayList<InputVo> iv = ct.analysisExcel(filepath);
		Document doc = DocumentHelper.createDocument();
		Util ut = new Util();
		//设置根节点
		Element firstpath = doc.addElement("testsuite");
		firstpath.addAttribute("name",iv.get(0).getFirstpath());
		Element node_order = firstpath.addElement("node_order");
		node_order.setText(ut.createId(0));
		
		//用arraylist来存放所有不重复的二级目录
		ArrayList<Element> secondpathlist = new ArrayList<Element>();
		ArrayList<Element> thirdpathlist = new ArrayList<Element>();
		ArrayList<String> secondpathlistvalue = new ArrayList<String>();
		ArrayList<String> thirdpathlistvalue = new ArrayList<String>();
		
		//ArrayList<InputVo> iv的index
		int ivINDEX = 0;
		//ArrayList<String> secondpathlist的Index
		int secondpathlistINDEX = 0;
		//ArrayList<String> thirdpathlist 的index
		int thirdpathlistINDEX = 0;
		/**
		 * 循环处理所有的testcase，详细设计如下：
		 * 1、所有testcase的一级目录名称是相同的，取第一个vo的Firstpath即可；
		 * 2、二级目录的处理较复杂，最终目的是把iv中所有不重复的目录取出来作为XML的节点，一共有7个参数（见上方），具体处理逻辑如下：
		 *    A. iv.index>1后的循环，如果inputvo.getSecondpath等于secondpathlistvalue<j=0>，证明是同一个二级目录，无须新建新的二级xml节点，
		 *       如果inputvo.getSecondpath不等于secondpathlist<j=0>，证明有新的二级目录，则新建新的二级xml节点，
		 *       把inputvo.getSecondpath赋值给secondpathlistvalue<j++>，后面的inputvo.getSecondpath则与secondpathlistvalue<j++>做对比，
		 *       并且把当前的element保存到secondpathlist中，后面三级目录可以通过secondpathlist获取到上一级节点的准确位置
		 *    注：由于二级目录在同属于一个一级目录，真实情况下一个特性不允许出现两个名称相同的子特性，而且所有二级目录相同的用例都是连续的，不存在
		 *        secondpathlist<j++>与secondpathlist<i(i<j)>相同的情况，三级目录也是
		 * 3、三级目录的处理同二级目录；
		 * 4、testcase的其他字段分装成一个单独方法insetSourceValueIntoTarget，每次循环调用一次即可。
		 */
		InputVo vifirst = iv.get(0);
		//secondpath赋值
		Element secondpathfirst = firstpath.addElement("testsuite");
		secondpathfirst.addAttribute("name", vifirst.getSecondpath());
		Element node_order_2_first = secondpathfirst.addElement("node_order");
		node_order_2_first.setText("10"+String.valueOf(secondpathlistINDEX+1));
		//secondpathlist第一个元素
		secondpathlist.add(secondpathfirst);
		secondpathlistvalue.add(vifirst.getSecondpath());
		
		//thirdpath赋值
		Element thirdpath = secondpathlist.get(secondpathlistINDEX).addElement("testsuite");
		thirdpath.addAttribute("name", vifirst.getThirdpath());
		ct.insetSourceValueIntoTarget(vifirst, thirdpath,ivINDEX);
		//thirdpathlist第一个元素
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
					//thirdpath赋值
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
		//输出为文件
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
			//获取每个testcase的所有字段取值并赋值给InputVo，然后放入List中存放
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
		//定义文件输出的整体xml结构
		Element testcase = el.addElement("testcase");
		//testcase xml结构
		Element node_order = testcase.addElement("node_order");
		Element externalid = testcase.addElement("externalid");
		Element version = testcase.addElement("version");
		Element summary = testcase.addElement("summary");
		Element preconditions = testcase.addElement("preconditions");
		Element execution_type = testcase.addElement("execution_type");
		Element importance = testcase.addElement("importance");
		Element steps = testcase.addElement("steps");
		Element custom_fields = testcase.addElement("custom_fields");
		//step xml结构
		Element step = steps.addElement("step");
		Element step_number = step.addElement("step_number");
		Element actions = step.addElement("actions");
		Element expectedresults = step.addElement("expectedresults");
		Element execution_type2 = step.addElement("execution_type");
		//custom_field xml结构
		Element custom_field = custom_fields.addElement("custom_field");
		Element name = custom_field.addElement("name");
		Element value = custom_field.addElement("value");
		
		//生成一个全局唯一的id，供所有要用到Id的字段
		Util ul = new Util();
		String commonId = ul.createId(testcaseid);
		
		//将inputvo中的值取出来赋给要输出xml节点的值
		testcase.addAttribute("id", commonId);
		testcase.addAttribute("name", inputvo.getName());
		node_order.setText("");
		externalid.setText(inputvo.getExternalid());
		version.setText(ul.addCDATA(commonId));
		summary.setText(ul.addCDATA(inputvo.getName()));
		preconditions.setText(ul.addHtmlElement(inputvo.getPreconditions()));
		execution_type.setText(ul.addCDATA("1"));
		importance.setText(ul.addCDATA("2"));
		//step结构体赋值
		step_number.setText("1");
		actions.setText(ul.addHtmlElement(inputvo.getActions()));
		expectedresults.setText(ul.addHtmlElement(inputvo.getExpectedresults()));
		execution_type2.setText(ul.addCDATA("1"));
		//custom_field结构体赋值
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
