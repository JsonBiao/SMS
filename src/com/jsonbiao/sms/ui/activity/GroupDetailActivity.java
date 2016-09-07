package com.jsonbiao.sms.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.jsonbiao.sms.R;
import com.jsonbiao.sms.adapter.ConversationListAdapter;
import com.jsonbiao.sms.base.BaseActivity;
import com.jsonbiao.sms.bean.Conversation;
import com.jsonbiao.sms.bean.Group;
import com.jsonbiao.sms.dao.GroupDao;
import com.jsonbiao.sms.dao.SimpleQueryHandler;
import com.jsonbiao.sms.dao.ThreadGroupDao;
import com.jsonbiao.sms.dialog.ConfirmDialog;
import com.jsonbiao.sms.dialog.ConfirmDialog.OnConfirmListener;
import com.jsonbiao.sms.dialog.ListDialog;
import com.jsonbiao.sms.globle.Constant;
import com.jsonbiao.sms.utils.ToastUtils;

public class GroupDetailActivity extends BaseActivity {

	private ListView lv_group_detail;
	private int group_id;
	private String groupName;
	private ConversationListAdapter adapter;
	
	private String[] projection = { "sms.body AS snippet", "sms.thread_id AS _id",
			"groups.msg_count AS msg_count", "address AS address",
			"date AS date" };
	private SimpleQueryHandler hanlder;

	@Override
	public void initView() {
		setContentView(R.layout.activity_group_detail);
		lv_group_detail = (ListView) findViewById(R.id.lv_group_detail);
	}

	@Override
	public void initData() {
		Intent intent = getIntent();
		groupName = intent.getStringExtra("name");
		group_id = intent.getIntExtra("group_id", -1);
		
		initTitleBar();

		//复用会话列表的所有组件、参数、条目布局、查询字段、表
		//只要查询的条件不一样
		adapter = new ConversationListAdapter(this, null);
		lv_group_detail.setAdapter(adapter);
		

		hanlder = new SimpleQueryHandler(
				getContentResolver());
		hanlder.startQuery(0, adapter, Constant.URI.URI_SMS_CONVERSATION,
				projection, buildQuery(), null, "date desc");

	}

	private String buildQuery() {
		//查询当前群组包含的所有会话的thread_id，这些会话都会被显示在GroupDetailActivity的界面中
		Cursor cursor = getContentResolver().query(Constant.URI.URI_THREAD_GROUP_QUERY,
				new String[] { "thread_id" }, "group_id=" + group_id, null,
				null);
		
		StringBuilder where = new StringBuilder();
		where.append("thread_id in(");
		while(cursor.moveToNext()) {
			if(cursor.isLast()) {
				//最后一个会话id后面不要逗号
				where.append(cursor.getInt(0));
			}else {
				where.append(cursor.getInt(0)).append(",");
			}
		}
		where.append(")");
		
		return where.toString();
	}

	private void initTitleBar() {
		((TextView) findViewById(R.id.tv_title_bact_title)).setText(groupName);
		findViewById(R.id.iv_titlebar_back_btn).setOnClickListener(this);
	}

	@Override
	public void initListner() {
		lv_group_detail.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
					Intent intent = new Intent(GroupDetailActivity.this, ConversationDetailActivity.class);
					//携带数据：address和thread_id
					Cursor cursor = (Cursor) adapter.getItem(position);
					Conversation conversation = Conversation.createFromCursor(cursor);
					intent.putExtra("address", conversation.getAddress());
					intent.putExtra("thread_id", conversation.getThread_id());
					startActivity(intent);
				
				
			}
		});
		
		lv_group_detail.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Cursor cursor = (Cursor)adapter.getItem(position);
				Conversation conversation = Conversation.createFromCursor(cursor);
				//判断选中的会话是否有所属的群组
				if(ThreadGroupDao.hasGroup(getContentResolver(), conversation.getThread_id())){
					//该会话已经被添加，弹出ConfirmDialog
					showExitDialog(conversation.getThread_id());
				}
				
				//消费掉这个事件，否则会传递给OnItemClickListener
				return true;
			}
		});
	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_titlebar_back_btn:
			finish();
			break;

		default:
			break;
		}
	}
	
	private void showExitDialog(final Integer thread_id) {
		//先通过会话id查询群组id
		final int group_id = ThreadGroupDao.getGroupIdByThreadId(getContentResolver(), thread_id);
		//通过群组id查询群组名字
		String name = GroupDao.getGroupNameByGroupId(getContentResolver(), group_id);
		String message = "会话是否移除[" + name + "]群组！";
		ConfirmDialog.showDialog(this, "提示", message, new OnConfirmListener() {
			
			@Override
			public void onConfirm() {
				//把选中的会话从群组中删除
				boolean isSuccess = ThreadGroupDao.deleteThreadGroupByThreadId(getContentResolver(), thread_id, group_id);
				ToastUtils.showToast(GroupDetailActivity.this, isSuccess ? "移除成功":"移除失败");
				hanlder.startQuery(0, adapter, Constant.URI.URI_SMS_CONVERSATION,
						projection, buildQuery(), null, "date desc");
			}
			
			@Override
			public void onCancle() {
				
			}
		});
		
	}
	
}
