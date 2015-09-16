package com.stream.stumanager.model;

public class Student implements Comparable<Student>{

	private String stu_id;
	private String stu_name;
	private String stu_sex;
	private String stu_birth;
	private String class_id;
	public String getStu_id() {
		return stu_id;
	}
	public Student(){
		
	}
	
	public Student(String stu_id, String stu_name, String stu_sex,
			String stu_birth, String class_id) {
		super();
		this.stu_id = stu_id;
		this.stu_name = stu_name;
		this.stu_sex = stu_sex;
		this.stu_birth = stu_birth;
		this.class_id = class_id;
	}
	public void setStu_id(String stu_id) {
		this.stu_id = stu_id;
	}
	public String getStu_name() {
		return stu_name;
	}
	public void setStu_name(String stu_name) {
		this.stu_name = stu_name;
	}
	public String getStu_sex() {
		return stu_sex;
	}
	public void setStu_sex(String stu_sex) {
		this.stu_sex = stu_sex;
	}
	public String getStu_birth() {
		return stu_birth;
	}
	public void setStu_birth(String stu_birth) {
		this.stu_birth = stu_birth;
	}
	public String getClass_id() {
		return class_id;
	}
	public void setClass_id(String class_id) {
		this.class_id = class_id;
	}
	@Override
	public int compareTo(Student another) {
		// TODO Auto-generated method stub
		return this.getStu_id().compareTo(another.getStu_id());
	}
}
