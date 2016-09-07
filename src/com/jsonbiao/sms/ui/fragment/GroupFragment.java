package com.jsonbiao.sms.ui.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.jsonbiao.sms.R;
import com.jsonbiao.sms.adapter.GroupListAdapter;
import com.jsonbiao.sms.base.BaseFragment;
import com.jsonbiao.sms.bean.Group;
import com.jsonbiao.sms.dao.GroupDao;
import com.jsonbiao.sms.dao.SimpleQueryHandler;
import com.jsonbiao.sms.dialog.InputDialog;
import com.jsonbiao.sms.dialog.ListDialog;
import com.jsonbiao.sms.globle.Constant;
import com.jsonbiao.sms.ui.activity.GroupDetailActivity;
import com.jsonbiao.sms.utils.ToastUtils;

public class GroupFragment extends BaseFragment {

	private ListView lv_group_list;
	private Button bt_group_newgroup;
	private GroupListAdapter adapter;

	@Override
	public View initView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_group, null);
		lv_group_list = (ListView) view.findViewById(R.id.lv_group_list);
		bt_group_newgroup = (Button) view.findViewById(R.id.bt_group_newgroup);
		return view;
	}

	@Override
	public void initData() {
		adapter = new GroupListAdapter(getActivity(), null);
		lv_group_list.setAdapter(adapter);

		String[] projection = new String[] { "_id", "name", "create_date",
				"thread_count" };
		SimpleQueryHandler queryHandler = new SimpleQueryHandler(getActivity()
				.getContentResolver());
		queryHandler.startQuery(0, adapter, Constant.URI.URI_GROUP_QUERY,
				projection, null, null, "create_date desc");
	}

	@Override
	public void initListner() {
		bt_group_newgroup.setOnClickListener(this);
		
		lv_group_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//��תʱЯ��Ⱥ�����֡�Ⱥ��id
				Cursor cursor = (Cursor) adapter.getItem(position);
				Group group = Group.createFromCursor(cursor);
				
				if(group.getThread_count() > 0) {
					Intent intent = new Intent(getActivity(), GroupDetailActivity.class);
					intent.putExtra("name", group.getName());
					intent.putExtra("group_id", group.get_id());
					startActivity(intent);
				}else {
					ToastUtils.showToast(getActivity(), "�÷���û���κλỰ");
				}
			}
			
		});
		
		//����Ŀ���ó�������
		lv_group_list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Cursor cursor = (Cursor) adapter.getItem(position);
				final Group group = Group.createFromCursor(cursor);
				//����֮��������һ���Ի���
				ListDialog.showDialog(getActivity(), "ѡ�����", new String[] {
						"�޸�", "ɾ��" }, new ListDialog.OnListDialogListener() {
					
					@Override
					public void OnItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						switch (position) {
						case 0://�޸�
							//��������Ի���
							InputDialog.showDialog(getActivity(), "�޸�Ⱥ��", new InputDialog.OnInputDialogListener() {
								
								@Override
								public void onConfirm(String text) {
									//ȷ���޸�Ⱥ����
									GroupDao.updateGroupName(getActivity().getContentResolver(), text,group.get_id());
								}
								
								@Override
								public void onCancel() {
									
								}
							});
							break;
						case 1://ɾ��
							GroupDao.deleteGroup(getActivity().getContentResolver(), group.get_id());
							break;
						}
					}
				});
				// ���ĵ���������¼�����Ȼ��������Ч
				return true;
			}

		});
	}

	@Override
	public void processClick(View view) {
		switch (view.getId()) {
		case R.id.bt_group_newgroup:
			InputDialog.showDialog(getActivity(), "����Ⱥ��",
					new InputDialog.OnInputDialogListener() {

						@Override
						public void onConfirm(String text) {
							if (!TextUtils.isEmpty(text)) {
								GroupDao.insertGroup(getActivity()
										.getContentResolver(), text);
							} else {
								ToastUtils.showToast(getActivity(), "Ⱥ��������Ϊ��");
							}
						}

						@Override
						public void onCancel() {

						}
					});
			break;

		default:
			break;
		}
	}

}
