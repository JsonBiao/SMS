package com.jsonbiao.sms.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jsonbiao.sms.R;

public class ListDialog extends BaseDialog {

	private TextView tv_listdialog_title;
	private ListView lv_listdialog;
	private String title;

	private String[] items;
	private OnListDialogListener onListDialogListener;
	
	protected ListDialog(Context context,String title,String[] items,OnListDialogListener onListDialogListener) {
		super(context);
		this.title = title;
		this.items = items;
		this.onListDialogListener = onListDialogListener;
	}
	
	
	public static void showDialog(Context context,String title,String[] items,OnListDialogListener onListDialogListener) {
		ListDialog dialog = new ListDialog(context,title,items,onListDialogListener);
		dialog.show();
	}

	@Override
	public void initView() {
		setContentView(R.layout.dialog_list);
		tv_listdialog_title = (TextView) findViewById(R.id.tv_listdialog_title);
		lv_listdialog = (ListView) findViewById(R.id.lv_listdialog);
	}

	@Override
	public void initData() {
		tv_listdialog_title.setText(title);
		lv_listdialog.setAdapter(new myAdapter());
	}

	@Override
	public void initListener() {
		lv_listdialog.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(null != onListDialogListener) {
					onListDialogListener.OnItemClick(parent, view, position, id);
				}
				dismiss();
			}
		});
	}

	@Override
	public void processClick(View view) {

	}
	
	public interface OnListDialogListener {
		void OnItemClick(AdapterView<?> parent, View view,
				int position, long id);
	}
	
	private class myAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return items.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(getContext(), R.layout.item_listdialog, null);
			TextView tv_item_listdialog = (TextView) view.findViewById(R.id.tv_item_listdialog);
			tv_item_listdialog.setText(items[position]);
			return view;
		}
		
	}

}
