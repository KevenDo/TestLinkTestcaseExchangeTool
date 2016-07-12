package com.testlink.vo;

public class InputVo 
{
	private String firstpath;
	
	private String secondpath;
	
	private String thirdpath;
	
	private String name;
	
	private String externalid;
	
	private String value;
	
	private String preconditions;
	
	private String actions;
	
	private String expectedresults;

	public String getFirstpath() {
		return firstpath;
	}

	public void setFirstpath(String firstpath) {
		this.firstpath = firstpath;
	}

	public String getSecondpath() {
		return secondpath;
	}

	public void setSecondpath(String secondpath) {
		this.secondpath = secondpath;
	}

	public String getThirdpath() {
		return thirdpath;
	}

	public void setThirdpath(String thirdpath) {
		this.thirdpath = thirdpath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExternalid() {
		return externalid;
	}

	public void setExternalid(String externalid) {
		this.externalid = externalid;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getPreconditions() {
		return preconditions;
	}

	public void setPreconditions(String preconditions) {
		this.preconditions = preconditions;
	}

	public String getActions() {
		return actions;
	}

	public void setActions(String actions) {
		this.actions = actions;
	}

	public String getExpectedresults() {
		return expectedresults;
	}

	public void setExpectedresults(String expectedresults) {
		this.expectedresults = expectedresults;
	}
	
	public String toString()
	{
		String result = "{testcase: firstpath=" +this.getFirstpath()+
				",secondpath="+this.getSecondpath()+
				",thirdpath="+this.getThirdpath()+
				",name="+this.getName()+
				",externalid="+this.getExternalid()+
				",value="+this.getValue()+
				",preconditions="+this.getPreconditions()+
				",actions="+this.getActions()+
				",expectedresults="+this.getExpectedresults()+"}";
		
		return result;
	}
}
