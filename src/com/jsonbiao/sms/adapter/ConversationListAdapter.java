package com.jsonbiao.sms.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsonbiao.sms.R;
import com.jsonbiao.sms.bean.Conversation;
import com.jsonbiao.sms.dao.ContactDao;
import com.jsonbiao.sms.utils.CursorUtils;

public class ConversationListAdapter extends CursorAdapter {

	private boolean isSelectMode = false;

	public boolean getIsSelectMode() {
		return isSelectMode;
	}

	public void setIsSelectMode(boolean isSelectMode) {
		this.isSelectMode = isSelectMode;
	}

	// ��¼ѡ��ģʽ��ѡ����Щ��Ŀ
	// ��ʵ��ѡ���ܵ�ʱ�򣬽�ѡ���˵ķ��뵽���������
	private List<Integer> selectConversationIds = new ArrayList<Integer>();

	@SuppressWarnings("deprecation")
	public ConversationListAdapter(Context context, Cursor c) {
		super(context, c);
	}

	// ���ص�View�������listView��Ŀ
	@SuppressWarnings("deprecation")
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		
		//CursorUtils.printCursor(cursor);
		
		ViewHolder holder = getHolder(view);
		// ����cursor���ݴ����Ự���󣬴�ʱcursor��ָ���Ѿ��ƶ�����ȷ��λ��
		Conversation conversation = Conversation.createFromCursor(cursor);

		// �жϵ�ǰ�Ƿ����ѡ��ģʽ
		if (isSelectMode) {
			holder.iv_check.setVisibility(View.VISIBLE);
			if (selectConversationIds.contains(conversation.getThread_id())) {
				holder.iv_check
						.setBackgroundResource(R.drawable.common_checkbox_checked);
			} else {
				holder.iv_check
						.setBackgroundResource(R.drawable.common_checkbox_normal);
			}
		} else {
			holder.iv_check.setVisibility(View.GONE);
		}

		// ���ú���
		// �������ѯ�Ƿ�����ϵ��
		String name = ContactDao.getNameByAddress(context.getContentResolver(),
				conversation.getAddress());
		if (TextUtils.isEmpty(name)) {
			holder.tv_conversation_address.setText(conversation.getAddress()
					+ "(" + conversation.getMsg_count() + ")");
		} else {
			holder.tv_conversation_address.setText(name + "("
					+ conversation.getMsg_count() + ")");
		}

		holder.tv_conversation_body.setText(conversation.getSnippet());

		// ����ʱ��
		// �ж��Ƿ��ǽ���=====>Android�ṩ�ķ���
		if (DateUtils.isToday(conversation.getDate())) {
			// ����ǣ���ʾʱ��
			holder.tv_conversation_date.setText(DateFormat.getTimeFormat(
					context).format(conversation.getDate()));
		} else {
			holder.tv_conversation_date.setText(DateFormat.getDateFormat(
					context).format(conversation.getDate()));
		}

		// ��ȡ��ϵ��ͷ��
		Bitmap avatar = ContactDao.getAvatarByAddress(
				context.getContentResolver(), conversation.getAddress());
		// �ж��Ƿ�ɹ��õ�ͷ��
		if (avatar == null) {
			holder.iv_conversation_avatar
					.setBackgroundResource(R.drawable.img_default_avatar);
		} else {
			holder.iv_conversation_avatar
					.setBackgroundDrawable(new BitmapDrawable(avatar));
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return View.inflate(context, R.layout.item_conversation_list, null);
	}

	// ����������Ŀ��View����
	private ViewHolder getHolder(View view) {
		// ���ж���Ŀ��View�������Ƿ���holder����
		ViewHolder holder = (ViewHolder) view.getTag();
		if (null == holder) {
			// ���û�У��ʹ���һ����������view������
			holder = new ViewHolder(view);
			view.setTag(holder);
		}
		return holder;
	}

	private class ViewHolder {

		private ImageView iv_check;
		private ImageView iv_conversation_avatar;
		private TextView tv_conversation_address;
		private TextView tv_conversation_date;
		private TextView tv_conversation_body;

		// ����������Ŀ��View����
		public ViewHolder(View view) {
			super();
			// �ڹ��췽������ɷ�װ��Ŀ���������
			iv_check = (ImageView) view.findViewById(R.id.iv_check);
			iv_conversation_avatar = (ImageView) view
					.findViewById(R.id.iv_conversation_avatar);
			tv_conversation_address = (TextView) view
					.findViewById(R.id.tv_conversation_address);
			tv_conversation_date = (TextView) view
					.findViewById(R.id.tv_conversation_date);
			tv_conversation_body = (TextView) view
					.findViewById(R.id.tv_conversation_body);
		}
	}

	public void selectSingle(int position) {
		// ��Cursor�л�ȡposition��Ӧ�ĻỰ
		// Get the data item associated with the specified position in the data
		// set=====> ���ص���data set һ�����ݵļ��ϣ���������ֱ��ת������Ҫ�ļ������ࡣ
		Cursor cursor = (Cursor) getItem(position);
		Conversation conversation = Conversation.createFromCursor(cursor);
		if (selectConversationIds.contains(conversation.getThread_id())) {
			// ǿתΪInteger�������Ѳ�����Ϊ����ɾ����������Ϊɾ����Ԫ��
			selectConversationIds.remove((Integer) conversation.getThread_id());
		} else {
			selectConversationIds.add(conversation.getThread_id());
		}
		notifyDataSetChanged();
	}

	public void selectAll() {
		Cursor cursor = getCursor();
		cursor.moveToPosition(-1);
		selectConversationIds.clear();
		while (cursor.moveToNext()) {
			Conversation conversation = Conversation.createFromCursor(cursor);
			selectConversationIds.add(conversation.getThread_id());
		}
		notifyDataSetChanged();
	}
	
	public void cancelSelect() {
		selectConversationIds.clear();
		notifyDataSetChanged();
	}

	//��ȡѡ�������ѡ��
	public List<Integer> getSelectConversationIds() {
		return selectConversationIds;
	}
	
	

}
