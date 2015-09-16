package com.stream.stumanager.control;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.stream.stumanager.R;
import com.stream.stumanager.model.Classes;

public class ClassAdapter extends ArrayAdapter<Classes>{
	private int resourceId;

	public ClassAdapter(Context context, int resource, List<Classes> objects) {
		super(context, resource, objects);
		resourceId = resource;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Classes c = getItem(position);
		View view;
		ViewHolder viewHolder;
		if(convertView==null){
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.class_id = (TextView)view.findViewById(R.id.class_id);
			viewHolder.class_name = (TextView)view.findViewById(R.id.class_name);
			viewHolder.class_date = (TextView)view.findViewById(R.id.class_date);
			view.setTag(viewHolder);
		}else{
			view = convertView;
			viewHolder = (ViewHolder)view.getTag();
		}
		viewHolder.class_id.setText(c.getClass_id());
		viewHolder.class_name.setText(c.getClass_name());
		viewHolder.class_date.setText(c.getClass_date());
		return view;
		
	}

	class ViewHolder{
		TextView class_id;
		TextView class_name;
		TextView class_date;
	}



}
