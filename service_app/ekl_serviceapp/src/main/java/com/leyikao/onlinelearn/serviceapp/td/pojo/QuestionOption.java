package com.leyikao.onlinelearn.serviceapp.td.pojo;


public class QuestionOption implements Comparable<QuestionOption>{
	private String name;
	private String description;

	public QuestionOption() {
		super();
	}

	public QuestionOption(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public int compareTo(QuestionOption o) {
		return this.name.compareTo(o.name);
	}

}
