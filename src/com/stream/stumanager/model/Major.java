package com.stream.stumanager.model;

public class Major implements Comparable<Major>{
	private String major_id;
	private String major_name;
	public String getMajor_id() {
		return major_id;
	}
	public Major(String major_id, String major_name) {
		super();
		this.major_id = major_id;
		this.major_name = major_name;
	}
	public Major() {
		super();
		// TODO Auto-generated constructor stub
	}
	public void setMajor_id(String major_id) {
		this.major_id = major_id;
	}
	public String getMajor_name() {
		return major_name;
	}
	public void setMajor_name(String major_name) {
		this.major_name = major_name;
	}
	@Override
	public int compareTo(Major another) {
		// TODO Auto-generated method stub
		return this.getMajor_id().compareTo(another.getMajor_id());
	}
}
