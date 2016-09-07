package com.jsonbiao.sms.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.jsonbiao.sms.R;

public class AutoSearchAdapter extends CursorAdapter {

	

	@SuppressWarnings("deprecation")
	public AutoSearchAdapter(Context context, Cursor c) {
		super(context, c);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View view = View.inflate(context, R.layout.item_auto_search_tv, null);		
		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder holder = getHolder(view);
		holder.tv_autosearch_name.setText(cursor.getString(cursor.getColumnIndex("display_name")));
		holder.tv_autosearch_address.setText(cursor.getString(cursor.getColumnIndex("data1")));
	}
	
	private ViewHolder getHolder(View view) {
		ViewHolder holder = (ViewHolder) view.getTag();
		if(null == holder) {
			holder = new ViewHolder(view);
			view.setTag(holder);
		}
		return holder;
	}
	
	private class ViewHolder {
		private TextView tv_autosearch_name;
		private TextView tv_autosearch_address;
		
		public ViewHolder(View view) {
			tv_autosearch_name = (TextView) view.findViewById(R.id.tv_autosearch_name);
			tv_autosearch_address = (TextView) view.findViewById(R.id.tv_autosearch_address);
		}
	}
	
	/**
	 * ��������б���Ŀ�Ƿ���
	 * AutoCompleteTextView�����ԣ���������ListView�������ڲ��Ѿ�ʵ�ּ���
	 */
	@Override
	public CharSequence convertToString(Cursor cursor) {
		return cursor.getString(cursor.getColumnIndex("data1"));
	}

}
