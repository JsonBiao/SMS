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

	//3���ӣ�ת���ɺ���
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
		// �������ȫ��holder����
		ViewHolder holder = getHolder(view);
		// ����ȫ��sms������
		Sms sms = Sms.createFromCursor(cursor);

		// ������ʾ����
		//�ж�ʱ�������Ӷ��ж��Ƿ���ʾʱ��
		//��һ�����Ų���Ҫ�Ա�ʱ��
		if(0 == cursor.getPosition()) {
			showDate(context, holder, sms);
		}else {
			//�жϵ�ǰ��������һ�����ŵ�ʱ�����Ƿ񳬹�3����
			Long preDate = getPreviousSmsDate(cursor.getPosition());
			if(sms.getDate() - preDate > DURATION) {
				showDate(context, holder, sms);
				holder.tv_conversation_detail_date.setVisibility(View.VISIBLE);
			}else {
				holder.tv_conversation_detail_date.setVisibility(View.GONE);
			}
		}
		

		// �ֱ���ʾ�ӵ����źͷ�������
		// ���Type��ֵ�Ǻ����ݿ��ж�Ӧ�ġ�
		// ÿ��adapter�з�װ�Ķ��ǣ�ʱ�䡢�ռ��ˡ��������������������
		//��ͼ�����ˣ����Ҫ������ʾ�������Ϊ��ʾ�������ʾ��Ҫ���أ�����Ӹ�Ϊ����
		holder.tv_conversation_detail_send.setVisibility(sms.getType() == Constant.SMS.TYPE_SEND ? View.VISIBLE : View.GONE);
		holder.tv_conversation_detail_receive.setVisibility(sms.getType() == Constant.SMS.TYPE_RECEIVE ? View.VISIBLE : View.GONE);
		if (sms.getType() == Constant.SMS.TYPE_RECEIVE) {			
			holder.tv_conversation_detail_receive.setText(sms.getBody());
		} else {			
			holder.tv_conversation_detail_send.setText(sms.getBody());
		}
	}
	
	/**
	 * ��ȡ��һ�����ŵ�ʱ��
	 * @param position
	 * @return
	 */
	private Long getPreviousSmsDate(int position) {
		//��ȡ��һ���Ľ����
		Cursor cursor = (Cursor)getItem(position - 1);
		//�Ӷ����л�ȡ����Ŀ����Ϣ
		return Sms.createFromCursor(cursor).getDate();
	}

	
	//����һ����ʾʱ��ľ��巽��
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
