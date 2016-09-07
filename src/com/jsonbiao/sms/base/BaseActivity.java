package com.jsonbiao.sms.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

public abstract class BaseActivity extends FragmentActivity implements OnClickListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		initListner();
		initData();
		
	}
	
	//Ä£°åÄ£Ê½
	public abstract void initView();
	public abstract void initData();
	public abstract void initListner();
	public abstract void processClick(View v);
	
	@Override
	public void onClick(View view) {
		processClick(view);
	}
	
}
