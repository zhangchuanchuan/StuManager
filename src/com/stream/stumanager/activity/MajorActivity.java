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
import android.widget.Toast;

import com.stream.stumanager.R;
import com.stream.stumanager.control.MajorAdapter;
import com.stream.stumanager.db.StuManagerDB;
import com.stream.stumanager.model.Classes;
import com.stream.stumanager.model.Major;
import com.stream.stumanager.util.DialogUtil;



public class MajorActivity extends Activity implements android.view.View.OnClickListener,
						OnItemClickListener, OnItemLongClickListener{
	
	private StuManagerDB dbManager;
	private MajorAdapter adapter;
	private List<Major> list;
	
	private long touch = 0;
	//��ʱmajor����ɾ��ȫ����
	private Major major;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.major_activity);
		
		dbManager = StuManagerDB.getInstance(this);
		ListView majorList = (ListView)findViewById(R.id.major_list);
		
		list = dbManager.getMajor();
		Collections.sort(list);
		adapter = new MajorAdapter(this,R.layout.major_item, list);
		majorList.setAdapter(adapter);
		majorList.setOnItemClickListener(this);

		majorList.setOnItemLongClickListener(this);
		
		Button addMajor = (Button)findViewById(R.id.add_major);
		addMajor.setOnClickListener(this);
		Button majorButton = (Button)findViewById(R.id.major_button);
		majorButton.setOnClickListener(this);
		//ע��contextMenu
		this.registerForContextMenu(majorList);
		majorList.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				menu.add(0, 0, 0, "�޸�");
				menu.add(0, 1, 0, "ɾ��");
			}
			
		});
	}


	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.add_major:
			showAddMajorDialog();
			break;
		case R.id.major_button:
			new DialogUtil(this).showMenu();
			break;
		}
	}

	/**
	 * ���רҵ�ĶԻ���
	 */
	private void showAddMajorDialog(){

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_major, null);
		final EditText major_id = (EditText) view.findViewById(R.id.add_major_id);
		final EditText major_name = (EditText) view.findViewById(R.id.add_major_name);
		builder.setView(view);
		builder.setTitle("���רҵ");
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String id = major_id.getText().toString();
				String name = major_name.getText().toString();
				if(id.equals("")||name.equals("")){
					Toast.makeText(MajorActivity.this, "��Ż����Ʋ���Ϊ��", Toast.LENGTH_SHORT).show();
					showAddMajorDialog();
				}else{
						major = new Major(id,name);
						if(dbManager.saveMajor(major)){
						Toast.makeText(MajorActivity.this, "��ӳɹ�", Toast.LENGTH_SHORT).show();
						list.add(major);
						Collections.sort(list);
						adapter.notifyDataSetChanged();
						}else{
							Toast.makeText(MajorActivity.this, "���ʧ�ܣ���Ż������Ѿ�����", Toast.LENGTH_SHORT).show();
						}
				}
			}
		});
		builder.setNegativeButton("ȡ��", null);
		builder.create().show();
		
	}


	/**
	 * ���רҵ������Ӧרҵ�İ༶
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
				major = list.get(position);
				Intent intent = new Intent(MajorActivity.this, ClassesActivity.class);
				intent.putExtra("major_id", major.getMajor_id());
				startActivity(intent);
			}


	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			final int position, long id) {
		//���Ҫ������Major����
		major = list.get(position);
		
		return false;
	}

	//context menu�¼�
	@Override
	public boolean onContextItemSelected(MenuItem item) {

		
		switch(item.getItemId()){
		case 0:
			showModifyDialog();
			break;
		case 1:
			List<Classes> classList=dbManager.getClasses(major.getMajor_id());
			if(classList.size()>0){
				Toast.makeText(this, "�޷�ɾ�������ڰ༶", Toast.LENGTH_SHORT).show();
			}else{
				if(dbManager.deleteMajor(major.getMajor_id())){
			
				list.remove(major);
				adapter.notifyDataSetChanged();
				Toast.makeText(this, "ɾ���ɹ�", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(this, "����bug�ˣ�����ϵ����Ա", Toast.LENGTH_SHORT).show();
				}
			}
			break;
		}
		return super.onContextItemSelected(item);
	}
	
	
	/**
	 * �޸�רҵ�Ի���
	 */
	private void showModifyDialog(){

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_major, null);
		final EditText major_id = (EditText) view.findViewById(R.id.add_major_id);
		final EditText major_name = (EditText) view.findViewById(R.id.add_major_name);
		major_id.setText(major.getMajor_id());
		major_name.setText(major.getMajor_name());
		builder.setView(view);
		builder.setTitle("�޸�רҵ");
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String id = major_id.getText().toString();
				String name = major_name.getText().toString();
				if(id.equals("")||name.equals("")){
					Toast.makeText(MajorActivity.this, "��Ż����Ʋ���Ϊ��", Toast.LENGTH_SHORT).show();
					showModifyDialog();
				}else{
						Major temp = new Major(id,name);
						if(dbManager.modifyMajor(temp, major.getMajor_id())){
							Toast.makeText(MajorActivity.this, "�޸ĳɹ�", Toast.LENGTH_SHORT).show();
							list.remove(major);
							list.add(temp);
							Collections.sort(list);
							adapter.notifyDataSetChanged();
						}else{
							Toast.makeText(MajorActivity.this, "�޸�ʧ�ܣ���Ż������Ѿ�����", Toast.LENGTH_SHORT).show();
						}
				}
			}
		});
		builder.setNegativeButton("ȡ��", null);
		builder.create().show();
		
	}
	
	@Override
	public void onBackPressed() {
		long currentTime = System.currentTimeMillis();
		if(currentTime-touch >= 2000){
			Toast.makeText(this, "�ٰ�һ���˳�", Toast.LENGTH_SHORT).show();
			touch = currentTime;
		}else{
			finish();
		}
		
	}
	
}
