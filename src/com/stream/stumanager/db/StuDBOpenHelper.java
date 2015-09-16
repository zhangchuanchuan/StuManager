package com.stream.stumanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class StuDBOpenHelper extends SQLiteOpenHelper {
	private final String create_user = "create table user(" +
			"id integer primary key autoincrement," +
			"username text unique not null," +
			"password text not null)";
	
	private final String create_major = "create table major(" +
			"id integer primary key autoincrement," +
			"major_id text unique not null," +
			"major_name text unique not null)";
	
	private final String create_class = "create table class(" +
			"id integer primary key autoincrement," +
			"class_id text unique not null," +
			"class_name text unique not null," +
			"class_date text," +
			"major_id text references major(major_id))";
	
	private final String create_student = "create table student(" +
			"id integer primary key autoincrement," +
			"student_id text unique not null," +
			"student_name text not null," +
			"student_sex text," +
			"student_birth text," +
			"class_id text references class(class_id))";

	
	public StuDBOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(create_user);
		db.execSQL(create_major);
		db.execSQL(create_class);
		db.execSQL(create_student);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
