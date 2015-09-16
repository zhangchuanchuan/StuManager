package com.stream.stumanager.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.stream.stumanager.R;
import com.stream.stumanager.db.StuManagerDB;

public class RegistActivity extends Activity implements android.view.View.OnClickListener{
	
	//获得需要的控件，数据库对象等
	private EditText name;
	private EditText pass;
	
	private StuManagerDB stuManagerDB;
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		stuManagerDB = StuManagerDB.getInstance(this);
		//findView
		setContentView(R.layout.activity_regist);
		name = (EditText)findViewById(R.id.regist_name);
		pass = (EditText)findViewById(R.id.regist_pass);
		Button commit = (Button)findViewById(R.id.regist_commit);
		Button cancel = (Button)findViewById(R.id.regist_cancel);
		commit.setOnClickListener(this);
		cancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.regist_commit:
			String username = name.getText().toString();
			String password = pass.getText().toString();
			if(username.equals("")||password.equals("")){
				Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
			}else{
				if(stuManagerDB.checkByName(username)){
					Toast.makeText(this, "注册失败，已经存在的用户名", Toast.LENGTH_SHORT).show();
				}else{
					stuManagerDB.addUser(username, password);
					Toast.makeText(this, "注册成功，返回登录界面", Toast.LENGTH_SHORT).show();
					finish();
				}
			}
			break;
		case R.id.regist_cancel:
			finish();
			break;
		default:
			break;
		}
	}


}
