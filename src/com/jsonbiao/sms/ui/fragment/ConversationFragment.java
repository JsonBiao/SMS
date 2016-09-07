package com.jsonbiao.sms.ui.fragment;

import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jsonbiao.sms.R;
import com.jsonbiao.sms.adapter.ConversationListAdapter;
import com.jsonbiao.sms.base.BaseFragment;
import com.jsonbiao.sms.bean.Conversation;
import com.jsonbiao.sms.bean.Group;
import com.jsonbiao.sms.dao.GroupDao;
import com.jsonbiao.sms.dao.SimpleQueryHandler;
import com.jsonbiao.sms.dao.ThreadGroupDao;
import com.jsonbiao.sms.dialog.ConfirmDialog;
import com.jsonbiao.sms.dialog.ListDialog;
import com.jsonbiao.sms.dialog.ConfirmDialog.OnConfirmListener;
import com.jsonbiao.sms.dialog.DeleteMsgDialog;
import com.jsonbiao.sms.dialog.DeleteMsgDialog.OnDeleteCancelListener;
import com.jsonbiao.sms.globle.Constant;
import com.jsonbiao.sms.ui.activity.ConversationDetailActivity;
import com.jsonbiao.sms.ui.activity.NewMsgActivity;
import com.jsonbiao.sms.utils.ToastUtils;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class ConversationFragment extends BaseFragment {

	private LinearLayout ll_conversation_select_menu;
	private LinearLayout ll_conversation_edit_menu;
	private Button bt_conversation_edit;
	private Button bt_conversation_new_msg;
	private Button bt_conversation_select_all;
	private Button bt_conversation_cancel_select;
	private Button bt_conversation_delete;
	private ListView lv_conversation_list;
	private ConversationListAdapter adapter;
	private List<Integer> selectConversationIds;
	private DeleteMsgDialog dialog;
	
	private static final int WHAT_DELETE_COMPLETE = 0;
	private static final int WHAT_UPDATE_DELETE_PROGRESS = 1;
	
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WHAT_DELETE_COMPLETE:
				adapter.setIsSelectMode(false);
				adapter.notifyDataSetChanged();
				showEditMenu();
				dialog.dismiss();
				break;

			case WHAT_UPDATE_DELETE_PROGRESS:
				dialog.updateProgressAndTitle(msg.arg1 + 1);
				break;
			}
		}
		
	};

	@Override
	public View initView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_conversation, null);

		lv_conversation_list = (ListView) view
				.findViewById(R.id.lv_conversation_list);

		// 获取编辑菜单栏和选择菜单的view视图
		ll_conversation_edit_menu = (LinearLayout) view
				.findViewById(R.id.ll_conversation_edit_menu);
		ll_conversation_select_menu = (LinearLayout) view
				.findViewById(R.id.ll_conversation_select_menu);

		// 获取编辑菜单的Button对象
		bt_conversation_edit = (Button) view
				.findViewById(R.id.bt_conversation_edit);
		bt_conversation_new_msg = (Button) view
				.findViewById(R.id.bt_conversation_new_msg);

		// 选择菜单的Button对象
		bt_conversation_select_all = (Button) view
				.findViewById(R.id.bt_conversation_select_all);
		bt_conversation_cancel_select = (Button) view
				.findViewById(R.id.bt_conversation_cancel_select);
		bt_conversation_delete = (Button) view
				.findViewById(R.id.bt_conversation_delete);

		return view;
	}

	// 初始化数据，各种页一旦打开就要出现的数据
	@Override
	public void initData() {

		adapter = new ConversationListAdapter(getActivity(), null);
		lv_conversation_list.setAdapter(adapter);

		String[] projection = new String[] {
				"sms.body AS snippet",
				"sms.thread_id AS _id",// 必须有一个“_id”,CursorAdapte需要用到
				"groups.msg_count AS msg_count", "address AS address",
				"date AS date" };

		SimpleQueryHandler queryHandler = new SimpleQueryHandler(getActivity()
				.getContentResolver());
		// 开始异步查询
		// arg0、arg1:可以用来携带一个int型和一个对象
		// arg1:用来携带一个adapter对象，查询完毕給adapter设置一个cursor
		// projection是要查询的字段，selection是根据什么条件查询。
		queryHandler.startQuery(0, adapter, Constant.URI.URI_SMS_CONVERSATION,
				projection, null, null, "date desc");
	}

	@Override
	public void initListner() {
		// 监听编辑菜单的按钮
		bt_conversation_edit.setOnClickListener(this);
		bt_conversation_new_msg.setOnClickListener(this);

		// 选择菜单的监听
		bt_conversation_select_all.setOnClickListener(this);
		bt_conversation_cancel_select.setOnClickListener(this);
		bt_conversation_delete.setOnClickListener(this);

		// 设置在ListView的item上的监听
		lv_conversation_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (adapter.getIsSelectMode()) {
					// 选中选框
					adapter.selectSingle(position);
				} else {
					// 进入会话详细
					//这里是跳转到会话详情页面，要携带数据过去。
					Intent intent = new Intent();
					intent.setClass(getActivity(), ConversationDetailActivity.class);
					//携带数据，address和thread_id
					//利用position做文章,通过position获得cursor，然后获取cursor中的数据
					Cursor cursor = (Cursor) adapter.getItem(position);
					Conversation conversation = Conversation.createFromCursor(cursor);
					intent.putExtra("address", conversation.getAddress());
					intent.putExtra("thread_id", conversation.getThread_id());
					startActivity(intent);
				}
			}

		});
		
		//设置长按侦听
		lv_conversation_list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Cursor cursor = (Cursor) adapter.getItem(position);
				Conversation conversation = Conversation.createFromCursor(cursor);
				//判断选中的会话是否有所属的群组
				if(ThreadGroupDao.hasGroup(getActivity().getContentResolver(), conversation.getThread_id())) {
					//该会话已经添加，弹出confirmDialog对话框
					showExitDialog(conversation.getThread_id());
				}else {
					//该会话没有被添加过，弹出ListDialog对话框
					showSelectGroupDialog(conversation.getThread_id());
				}
				//消耗掉长按侦听，阻止OnItemClickListener生效
				return true;
			}
		});
	}
	
	

	@Override
	public void processClick(View view) {
		switch (view.getId()) {
		case R.id.bt_conversation_edit:// 编辑状态
			// 进入选择模式
			showSelectMenu();
			adapter.setIsSelectMode(true);
			// 不会自动刷新UI除非数据改变，如果数据没改变手动设置数据已经改变
			adapter.notifyDataSetChanged();
			break;
		case R.id.bt_conversation_new_msg:// 新建消息
			Intent intent = new Intent(getActivity(), NewMsgActivity.class);
			startActivity(intent);
			break;
		case R.id.bt_conversation_select_all:// 全选状态
			adapter.selectAll();
			break;
		case R.id.bt_conversation_cancel_select:// 取消选择
			// 退出选择模式，进入编辑模式
			showEditMenu();
			adapter.setIsSelectMode(false);
			adapter.cancelSelect();
			break;
		case R.id.bt_conversation_delete:// 删除选择的项目
			selectConversationIds = adapter
			.getSelectConversationIds();
			if(0 == selectConversationIds.size()) {
				//如果用户还没有选择任何信息可以先弹出一个对话框让其选择一个要删除的对话。
		//		showDeleteDialog2();
				return;
			}
			showDeleteDialog();
			break;

		default:
			break;
		}
	}

	private void showDeleteDialog() {
		ConfirmDialog.showDialog(getActivity(), "提示", "真的要删除会话吗", new OnConfirmListener() {
			
			@Override
			public void onConfirm() {
				deleteSms();
			}
			
			@Override
			public void onCancle() {
				
			}
		});
	}

	private void showEditMenu() {
		ViewPropertyAnimator.animate(ll_conversation_select_menu)
				.translationY(ll_conversation_select_menu.getHeight())
				.setDuration(200);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				ViewPropertyAnimator.animate(ll_conversation_edit_menu)
						.translationY(0).setDuration(200);
			}
		}, 200);

	}

	private void showSelectMenu() {
		ViewPropertyAnimator.animate(ll_conversation_edit_menu)
				.translationY(ll_conversation_edit_menu.getHeight())
				.setDuration(200);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				ViewPropertyAnimator.animate(ll_conversation_select_menu)
						.translationY(0).setDuration(200);
			}
		}, 200);
	}

	//这里具体实现一个会话删除功能
	//定义一个标记记录是否中断删除
	private boolean isStopDelete = false;
	private void deleteSms() {	
		dialog = DeleteMsgDialog.showDeleteDialog(getActivity(), selectConversationIds.size(),new OnDeleteCancelListener() {
			
			@Override
			public void onCancel() {
				//是停止删除状态吗?
				isStopDelete = true;
			}
		});
		new Thread() {
			@Override
			public void run() {
				for (int i = 0; i < selectConversationIds.size(); i++) {
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//中断删除==》如果用户突然不想删除了
					if(isStopDelete) {
						//重新赋值为false,为下一次删除做准备，不然下次不会删除
						isStopDelete = false;
						break;
					}
					//取出集合中的会话Id，以Id为where条件删除所有符合条件的值
					//这个i是这条数据的在集合中的角标
					String where = "thread_id = " + selectConversationIds.get(i);
					getActivity().getContentResolver().delete(Constant.URI.URI_SMS,
							where, null);
					
					//会话删除相应的组里面的会话也需要删除
					int group_id = ThreadGroupDao.getGroupIdByThreadId(getActivity().getContentResolver(), selectConversationIds.get(i));
					ThreadGroupDao.deleteThreadGroupByThreadId(getActivity().getContentResolver(), selectConversationIds.get(i), group_id);
					
					//发送消息，让其删除的进度条刷新，同时把当前的删除的进度传递个进度条
					Message msg = handler.obtainMessage();
					msg.what = WHAT_UPDATE_DELETE_PROGRESS;
					//将当前进度存入消息中
					msg.arg1 = i;
					handler.sendMessage(msg);					
				}
				
				//删除会话后，清空集合
				selectConversationIds.clear();
				handler.sendEmptyMessage(WHAT_DELETE_COMPLETE);
			}
		}.start();
	}

	
	private void showExitDialog(final Integer thread_id) {
		//先通过会话id查询群组id
		final int group_id = ThreadGroupDao.getGroupIdByThreadId(getActivity().getContentResolver(), thread_id);
		//通过群组id查询群组名字
		String name = GroupDao.getGroupNameByGroupId(getActivity().getContentResolver(), group_id);
		String message = "该会话已被添加至[" + name + "]群组，是否要退出该群组？";
		ConfirmDialog.showDialog(getActivity(), "提示", message, new OnConfirmListener() {
			
			@Override
			public void onConfirm() {
				//把选中的会话从群组中删除
				boolean isSuccess = ThreadGroupDao.deleteThreadGroupByThreadId(getActivity().getContentResolver(), thread_id, group_id);
				ToastUtils.showToast(getActivity(), isSuccess ? "退出成功":"退出失败");
			}
			
			@Override
			public void onCancle() {
				
			}
		});
		
	}
	
	private void showSelectGroupDialog(final Integer thread_id) {
		//查询一共有哪些群组，取出名字全部存入items
		final Cursor cursor = getActivity().getContentResolver().query(Constant.URI.URI_GROUP_QUERY, null, null, null, null);
		if(0 == cursor.getCount()) {
			ToastUtils.showToast(getActivity(), "还没有创建群组，请先创建群组");
			return;
		}
		
		String[] items = new String[cursor.getCount()];
		//遍历cursor，取出名字
		while(cursor.moveToNext()) {
			items[cursor.getPosition()] = cursor.getString(cursor.getColumnIndex("name"));
		}
		
		ListDialog.showDialog(getActivity(), "选择群组", items, new ListDialog.OnListDialogListener() {
			
			@Override
			public void OnItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				//cursor就是查询groups表得到的，里面就是群组的所有信息
				cursor.moveToPosition(position);
				Group group = Group.createFromCursor(cursor);
				//把指定会话存入指定群组
				boolean isSuccess = ThreadGroupDao.insertThreadGroup(getActivity().getContentResolver(), thread_id, group.get_id());
				ToastUtils.showToast(getActivity(), isSuccess ? "插入成功":"插入失败");
			}
		});
	}
}
