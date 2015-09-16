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
	
	//ȫ�ֵ�רҵ��
	private String major_id; 
	
	//��ʱ�༶
	private Classes classes;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.classes_activity);
		
		//�ҵ�������Ҫ�Ŀؼ�
		ListView list = (ListView)findViewById(R.id.classes_list);
		Button addClass = (Button)findViewById(R.id.add_class);
		Button classButton = (Button)findViewById(R.id.class_button);
		addClass.setOnClickListener(this);
		classButton.setOnClickListener(this);
		TextView title = (TextView)findViewById(R.id.class_title);
		
		
		dbManager = StuManagerDB.getInstance(this);
		
		//��ʼ���б�
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
				menu.add(0, 0, 0, "�޸�");
				menu.add(0, 1, 0, "ɾ��");
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
	 * ���õ��context menu�¼�
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
				Toast.makeText(this, "�޷�ɾ��������ѧ��", Toast.LENGTH_SHORT).show();
			}else{
				if(dbManager.deleteClass(classes.getClass_id())){
					classesList.remove(classes);
					adapter.notifyDataSetChanged();
					Toast.makeText(this, "ɾ���ɹ�", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(this, "����bug�ˣ�����ϵ����Ա",  Toast.LENGTH_SHORT).show();
				}
			}

			break;
		}
		
		return super.onContextItemSelected(item);
	}
	
	/**
	 * �����޸Ĵ���
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
		builder.setTitle("�޸İ༶");
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String id = class_id.getText().toString();
				String name = class_name.getText().toString();
				String date = class_date.getText().toString();
				if(id.equals("")||name.equals("")||date.equals("")){
					Toast.makeText(ClassesActivity.this, "��Ż����ƻ����ڲ���Ϊ��", Toast.LENGTH_SHORT).show();
					showModifyDialog();
				}else{
						Classes temp = new Classes(id, name, date, major_id);
						
						if(dbManager.modifyClass(temp, classes.getClass_id() )){
						Toast.makeText(ClassesActivity.this, "�޸ĸóɹ�", Toast.LENGTH_SHORT).show();
						classesList.remove(classes);
						classesList.add(temp);
						Collections.sort(classesList);
						adapter.notifyDataSetChanged();
						}else{
							Toast.makeText(ClassesActivity.this, "�޸�ʧ�ܣ���Ż������Ѿ�����", Toast.LENGTH_SHORT).show();
						}
				}
			}
		});
		builder.setNegativeButton("ȡ��", null);
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
	
	//��Ӱ༶�ĶԻ���
	private void showAddClassDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_class, null);
		final EditText class_id = (EditText) view.findViewById(R.id.add_class_id);
		final EditText class_name = (EditText) view.findViewById(R.id.add_class_name);
		final EditText class_date = (EditText) view.findViewById(R.id.add_class_date);
		builder.setView(view);
		builder.setTitle("��Ӱ༶");
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String id = class_id.getText().toString();
				String name = class_name.getText().toString();
				String date = class_date.getText().toString();
				if(id.equals("")||name.equals("")||date.equals("")){
					Toast.makeText(ClassesActivity.this, "��Ż����ƻ����ڲ���Ϊ��", Toast.LENGTH_SHORT).show();
					showAddClassDialog();
				}else{
						classes = new Classes(id, name, date, major_id);
						
						if(dbManager.saveClass(classes)){
						Toast.makeText(ClassesActivity.this, "��ӳɹ�", Toast.LENGTH_SHORT).show();
						classesList.add(classes);
						Collections.sort(classesList);
						adapter.notifyDataSetChanged();
						}else{
							Toast.makeText(ClassesActivity.this, "���ʧ�ܣ���Ż������Ѿ�����", Toast.LENGTH_SHORT).show();
						}
				}
			}
		});
		builder.setNegativeButton("ȡ��", null);
		builder.create().show();
		
	}
	

}
