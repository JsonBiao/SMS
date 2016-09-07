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

	// 记录选择模式下选中哪些条目
	// 在实现选择功能的时候，将选择了的放入到这个集合中
	private List<Integer> selectConversationIds = new ArrayList<Integer>();

	@SuppressWarnings("deprecation")
	public ConversationListAdapter(Context context, Cursor c) {
		super(context, c);
	}

	// 返回的View对象就是listView条目
	@SuppressWarnings("deprecation")
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		
		//CursorUtils.printCursor(cursor);
		
		ViewHolder holder = getHolder(view);
		// 根据cursor内容创建会话对象，此时cursor的指针已经移动至正确的位置
		Conversation conversation = Conversation.createFromCursor(cursor);

		// 判断当前是否进入选择模式
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

		// 设置号码
		// 按号码查询是否有联系人
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

		// 设置时间
		// 判断是否是今天=====>Android提供的方法
		if (DateUtils.isToday(conversation.getDate())) {
			// 如果是，显示时分
			holder.tv_conversation_date.setText(DateFormat.getTimeFormat(
					context).format(conversation.getDate()));
		} else {
			holder.tv_conversation_date.setText(DateFormat.getDateFormat(
					context).format(conversation.getDate()));
		}

		// 获取联系人头像
		Bitmap avatar = ContactDao.getAvatarByAddress(
				context.getContentResolver(), conversation.getAddress());
		// 判断是否成功拿到头像
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

	// 参数就是条目的View对象
	private ViewHolder getHolder(View view) {
		// 先判断条目的View对象中是否有holder对象
		ViewHolder holder = (ViewHolder) view.getTag();
		if (null == holder) {
			// 如果没有，就创建一个，并存入view对象中
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

		// 参数就是条目的View对象
		public ViewHolder(View view) {
			super();
			// 在构造方法中完成封装条目的所有组件
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
		// 从Cursor中获取position对应的会话
		// Get the data item associated with the specified position in the data
		// set=====> 返回的是data set 一个数据的集合，这样可以直接转化成想要的集合种类。
		Cursor cursor = (Cursor) getItem(position);
		Conversation conversation = Conversation.createFromCursor(cursor);
		if (selectConversationIds.contains(conversation.getThread_id())) {
			// 强转为Integer，否则会把参数作为索引删除而不是作为删除的元素
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

	//获取选择的所用选项
	public List<Integer> getSelectConversationIds() {
		return selectConversationIds;
	}
	
	

}
