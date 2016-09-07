package com.jsonbiao.sms.dialog;

import com.jsonbiao.sms.R;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public abstract class BaseDialog extends AlertDialog implements OnClickListener {

	protected BaseDialog(Context context) {
		//ͨ�����췽��ָ�����⣬�����о��Ѿ������˻��εı߽Ǳ���
		super(context,R.style.BaseDialog);
	}
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initListener();
		initData();
	}



	@Override
	public void onClick(View v) {
		processClick(v);
	}
	
	public abstract void initView();
	public abstract void initData();
	public abstract void initListener();
	public abstract void processClick(View view);

}
