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

		// ��ȡ�༭�˵�����ѡ��˵���view��ͼ
		ll_conversation_edit_menu = (LinearLayout) view
				.findViewById(R.id.ll_conversation_edit_menu);
		ll_conversation_select_menu = (LinearLayout) view
				.findViewById(R.id.ll_conversation_select_menu);

		// ��ȡ�༭�˵���Button����
		bt_conversation_edit = (Button) view
				.findViewById(R.id.bt_conversation_edit);
		bt_conversation_new_msg = (Button) view
				.findViewById(R.id.bt_conversation_new_msg);

		// ѡ��˵���Button����
		bt_conversation_select_all = (Button) view
				.findViewById(R.id.bt_conversation_select_all);
		bt_conversation_cancel_select = (Button) view
				.findViewById(R.id.bt_conversation_cancel_select);
		bt_conversation_delete = (Button) view
				.findViewById(R.id.bt_conversation_delete);

		return view;
	}

	// ��ʼ�����ݣ�����ҳһ���򿪾�Ҫ���ֵ�����
	@Override
	public void initData() {

		adapter = new ConversationListAdapter(getActivity(), null);
		lv_conversation_list.setAdapter(adapter);

		String[] projection = new String[] {
				"sms.body AS snippet",
				"sms.thread_id AS _id",// ������һ����_id��,CursorAdapte��Ҫ�õ�
				"groups.msg_count AS msg_count", "address AS address",
				"date AS date" };

		SimpleQueryHandler queryHandler = new SimpleQueryHandler(getActivity()
				.getContentResolver());
		// ��ʼ�첽��ѯ
		// arg0��arg1:��������Я��һ��int�ͺ�һ������
		// arg1:����Я��һ��adapter���󣬲�ѯ��Ͻoadapter����һ��cursor
		// projection��Ҫ��ѯ���ֶΣ�selection�Ǹ���ʲô������ѯ��
		queryHandler.startQuery(0, adapter, Constant.URI.URI_SMS_CONVERSATION,
				projection, null, null, "date desc");
	}

	@Override
	public void initListner() {
		// �����༭�˵��İ�ť
		bt_conversation_edit.setOnClickListener(this);
		bt_conversation_new_msg.setOnClickListener(this);

		// ѡ��˵��ļ���
		bt_conversation_select_all.setOnClickListener(this);
		bt_conversation_cancel_select.setOnClickListener(this);
		bt_conversation_delete.setOnClickListener(this);

		// ������ListView��item�ϵļ���
		lv_conversation_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (adapter.getIsSelectMode()) {
					// ѡ��ѡ��
					adapter.selectSingle(position);
				} else {
					// ����Ự��ϸ
					//��������ת���Ự����ҳ�棬ҪЯ�����ݹ�ȥ��
					Intent intent = new Intent();
					intent.setClass(getActivity(), ConversationDetailActivity.class);
					//Я�����ݣ�address��thread_id
					//����position������,ͨ��position���cursor��Ȼ���ȡcursor�е�����
					Cursor cursor = (Cursor) adapter.getItem(position);
					Conversation conversation = Conversation.createFromCursor(cursor);
					intent.putExtra("address", conversation.getAddress());
					intent.putExtra("thread_id", conversation.getThread_id());
					startActivity(intent);
				}
			}

		});
		
		//���ó�������
		lv_conversation_list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Cursor cursor = (Cursor) adapter.getItem(position);
				Conversation conversation = Conversation.createFromCursor(cursor);
				//�ж�ѡ�еĻỰ�Ƿ���������Ⱥ��
				if(ThreadGroupDao.hasGroup(getActivity().getContentResolver(), conversation.getThread_id())) {
					//�ûỰ�Ѿ���ӣ�����confirmDialog�Ի���
					showExitDialog(conversation.getThread_id());
				}else {
					//�ûỰû�б���ӹ�������ListDialog�Ի���
					showSelectGroupDialog(conversation.getThread_id());
				}
				//���ĵ�������������ֹOnItemClickListener��Ч
				return true;
			}
		});
	}
	
	

	@Override
	public void processClick(View view) {
		switch (view.getId()) {
		case R.id.bt_conversation_edit:// �༭״̬
			// ����ѡ��ģʽ
			showSelectMenu();
			adapter.setIsSelectMode(true);
			// �����Զ�ˢ��UI�������ݸı䣬�������û�ı��ֶ����������Ѿ��ı�
			adapter.notifyDataSetChanged();
			break;
		case R.id.bt_conversation_new_msg:// �½���Ϣ
			Intent intent = new Intent(getActivity(), NewMsgActivity.class);
			startActivity(intent);
			break;
		case R.id.bt_conversation_select_all:// ȫѡ״̬
			adapter.selectAll();
			break;
		case R.id.bt_conversation_cancel_select:// ȡ��ѡ��
			// �˳�ѡ��ģʽ������༭ģʽ
			showEditMenu();
			adapter.setIsSelectMode(false);
			adapter.cancelSelect();
			break;
		case R.id.bt_conversation_delete:// ɾ��ѡ�����Ŀ
			selectConversationIds = adapter
			.getSelectConversationIds();
			if(0 == selectConversationIds.size()) {
				//����û���û��ѡ���κ���Ϣ�����ȵ���һ���Ի�������ѡ��һ��Ҫɾ���ĶԻ���
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
		ConfirmDialog.showDialog(getActivity(), "��ʾ", "���Ҫɾ���Ự��", new OnConfirmListener() {
			
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

	//�������ʵ��һ���Ựɾ������
	//����һ����Ǽ�¼�Ƿ��ж�ɾ��
	private boolean isStopDelete = false;
	private void deleteSms() {	
		dialog = DeleteMsgDialog.showDeleteDialog(getActivity(), selectConversationIds.size(),new OnDeleteCancelListener() {
			
			@Override
			public void onCancel() {
				//��ֹͣɾ��״̬��?
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
					//�ж�ɾ��==������û�ͻȻ����ɾ����
					if(isStopDelete) {
						//���¸�ֵΪfalse,Ϊ��һ��ɾ����׼������Ȼ�´β���ɾ��
						isStopDelete = false;
						break;
					}
					//ȡ�������еĻỰId����IdΪwhere����ɾ�����з���������ֵ
					//���i���������ݵ��ڼ����еĽǱ�
					String where = "thread_id = " + selectConversationIds.get(i);
					getActivity().getContentResolver().delete(Constant.URI.URI_SMS,
							where, null);
					
					//�Ựɾ����Ӧ��������ĻỰҲ��Ҫɾ��
					int group_id = ThreadGroupDao.getGroupIdByThreadId(getActivity().getContentResolver(), selectConversationIds.get(i));
					ThreadGroupDao.deleteThreadGroupByThreadId(getActivity().getContentResolver(), selectConversationIds.get(i), group_id);
					
					//������Ϣ������ɾ���Ľ�����ˢ�£�ͬʱ�ѵ�ǰ��ɾ���Ľ��ȴ��ݸ�������
					Message msg = handler.obtainMessage();
					msg.what = WHAT_UPDATE_DELETE_PROGRESS;
					//����ǰ���ȴ�����Ϣ��
					msg.arg1 = i;
					handler.sendMessage(msg);					
				}
				
				//ɾ���Ự����ռ���
				selectConversationIds.clear();
				handler.sendEmptyMessage(WHAT_DELETE_COMPLETE);
			}
		}.start();
	}

	
	private void showExitDialog(final Integer thread_id) {
		//��ͨ���Ựid��ѯȺ��id
		final int group_id = ThreadGroupDao.getGroupIdByThreadId(getActivity().getContentResolver(), thread_id);
		//ͨ��Ⱥ��id��ѯȺ������
		String name = GroupDao.getGroupNameByGroupId(getActivity().getContentResolver(), group_id);
		String message = "�ûỰ�ѱ������[" + name + "]Ⱥ�飬�Ƿ�Ҫ�˳���Ⱥ�飿";
		ConfirmDialog.showDialog(getActivity(), "��ʾ", message, new OnConfirmListener() {
			
			@Override
			public void onConfirm() {
				//��ѡ�еĻỰ��Ⱥ����ɾ��
				boolean isSuccess = ThreadGroupDao.deleteThreadGroupByThreadId(getActivity().getContentResolver(), thread_id, group_id);
				ToastUtils.showToast(getActivity(), isSuccess ? "�˳��ɹ�":"�˳�ʧ��");
			}
			
			@Override
			public void onCancle() {
				
			}
		});
		
	}
	
	private void showSelectGroupDialog(final Integer thread_id) {
		//��ѯһ������ЩȺ�飬ȡ������ȫ������items
		final Cursor cursor = getActivity().getContentResolver().query(Constant.URI.URI_GROUP_QUERY, null, null, null, null);
		if(0 == cursor.getCount()) {
			ToastUtils.showToast(getActivity(), "��û�д���Ⱥ�飬���ȴ���Ⱥ��");
			return;
		}
		
		String[] items = new String[cursor.getCount()];
		//����cursor��ȡ������
		while(cursor.moveToNext()) {
			items[cursor.getPosition()] = cursor.getString(cursor.getColumnIndex("name"));
		}
		
		ListDialog.showDialog(getActivity(), "ѡ��Ⱥ��", items, new ListDialog.OnListDialogListener() {
			
			@Override
			public void OnItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				//cursor���ǲ�ѯgroups��õ��ģ��������Ⱥ���������Ϣ
				cursor.moveToPosition(position);
				Group group = Group.createFromCursor(cursor);
				//��ָ���Ự����ָ��Ⱥ��
				boolean isSuccess = ThreadGroupDao.insertThreadGroup(getActivity().getContentResolver(), thread_id, group.get_id());
				ToastUtils.showToast(getActivity(), isSuccess ? "����ɹ�":"����ʧ��");
			}
		});
	}
}
