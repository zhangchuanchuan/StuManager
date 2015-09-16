package com.stream.stumanager.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.stream.stumanager.R;
import com.stream.stumanager.db.StuManagerDB;

public class LoginActivity extends Activity implements android.view.View.OnClickListener{
	private long touch = 0;
	
	private SharedPreferences prefs;
	private Editor edit;
	
	private Button login;
	private Button regist;
	private EditText userName;
	private EditText password;
	private CheckBox re_name;
	private CheckBox re_word;
	
	private StuManagerDB stuManagerDB;
	
	/**
	 * 活动创建时执行
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		//findView
		login = (Button)findViewById(R.id.login);
		regist = (Button)findViewById(R.id.regist);
		userName =(EditText)findViewById(R.id.username);
		password = (EditText)findViewById(R.id.password);
		re_name = (CheckBox)findViewById(R.id.re_name);
		re_word = (CheckBox)findViewById(R.id.re_pass);
		
		stuManagerDB = StuManagerDB.getInstance(this);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		boolean reName = prefs.getBoolean("re_name", false);
		boolean reWord = prefs.getBoolean("re_word", false);
		
		if(reName){
			String name = prefs.getString("username", "");
			userName.setText(name);
			re_name.setChecked(true);
			if(reWord){
				String key = prefs.getString("password", "");
				password.setText(key);
				re_word.setChecked(true);
			}
		}
		
		login.setOnClickListener(this);
		regist.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.login:
			String name = userName.getText().toString();
			String pass = password.getText().toString();
			if(name.equals("")||pass.equals("")){

				Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
			}else{
				if(stuManagerDB.checkLogin(name, pass)){
					edit = prefs.edit();
					edit.putString("username", name);
					edit.putString("password", pass);
					edit.putBoolean("re_name", re_name.isChecked());
					edit.putBoolean("re_word", re_word.isChecked());
					edit.commit();
					Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
					//切换到用户界面
					Intent intent = new Intent(this, MajorActivity.class);
					startActivity(intent);
					this.finish();
				}else{
					Toast.makeText(this, "登录失败，请检查用户名和密码", Toast.LENGTH_SHORT).show();
				}
			}
			
			break;
		case R.id.regist:
			//切换到注册界面
			Intent intent = new Intent(this,RegistActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		long currentTime = System.currentTimeMillis();
		if(currentTime-touch >= 2000){
			Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
			touch = currentTime;
		}else{
			finish();
		}
		
	}
	
}
