package com.stream.stumanager.activity;

import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.stream.stumanager.control.ClassAdapter;
import com.stream.stumanager.db.StuManagerDB;
import com.stream.stumanager.model.Classes;
import com.stream.stumanager.model.Student;
import com.stream.stumanager.util.DialogUtil;

public class ClassesActivity extends Activity implements android.view.View.OnClickListener,
OnItemClickListener, OnItemLongClickListener{
	private StuManagerDB dbManager;
	private ClassAdapter adapter;
	private List<Classes> classesList;
	
	//全局的专业号
	private String major_id; 
	
	//临时班级
	private Classes classes;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.classes_activity);
		
		//找到所有需要的控件
		ListView list = (ListView)findViewById(R.id.classes_list);
		Button addClass = (Button)findViewById(R.id.add_class);
		Button classButton = (Button)findViewById(R.id.class_button);
		addClass.setOnClickListener(this);
		classButton.setOnClickListener(this);
		TextView title = (TextView)findViewById(R.id.class_title);
		
		
		dbManager = StuManagerDB.getInstance(this);
		
		//初始化列表
		Intent intent = getIntent();
		major_id = intent.getStringExtra("major_id");
		title.setText(major_id);
		classesList = dbManager.getClasses(major_id);
		Collections.sort(classesList);
		adapter = new ClassAdapter(this, R.layout.classes_item, 
				classesList);
		list.setAdapter(adapter);
		
		
		
		list.setOnItemClickListener(this);
		
		list.setOnItemLongClickListener(this);
		this.registerForContextMenu(list);
		list.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				menu.add(0, 0, 0, "修改");
				menu.add(0, 1, 0, "删除");
			}
		});
		
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		classes = classesList.get(position);
		return false;
	}
	
	/**
	 * 设置点击context menu事件
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
		case 0:
			showModifyDialog();
			break;
		case 1:
			List<Student> stuList = dbManager.getStudents(classes.getClass_id());
			if(stuList.size()>0){
				Toast.makeText(this, "无法删除，存在学生", Toast.LENGTH_SHORT).show();
			}else{
				if(dbManager.deleteClass(classes.getClass_id())){
					classesList.remove(classes);
					adapter.notifyDataSetChanged();
					Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(this, "出现bug了，请联系管理员",  Toast.LENGTH_SHORT).show();
				}
			}

			break;
		}
		
		return super.onContextItemSelected(item);
	}
	
	/**
	 * 弹出修改窗体
	 */
	private void showModifyDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_class, null);
		final EditText class_id = (EditText) view.findViewById(R.id.add_class_id);
		final EditText class_name = (EditText) view.findViewById(R.id.add_class_name);
		final EditText class_date = (EditText) view.findViewById(R.id.add_class_date);
		class_id.setText(classes.getClass_id());
		class_name.setText(classes.getClass_name());
		class_date.setText(classes.getClass_date());
		
		builder.setView(view);
		builder.setTitle("修改班级");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String id = class_id.getText().toString();
				String name = class_name.getText().toString();
				String date = class_date.getText().toString();
				if(id.equals("")||name.equals("")||date.equals("")){
					Toast.makeText(ClassesActivity.this, "编号或名称或日期不能为空", Toast.LENGTH_SHORT).show();
					showModifyDialog();
				}else{
						Classes temp = new Classes(id, name, date, major_id);
						
						if(dbManager.modifyClass(temp, classes.getClass_id() )){
						Toast.makeText(ClassesActivity.this, "修改该成功", Toast.LENGTH_SHORT).show();
						classesList.remove(classes);
						classesList.add(temp);
						Collections.sort(classesList);
						adapter.notifyDataSetChanged();
						}else{
							Toast.makeText(ClassesActivity.this, "修改失败，编号或名称已经存在", Toast.LENGTH_SHORT).show();
						}
				}
			}
		});
		builder.setNegativeButton("取消", null);
		builder.create().show();
		
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
				Classes classes = classesList.get(position);
				Intent intent = new Intent(ClassesActivity.this, StudentActivity.class);
				intent.putExtra("class_id", classes.getClass_id());
				startActivity(intent);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.add_class:
			showAddClassDialog();
			break;
		case R.id.class_button:
			new DialogUtil(this).showMenu();
			break;
		}
	}
	
	//添加班级的对话框
	private void showAddClassDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_class, null);
		final EditText class_id = (EditText) view.findViewById(R.id.add_class_id);
		final EditText class_name = (EditText) view.findViewById(R.id.add_class_name);
		final EditText class_date = (EditText) view.findViewById(R.id.add_class_date);
		builder.setView(view);
		builder.setTitle("添加班级");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String id = class_id.getText().toString();
				String name = class_name.getText().toString();
				String date = class_date.getText().toString();
				if(id.equals("")||name.equals("")||date.equals("")){
					Toast.makeText(ClassesActivity.this, "编号或名称或日期不能为空", Toast.LENGTH_SHORT).show();
					showAddClassDialog();
				}else{
						classes = new Classes(id, name, date, major_id);
						
						if(dbManager.saveClass(classes)){
						Toast.makeText(ClassesActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
						classesList.add(classes);
						Collections.sort(classesList);
						adapter.notifyDataSetChanged();
						}else{
							Toast.makeText(ClassesActivity.this, "添加失败，编号或名称已经存在", Toast.LENGTH_SHORT).show();
						}
				}
			}
		});
		builder.setNegativeButton("取消", null);
		builder.create().show();
		
	}
	

}
