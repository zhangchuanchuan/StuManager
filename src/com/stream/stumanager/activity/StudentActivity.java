package com.stream.stumanager.activity;

import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.stream.stumanager.R;
import com.stream.stumanager.control.StudentAdapter;
import com.stream.stumanager.db.StuManagerDB;
import com.stream.stumanager.model.DetailStudent;
import com.stream.stumanager.model.Student;
import com.stream.stumanager.util.DialogUtil;

public class StudentActivity extends Activity implements OnItemLongClickListener
							, OnClickListener, OnCreateContextMenuListener{
	
	private StuManagerDB dbManager;
	private StudentAdapter adapter;
	private List<Student> stus;
	private ListView list;
	private String class_id;
	
	private Student student;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.students_activity);
		list = (ListView)findViewById(R.id.student_list);
		Button addStu = (Button)findViewById(R.id.add_stu); 
		Button stuButton = (Button)findViewById(R.id.stu_button);
		addStu.setOnClickListener(this);
		stuButton.setOnClickListener(this);
		
		dbManager = StuManagerDB.getInstance(this);
		Intent intent = getIntent();
		class_id = intent.getStringExtra("class_id");
		TextView title = (TextView)findViewById(R.id.stu_title);
		title.setText(class_id);
		
		//注册context menu
		
		stus = dbManager.getStudents(class_id);
		adapter = new StudentAdapter(this, R.layout.student_item, 
				stus);
		if(adapter!=null){
			list.setAdapter(adapter);
		}
		
		list.setOnItemLongClickListener(this);
		
		registerForContextMenu(list);
		list.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				Log.d("ok?", "85");
				menu.add(0, 0, 0, "修改");
				menu.add(0, 1, 0, "删除");
			}
		});
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				student = stus.get(position);
				Intent intent = new Intent(StudentActivity.this, StuDetailActivity.class);
				DetailStudent ds = dbManager.getDetailStudent(student.getStu_id());
				if(ds!=null){
				intent.putExtra("stu", ds);
				startActivity(intent);
				}else{
					Toast.makeText(StudentActivity.this, "出错了", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}



	

	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case 0:
			showModifyDialog();
			break;
		case 1:
			
			if(dbManager.deleteStudent(student.getStu_id())){
				Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
				stus.remove(student);
				adapter.notifyDataSetChanged();
			}else{
				Toast.makeText(this, "删除失败，程序出错了，请联系管理员", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.add_stu:
			showAddDialog();
			break;
		case R.id.stu_button:
			new DialogUtil(this).showMenu();
			break;
		}
	}

	/**
	 * 修改学生对话框
	 */
	private void showModifyDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_student, null);
		final EditText student_id = (EditText) view.findViewById(R.id.add_student_id);
		final EditText student_name = (EditText) view.findViewById(R.id.add_student_name);
		final EditText student_sex = (EditText)view.findViewById(R.id.add_student_sex);
		final EditText student_birth = (EditText)view.findViewById(R.id.add_student_birth);
		builder.setView(view);
		builder.setTitle("修改学生");
		student_id.setText(student.getStu_id());
		student_name.setText(student.getStu_name());
		student_sex.setText(student.getStu_sex());
		student_birth.setText(student.getStu_birth());
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String id = student_id.getText().toString();
				String name = student_name.getText().toString();
				String sex = student_sex.getText().toString();
				String birth = student_birth.getText().toString();
				if(id.equals("")||name.equals("")||sex.equals("")||birth.equals("")){
					Toast.makeText(StudentActivity.this, "任意一项不能为空", Toast.LENGTH_SHORT).show();
					showAddDialog();
				}else{
						Student temp = new Student(id, name, sex, birth, class_id);
						if(dbManager.modifyStudent(temp, student.getStu_id())){
						Toast.makeText(StudentActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
						stus.remove(student);
						stus.add(temp);
						Collections.sort(stus);
						adapter.notifyDataSetChanged();
						}else{
							Toast.makeText(StudentActivity.this, "修改失败，学号已经存在", Toast.LENGTH_SHORT).show();
						}
				}
			}
		});
		builder.setNegativeButton("取消", null);
		builder.create().show();
		
	}

	/**
	 * 添加学生对话框
	 */
	private void showAddDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_student, null);
		final EditText student_id = (EditText) view.findViewById(R.id.add_student_id);
		final EditText student_name = (EditText) view.findViewById(R.id.add_student_name);
		final EditText student_sex = (EditText)view.findViewById(R.id.add_student_sex);
		final EditText student_birth = (EditText)view.findViewById(R.id.add_student_birth);
		builder.setView(view);
		builder.setTitle("添加学生");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String id = student_id.getText().toString();
				String name = student_name.getText().toString();
				String sex = student_sex.getText().toString();
				String birth = student_birth.getText().toString();
				if(id.equals("")||name.equals("")||sex.equals("")||birth.equals("")){
					Toast.makeText(StudentActivity.this, "任意一项不能为空", Toast.LENGTH_SHORT).show();
					showAddDialog();
				}else{
						student = new Student(id, name, sex, birth, class_id);
						if(dbManager.saveStu(student)){
						Toast.makeText(StudentActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
						stus.add(student);
						Collections.sort(stus);
						adapter.notifyDataSetChanged();
						}else{
							Toast.makeText(StudentActivity.this, "添加失败，学号已经存在", Toast.LENGTH_SHORT).show();
						}
				}
			}
		});
		builder.setNegativeButton("取消", null);
		builder.create().show();
		
	}






	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		student = stus.get(position);
		return false;
	}
	
}
