package com.stream.stumanager.model;

public class Classes implements Comparable<Classes>{
	public Classes() {
		super();
		// TODO Auto-generated constructor stub
	}
	private String class_id;
	private String class_name;
	private String class_date;
	public Classes(String class_id, String class_name, String class_date, String major_id) {
		super();
		this.class_id = class_id;
		this.class_name = class_name;
		this.class_date = class_date;
		this.major_id = major_id;
	}
	private String major_id;
	public String getClass_id() {
		return class_id;
	}
	public void setClass_id(String class_id) {
		this.class_id = class_id;
	}
	public String getClass_name() {
		return class_name;
	}
	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}
	public String getClass_date() {
		return class_date;
	}
	public void setClass_date(String class_date) {
		this.class_date = class_date;
	}
	public String getMajor_id() {
		return major_id;
	}
	public void setMajor_id(String major_id) {
		this.major_id = major_id;
	}
	@Override
	public int compareTo(Classes another) {
		// TODO Auto-generated method stub
		return this.getClass_id().compareTo(another.getClass_id());
	}
}
