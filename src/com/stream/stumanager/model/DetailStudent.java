package com.stream.stumanager.model;

import java.io.Serializable;

public class DetailStudent implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String class_id;
	private String class_name;
	private String major_id;
	private String major_name;
	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	private String name;
	private String sex;
	private String birth;
	
	
	
	protected DetailStudent() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DetailStudent(String class_id, String class_name,
			String major_id, String major_name, String id,String name, String sex,
			String birth) {
		super();
		this.class_id = class_id;
		this.class_name = class_name;
		this.major_id = major_id;
		this.major_name = major_name;
		this.id = id;
		this.name = name;
		this.sex = sex;
		this.birth = birth;
	}
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
	public String getMajor_id() {
		return major_id;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
}
