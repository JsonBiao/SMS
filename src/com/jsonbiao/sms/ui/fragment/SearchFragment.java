package com.jsonbiao.sms.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.jsonbiao.sms.R;
import com.jsonbiao.sms.adapter.ConversationListAdapter;
import com.jsonbiao.sms.base.BaseFragment;
import com.jsonbiao.sms.bean.Conversation;
import com.jsonbiao.sms.dao.SimpleQueryHandler;
import com.jsonbiao.sms.globle.Constant;
import com.jsonbiao.sms.ui.activity.ConversationDetailActivity;

public class SearchFragment extends BaseFragment {

	private EditText et_search_content;
	private ListView lv_search_list;
	private ConversationListAdapter adapter;
	private SimpleQueryHandler queryHandler;
	
	String[] projection = new String[] {
			"sms.body AS snippet",
			"sms.thread_id AS _id",// 必须有一个“_id”,CursorAdapte需要用到
			"groups.msg_count AS msg_count", "address AS address",
			"date AS date" };

	@Override
	public View initView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_search, null);
		et_search_content = (EditText) view.findViewById(R.id.et_search_content);
		lv_search_list = (ListView) view.findViewById(R.id.lv_search_list);
		return view;
	}

	@Override
	public void initData() {
		adapter = new ConversationListAdapter(getActivity(), null);
		lv_search_list.setAdapter(adapter);
				
		queryHandler = new SimpleQueryHandler(getActivity()
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
		et_search_content.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				queryHandler.startQuery(0, adapter, Constant.URI.URI_SMS_CONVERSATION,
						projection, "snippet like '%" + s + "%'", null, "date desc");
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		
		
		lv_search_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
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
			
		});
		
		lv_search_list.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				//输入法管理器
				//隐藏输入法软键盘
				InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
			}
		});
		
	}

	@Override
	public void processClick(View view) {
		
	}
	
	

}
