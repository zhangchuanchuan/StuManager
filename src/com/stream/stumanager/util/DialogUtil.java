package com.stream.stumanager.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.stream.stumanager.R;
import com.stream.stumanager.activity.LoginActivity;
import com.stream.stumanager.activity.StuDetailActivity;
import com.stream.stumanager.db.StuManagerDB;
import com.stream.stumanager.model.DetailStudent;

public class DialogUtil {
	private Context context;
	
	private StuManagerDB dbManager;
	private SharedPreferences pref;
	
	public DialogUtil(Context context){
		this.context = context;
		dbManager = StuManagerDB.getInstance(context);
		pref = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public void showMenu(){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("选择操作");
		builder.setItems(new String[]{"修改密码", "查找学生", "同步到云", "获取更新", "退出登录"}, 
				new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch(which){
				case 0:
					showModifyPassword();
					break;
				case 1:
					showSearchDialog();
					break;
				case 2:
					PushDateToCloud pdtc = new PushDateToCloud(context);
					pdtc.execute();
					break;
				case 3:
					PullDateFromCloud pdfc = new PullDateFromCloud(context);
					pdfc.execute();
					break;
				case 4:
					Intent intent = new Intent(context, LoginActivity.class);
					context.startActivity(intent);
					Activity a = (Activity)context;
					a.finish();
					break;
				}
			}
		});
		builder.create().show();
	}
	
	private void showSearchDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("查询学生");
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_search_student, null);
		final EditText stu_id = (EditText)view.findViewById(R.id.search_stu_id);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String ds_id = stu_id.getText().toString();
				if(ds_id.equals("")){
					Toast.makeText(context, "学号不能为空", Toast.LENGTH_SHORT).show();
					showSearchDialog();
					return;
				}
				DetailStudent ds = dbManager.getDetailStudent(ds_id);
				if(ds!=null){
					Intent intent = new Intent(context, StuDetailActivity.class);
					intent.putExtra("stu", ds);
					context.startActivity(intent);
				}else{
					Toast.makeText(context, "没有这个人", Toast.LENGTH_SHORT).show();
				}
				
			}
			
		});
		builder.setNegativeButton("取消", null);
		builder.setView(view);
		builder.create().show();
	}
	
	private void showModifyPassword(){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("修改密码");
		//填充布局获得控件
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_modify_password, null);
		final EditText originPassword = (EditText)view.findViewById(R.id.origin_password);
		final EditText newPassword = (EditText)view.findViewById(R.id.new_password);
		final EditText confirmPassword = (EditText)view.findViewById(R.id.confirm_password);
		

		
		//设置点击事件
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String origin = originPassword.getText().toString();
				String newpass = newPassword.getText().toString();
				String confirm = confirmPassword.getText().toString();
				String username = pref.getString("username", "");
				String password = pref.getString("password", "");
				
				if(origin.equals("")||newpass.equals("")||confirm.equals("")){
					Toast.makeText(context, "有空项", Toast.LENGTH_SHORT).show();
					showModifyPassword();
					return;
				}
				if(!newpass.equals(confirm)){
					Toast.makeText(context, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
					showModifyPassword();
					return;
				}
				
				if(newpass.equals(password)){
					Toast.makeText(context, "未修改密码", Toast.LENGTH_SHORT).show();
					showModifyPassword();
					return;
				}
				
				if(origin.equals(password)){
					if(dbManager.modifyUser(username, newpass)){
						Editor editor = pref.edit();
						editor.putString("password", newpass);
						editor.commit();
						Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(context, "修改失败", Toast.LENGTH_SHORT).show();
					}
				}
				
			}
		});
		
		builder.setNegativeButton("取消", null);
		
		builder.setView(view);
		builder.create().show();

	}
	
	
	
}
