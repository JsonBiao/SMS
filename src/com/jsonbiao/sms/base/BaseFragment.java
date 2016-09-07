package com.jsonbiao.sms.base;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment implements OnClickListener{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return initView(inflater, container, savedInstanceState);
	}
	
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initListner();
		initData();
	}



	public abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
	public abstract void initData();
	public abstract void initListner();
	public abstract void processClick(View view);

	
	
	@Override
	public void onClick(View view) {
		processClick(view);
	}
	
	
	
	
}
