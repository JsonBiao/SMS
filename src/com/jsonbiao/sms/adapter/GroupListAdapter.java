package com.jsonbiao.sms.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.jsonbiao.sms.R;
import com.jsonbiao.sms.bean.Group;
import com.jsonbiao.sms.utils.CursorUtils;

public class GroupListAdapter extends CursorAdapter {

	public GroupListAdapter(Context context, Cursor c) {
		super(context, c);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return View.inflate(context, R.layout.item_group_list, null);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder holder = getHolder(view);
		//将结果集转化为一个bean对象。
		Group group = Group.createFromCursor(cursor);
		holder.tv_grouplist_name.setText(group.getName() + "("
				+ group.getThread_count() + ")");
		holder.tv_grouplist_date.setText(DateUtils.isToday(group
				.getCreate_date()) ? DateFormat.getTimeFormat(context).format(
				group.getCreate_date()) : DateFormat.getDateFormat(context)
				.format(group.getCreate_date()));
	}

	public ViewHolder getHolder(View view) {
		ViewHolder holder = (ViewHolder) view.getTag();
		if (null == holder) {
			holder = new ViewHolder(view);
		}
		return holder;
	}

	private class ViewHolder {

		private TextView tv_grouplist_date;
		private TextView tv_grouplist_name;

		public ViewHolder(View view) {
			tv_grouplist_date = (TextView) view
					.findViewById(R.id.tv_grouplist_date);
			tv_grouplist_name = (TextView) view
					.findViewById(R.id.tv_grouplist_name);
		}

	}

}
