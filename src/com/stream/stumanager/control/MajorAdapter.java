package com.stream.stumanager.control;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.stream.stumanager.R;
import com.stream.stumanager.model.Major;

public class MajorAdapter extends ArrayAdapter<Major>{
	
	private int resource;
	
	
	public MajorAdapter(Context context, int resource,
			List<Major> objects) {
		super(context, resource, objects);
		this.resource = resource;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Major major = getItem(position);
		View view;
		ViewHolder viewHolder;
		if(convertView==null){
			view = LayoutInflater.from(getContext()).inflate(resource, null);
			viewHolder = new ViewHolder();
			viewHolder.major_id = (TextView)view.findViewById(R.id.major_id);
			viewHolder.major_name = (TextView)view.findViewById(R.id.major_name);
			view.setTag(viewHolder);
		}else{
			view = convertView;
			viewHolder = (ViewHolder)view.getTag();
		}
		viewHolder.major_id.setText(major.getMajor_id());
		viewHolder.major_name.setText(major.getMajor_name());
		return view;
		
	}
	class ViewHolder{
		TextView major_id;
		TextView major_name;
	}

}
