package com.jsonbiao.sms.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jsonbiao.sms.R;
import com.jsonbiao.sms.adapter.ConversationdetailAdapter;
import com.jsonbiao.sms.base.BaseActivity;
import com.jsonbiao.sms.dao.ContactDao;
import com.jsonbiao.sms.dao.SimpleQueryHandler;
import com.jsonbiao.sms.dao.SmsDao;
import com.jsonbiao.sms.globle.Constant;

public class ConversationDetailActivity extends BaseActivity {


	private ListView lv_conversation_detail;
	private EditText et_conversation_detail;
	private Button bt_conversation_detail_send;
	private String address;
	private int thread_id;

	@Override
	public void initView() {
		setContentView(R.layout.activity_conversation_detail);

		lv_conversation_detail = (ListView) findViewById(R.id.lv_conversation_detail);

		et_conversation_detail = (EditText) findViewById(R.id.et_conversation_detail);
		bt_conversation_detail_send = (Button) findViewById(R.id.bt_conversation_detail_send);
		
		//只要ListView刷新，就会滑动到底部
		lv_conversation_detail.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
	}

	@Override
	public void initData() {
		//获取传递过来的数据
		Intent intent = getIntent();
		if(null != intent) {
			address = intent.getStringExtra("address");
			thread_id = intent.getIntExtra("thread_id", -1);
			initTitleBar();
		}
		
		//给会话详细界面ListView设置adapter，显示会话的所有短信
		ConversationdetailAdapter adapter = new ConversationdetailAdapter(this, null, lv_conversation_detail);
		lv_conversation_detail.setAdapter(adapter);
		
		//创建会话id查询所属的会话的说有短信
		String[] projection = new String[] {
				"_id",
				"body",
				"type",
				"date"
		};
		
		//异步查询短信
		String selection = "thread_id=" + thread_id;
		SimpleQueryHandler queryHandler = new SimpleQueryHandler(getContentResolver());
		queryHandler.startQuery(0, adapter, Constant.URI.URI_SMS, projection, selection, null, "date ASC");
	}

	
	/**
	 * 初始化标题栏
	 */
	// 单独将标题栏部分的内容提出来，标题栏部分需要显示姓名
	// 这个详情页面是从一个activity跳转过来的，跳转的时候要能够携带数据。
	private void initTitleBar() {
		TextView tv_title_bact_title = (TextView) findViewById(R.id.tv_title_bact_title);
		ImageView iv_titlebar_back_btn = (ImageView) findViewById(R.id.iv_titlebar_back_btn);
		
		iv_titlebar_back_btn.setOnClickListener(this);
		String name = ContactDao.getNameByAddress(this.getContentResolver(), address);
		tv_title_bact_title.setText(TextUtils.isEmpty(name) ? address : name);
	}

	@Override
	public void initListner() {
		bt_conversation_detail_send.setOnClickListener(this);
	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_titlebar_back_btn:
			finish();
			break;
		case R.id.bt_conversation_detail_send:
			String body = et_conversation_detail.getText().toString();
			
			if(!TextUtils.isEmpty(body)) {
				SmsDao.sendSms(this, body, address);
				et_conversation_detail.setText("");
			}
			break;
		default:
			break;
		}
	}

}
