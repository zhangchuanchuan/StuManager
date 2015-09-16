package com.stream.stumanager.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.stream.stumanager.R;
import com.stream.stumanager.model.DetailStudent;

public class StuDetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.student_detail);
		//findView
		TextView majorId = (TextView)findViewById(R.id.detail_major_id);
		TextView majorName = (TextView)findViewById(R.id.detail_major_name);
		TextView classId = (TextView)findViewById(R.id.detail_class_id);
		TextView className = (TextView)findViewById(R.id.detail_class_name);
		TextView name = (TextView)findViewById(R.id.detail_name);
		TextView id = (TextView)findViewById(R.id.detail_id);
		TextView sex = (TextView)findViewById(R.id.detail_sex);
		TextView birth= (TextView)findViewById(R.id.detail_birth);
		
		//获得对象
		DetailStudent ds = (DetailStudent)getIntent().getSerializableExtra("stu");
		
		
		
		//设置显示信息
		majorId.setText(ds.getMajor_id());
		majorName.setText(ds.getMajor_name());
		classId.setText(ds.getClass_id());
		className.setText(ds.getClass_name());
		name.setText(ds.getName());
		id.setText(ds.getId());
		sex.setText(ds.getSex());
		birth.setText(ds.getBirth());
		
		
		
	}
}
