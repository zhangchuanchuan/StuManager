package com.stream.stumanager.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class PushDateToCloud extends AsyncTask<Void, Integer, Boolean>{
	
	private Context context;
	
	private ProgressDialog pd;
	
	public PushDateToCloud(Context context){
		super();
		this.context = context;
	}
	
	//初始化
	@Override
	protected void onPreExecute() {
		pd = new ProgressDialog(context);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMax(100);
		pd.setTitle("正在传送数据至云端");
		pd.setMessage("0%");
		pd.show();
	}
	

	//后台的子线程
	@Override
	protected Boolean doInBackground(Void... params) {
		
		int i=0;
		while(i<=100){
			pd.setProgress(i++);
			//将进度反馈给主线程
			publishProgress(i);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				return false;
			}
			
		}
		
		return true;
	}
	
	//获得进度的反馈处理变化
	@Override
	protected void onProgressUpdate(Integer... values) {
		pd.setMessage("进度"+(values[0]-1)+"%");
	}

	//最终结果处理
	@Override
	protected void onPostExecute(Boolean result) {
		pd.dismiss();
	}
	
}
