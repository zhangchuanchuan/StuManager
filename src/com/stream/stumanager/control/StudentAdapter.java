package com.stream.stumanager.control;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.stream.stumanager.R;
import com.stream.stumanager.model.Student;

public class StudentAdapter extends ArrayAdapter<Student> {
	private int resourceId;

	public StudentAdapter(Context context, int resource, List<Student> objects) {
		super(context, resource, objects);
		resourceId = resource;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Student s = getItem(position);
		
		View view;
		ViewHolder viewHolder;
		if(convertView==null){
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.id = (TextView)view.findViewById(R.id.stu_id);
			viewHolder.name = (TextView)view.findViewById(R.id.stu_name);
			viewHolder.sex = (TextView)view.findViewById(R.id.stu_sex);
			viewHolder.birth = (TextView)view.findViewById(R.id.stu_birth);
			view.setTag(viewHolder);
		}else{
			view = convertView;
			viewHolder = (ViewHolder)view.getTag();
		}
		viewHolder.id.setText(s.getStu_id());
		viewHolder.name.setText(s.getStu_name());
		viewHolder.sex.setText(s.getStu_sex());
		viewHolder.birth.setText(s.getStu_birth());
		return view;
	}
	
	class ViewHolder{
		TextView id;
		TextView name;
		TextView sex;
		TextView birth;
	}
}
