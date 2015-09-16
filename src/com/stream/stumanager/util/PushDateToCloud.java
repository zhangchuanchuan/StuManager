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
	
	//��ʼ��
	@Override
	protected void onPreExecute() {
		pd = new ProgressDialog(context);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMax(100);
		pd.setTitle("���ڴ����������ƶ�");
		pd.setMessage("0%");
		pd.show();
	}
	

	//��̨�����߳�
	@Override
	protected Boolean doInBackground(Void... params) {
		
		int i=0;
		while(i<=100){
			pd.setProgress(i++);
			//�����ȷ��������߳�
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
	
	//��ý��ȵķ�������仯
	@Override
	protected void onProgressUpdate(Integer... values) {
		pd.setMessage("����"+(values[0]-1)+"%");
	}

	//���ս������
	@Override
	protected void onPostExecute(Boolean result) {
		pd.dismiss();
	}
	
}
