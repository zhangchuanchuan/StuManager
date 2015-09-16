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
	
	//�����Ҫ�Ŀؼ������ݿ�����
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
				Toast.makeText(this, "�û��������벻��Ϊ��", Toast.LENGTH_SHORT).show();
			}else{
				if(stuManagerDB.checkByName(username)){
					Toast.makeText(this, "ע��ʧ�ܣ��Ѿ����ڵ��û���", Toast.LENGTH_SHORT).show();
				}else{
					stuManagerDB.addUser(username, password);
					Toast.makeText(this, "ע��ɹ������ص�¼����", Toast.LENGTH_SHORT).show();
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
