package com.stream.stumanager.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.stream.stumanager.model.Classes;
import com.stream.stumanager.model.DetailStudent;
import com.stream.stumanager.model.Major;
import com.stream.stumanager.model.Student;

public class StuManagerDB {
	/**
	 * ���ݿ����
	 */
	private final String DB_NAME = "stu_manager";
	private final int VERSION = 1;
	private static StuManagerDB stuManagerDB;
	private SQLiteDatabase db;
	
	/**
	 * ˽�л�����
	 */
	private StuManagerDB(Context context){
		StuDBOpenHelper sdbHelper = new StuDBOpenHelper(context, DB_NAME, null, VERSION);
		db = sdbHelper.getWritableDatabase();
	}
	
	/**
	 * 
	 * @param context
	 * @return StuManagerDB��ʵ��
	 */
	public synchronized static StuManagerDB getInstance(Context context){
		if(stuManagerDB==null){
			stuManagerDB = new StuManagerDB(context);
		}
		return stuManagerDB;
	}
	
	/**
	 * 
	 * @param major
	 * ����major�����ݿ�
	 */
	public boolean saveMajor(Major major){
		if(major!=null){
			ContentValues values = new ContentValues();
			values.put("major_id", major.getMajor_id());
			values.put("major_name", major.getMajor_name());
			long l = db.insert("major", null, values);
			if(l==-1){
				return false;
			}else{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * ����classes�����ݿ�
	 * @param classes
	 */
	public boolean saveClass(Classes classes){
		if(classes!=null){
			ContentValues values = new ContentValues();
			values.put("class_id", classes.getClass_id());
			values.put("class_name", classes.getClass_name());
			values.put("class_date", classes.getClass_date());
			values.put("major_id", classes.getMajor_id());
			long l = db.insert("class", null, values);
			if(l==-1){
				return false;
			}else{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * ����student�����ݿ�
	 * @param student
	 */
	public boolean saveStu(Student student){
		if(student!=null){
			ContentValues values = new ContentValues();
			values.put("student_id", student.getStu_id());
			values.put("student_name", student.getStu_name());
			values.put("student_sex", student.getStu_sex());
			values.put("student_birth", student.getStu_birth());
			values.put("class_id", student.getClass_id());
			long l = db.insert("student", null, values);
			if(l==-1){
				return false;
			}else{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @return ���ش�������Major��List
	 */
	public List<Major> getMajor(){
		List<Major> list = new ArrayList<Major>();
		Cursor cursor = db.query("major", null, null, null, null, null, null);
		while(cursor.moveToNext()){
			Major major = new Major();
			major.setMajor_id(cursor.getString(cursor.getColumnIndex("major_id")));
			major.setMajor_name(cursor.getString(cursor.getColumnIndex("major_name")));
			list.add(major);
		}
		if(cursor!=null){
			cursor.close();
		}
		return list;
	}
	
	/**
	 * ���ض�Ӧרҵ�����а༶
	 * @param major_name
	 * @return
	 */
	public List<Classes> getClasses(String major_id){
		List<Classes> list = new ArrayList<Classes>();
		Cursor cursor = db.query("class", null, "major_id=?", new String[]{major_id}, null
				, null, null);
		while(cursor.moveToNext()){
			Classes classes = new Classes();
			classes.setClass_id(cursor.getString(cursor.getColumnIndex("class_id")));
			classes.setClass_date(cursor.getString(cursor.getColumnIndex("class_date")));
			classes.setClass_name(cursor.getString(cursor.getColumnIndex("class_name")));
			list.add(classes);
		}
		if(cursor!=null){
			cursor.close();
		}
		return list;
	}
	
	/**
	 * ���ض�Ӧ�༶������ѧ��
	 * @param class_id
	 * @return
	 */
	public List<Student> getStudents(String class_id){
		List<Student> list = new ArrayList<Student>();
		Cursor cursor = db.query("student", null, "class_id=?", new String[]{class_id},
				null, null, null);
		while(cursor.moveToNext()){
			Student student = new Student();
			student.setStu_id(cursor.getString(cursor.getColumnIndex("student_id")));
			student.setStu_birth(cursor.getString(cursor.getColumnIndex("student_birth")));
			student.setStu_name(cursor.getString(cursor.getColumnIndex("student_name")));
			student.setStu_sex(cursor.getString(cursor.getColumnIndex("student_sex")));
			student.setClass_id(class_id);
			list.add(student);
		}
		if(cursor!=null){
			cursor.close();
		}
		return list;
	}
	
	/**
	 * ���һ��ѧ������ϸ��Ϣ
	 */
	public DetailStudent getDetailStudent(String id){
		DetailStudent ds=null ;
		String sql = "select m.major_id, major_name, c.class_id, class_name, student_id, student_name, student_sex, student_birth from" +
				" student s join class c on (s.class_id=c.class_id)" +
				" join major m on (c.major_id=m.major_id)";
		Cursor cursor = db.rawQuery(sql +"where student_id=?", new String[]{id});
		while(cursor.moveToNext()){
			ds = new DetailStudent(cursor.getString(0),cursor.getString(1),
					cursor.getString(2),cursor.getString(3),cursor.getString(4),
					cursor.getString(5),cursor.getString(6),cursor.getString(7));
		}
		return ds;
	}

	
	/**
	 * ����¼�Ƿ�ɹ�
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean checkLogin(String username, String password){
		Cursor cursor = db.query("user", null, "username=? and password=?", 
				new String[]{username, password}, null, null, null);
		if(cursor.moveToNext()){
			if(cursor!=null){
				cursor.close();
			}
			return true;
		}

		return false;
	}
	
	/**
	 * �������ּ���Ƿ���ڸ��û�
	 */
	public boolean checkByName(String username){
		Cursor cursor = db.query("user", null, "username=?", 
				new String[]{username}, null, null, null);
		if(cursor.moveToNext()){
			if(cursor!=null){
				cursor.close();
			}
			return true;
		}
		return false;
	}
	
	/**
	 * ����û�
	 * @param username �û���
	 * @param password ����
	 */
	public void addUser(String username, String password){
		
			ContentValues values = new ContentValues();
			values.put("username", username);
			values.put("password", password);
			db.insert("user", null, values);
	}
	
	/**
	 * �޸�רҵ
	 */
	public boolean modifyMajor(Major major, String id){
		ContentValues values = new ContentValues();
		values.put("major_id", major.getMajor_id());
		values.put("major_name", major.getMajor_name());
		long l = db.update("major", values, "major_id=?", new String[]{id});
		return l==1;
	}
	
	/**
	 * �޸İ༶
	 */
	public boolean modifyClass(Classes classes, String id){
		ContentValues values = new ContentValues();
		values.put("class_id", classes.getClass_id());
		values.put("class_name", classes.getClass_name());
		values.put("class_date", classes.getClass_date());
		long l = db.update("class", values, "class_id=?", new String[]{id});
		return l==1;
	}
	
	/**
	 * �޸ĸ�ѧ��
	 */
	public boolean modifyStudent(Student student, String stu_id){
		ContentValues values = new ContentValues();
		values.put("student_id", student.getStu_id());
		values.put("student_name", student.getStu_name());
		values.put("student_sex", student.getStu_sex());
		values.put("student_birth", student.getStu_birth());
		long l = db.update("student", values, "student_id=?", new String[]{stu_id});
		return l==1;
	}
	
	public boolean modifyUser(String username, String password){
		ContentValues values = new ContentValues();
		values.put("password", password);
		long l = db.update("user", values, "username=?", new String[]{username});
		return l==1;
	}
	
	
	/**
	 * ����major��idɾ��רҵ
	 * @param major_id
	 */
	public boolean deleteMajor(String major_id){
		long l = db.delete("major", "major_id=?", new String[]{major_id});
		if(l==1){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * ���ݰ༶���,ɾ���༶
	 */
	public boolean deleteClass(String class_id){
		long l = db.delete("class", "class_id=?", new String[]{class_id});
		if(l==1){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * ����ѧ�ţ�ɾ��ѧ��
	 */
	public boolean deleteStudent(String stu_id){
		long l = db.delete("student", "student_id=?", new String[]{stu_id});
		if(l==1){
			return true;
		}else{
			return false;
		}
	}
	
}
