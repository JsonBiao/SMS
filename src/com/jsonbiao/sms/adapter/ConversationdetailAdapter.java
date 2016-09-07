package com.jsonbiao.sms.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jsonbiao.sms.R;
import com.jsonbiao.sms.bean.Sms;
import com.jsonbiao.sms.globle.Constant;

public class ConversationdetailAdapter extends CursorAdapter {

	//3分钟，转换成毫秒
	private static final int DURATION = 3 * 60 * 1000;
	
	private ListView lv;
	
	@SuppressWarnings("deprecation")
	public ConversationdetailAdapter(Context context, Cursor c, ListView lv) {
		super(context, c);
		this.lv = lv;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View view = View.inflate(context, R.layout.item_conversation_detail,
				null);
		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// 组件对象全在holder里面
		ViewHolder holder = getHolder(view);
		// 数据全在sms对象里
		Sms sms = Sms.createFromCursor(cursor);

		// 设置显示内容
		//判断时间间隔，从而判断是否显示时间
		//第一条短信不需要对比时间
		if(0 == cursor.getPosition()) {
			showDate(context, holder, sms);
		}else {
			//判断当前短信与上一条短信的时间间隔是否超过3分钟
			Long preDate = getPreviousSmsDate(cursor.getPosition());
			if(sms.getDate() - preDate > DURATION) {
				showDate(context, holder, sms);
				holder.tv_conversation_detail_date.setVisibility(View.VISIBLE);
			}else {
				holder.tv_conversation_detail_date.setVisibility(View.GONE);
			}
		}
		

		// 分别显示接到短信和发出短信
		// 这个Type的值是和数据库中对应的。
		// 每个adapter中封装的都是（时间、收件人、发件人这三个组件。）
		//视图隐藏了，如果要重新显示，必须改为显示，如果显示，要隐藏，则需从改为隐藏
		holder.tv_conversation_detail_send.setVisibility(sms.getType() == Constant.SMS.TYPE_SEND ? View.VISIBLE : View.GONE);
		holder.tv_conversation_detail_receive.setVisibility(sms.getType() == Constant.SMS.TYPE_RECEIVE ? View.VISIBLE : View.GONE);
		if (sms.getType() == Constant.SMS.TYPE_RECEIVE) {			
			holder.tv_conversation_detail_receive.setText(sms.getBody());
		} else {			
			holder.tv_conversation_detail_send.setText(sms.getBody());
		}
	}
	
	/**
	 * 获取上一条短信的时间
	 * @param position
	 * @return
	 */
	private Long getPreviousSmsDate(int position) {
		//获取上一条的结果集
		Cursor cursor = (Cursor)getItem(position - 1);
		//从对象中获取该条目的信息
		return Sms.createFromCursor(cursor).getDate();
	}

	
	//这是一个显示时间的具体方法
	private void showDate(Context context, ViewHolder holder, Sms sms) {
		holder.tv_conversation_detail_date.setText(DateUtils.isToday(sms
				.getDate()) ? DateFormat.getTimeFormat(context).format(
				sms.getDate()) : DateFormat.getDateFormat(context).format(
				sms.getDate()));
	}

	private ViewHolder getHolder(View view) {
		ViewHolder holder = (ViewHolder) view.getTag();
		if (null == holder) {
			holder = new ViewHolder(view);
			view.setTag(holder);
		}
		return holder;
	}

	private class ViewHolder {

		private TextView tv_conversation_detail_date;
		private TextView tv_conversation_detail_receive;
		private TextView tv_conversation_detail_send;

		public ViewHolder(View view) {
			tv_conversation_detail_date = (TextView) view
					.findViewById(R.id.tv_conversation_detail_date);
			tv_conversation_detail_receive = (TextView) view
					.findViewById(R.id.tv_conversation_detail_receive);
			tv_conversation_detail_send = (TextView) view
					.findViewById(R.id.tv_conversation_detail_send);
		}

	}
	
	@Override
	public void changeCursor(Cursor cursor) {
		// TODO Auto-generated method stub
		super.changeCursor(cursor);
		lv.setSelection(getCount());
	}
}
