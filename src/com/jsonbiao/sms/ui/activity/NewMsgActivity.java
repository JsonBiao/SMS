package com.jsonbiao.sms.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsonbiao.sms.R;
import com.jsonbiao.sms.adapter.AutoSearchAdapter;
import com.jsonbiao.sms.base.BaseActivity;
import com.jsonbiao.sms.dao.SmsDao;
import com.jsonbiao.sms.utils.CursorUtils;
import com.jsonbiao.sms.utils.ToastUtils;

public class NewMsgActivity extends BaseActivity {

	private ImageView iv_newmsg_select_contact;
	private EditText et_newmsg_body;
	private Button bt_newmsg_send;
	private AutoCompleteTextView actv_newmsg_address;

	@Override
	public void initView() {
		setContentView(R.layout.activity_newmsg);

		iv_newmsg_select_contact = (ImageView) findViewById(R.id.iv_newmsg_select_contact);
		et_newmsg_body = (EditText) findViewById(R.id.et_newmsg_body);
		bt_newmsg_send = (Button) findViewById(R.id.bt_newmsg_send);

		actv_newmsg_address = (AutoCompleteTextView) findViewById(R.id.actv_newmsg_address);
		actv_newmsg_address
				.setDropDownBackgroundResource(R.drawable.bg_btn_normal);
		actv_newmsg_address.setDropDownVerticalOffset(5);
	}

	@Override
	public void initData() {
		AutoSearchAdapter adapter = new AutoSearchAdapter(this, null);
		// 给输入框设置adapter,改adapter负责显示数据框的下拉列表
		actv_newmsg_address.setAdapter(adapter);

		adapter.setFilterQueryProvider(new FilterQueryProvider() {
			// 这个方法的调用，用来执行查询
			// constraint:用户在输入框中输入的号码，也就是模糊查询条件
			@Override
			public Cursor runQuery(CharSequence constraint) {
				String[] projection = new String[] { "data1", "display_name",
						"_id" };
				// 模糊查询条件
				String selection = "data1 like '%" + constraint + "%'";
				Cursor cursor = getContentResolver().query(Phone.CONTENT_URI,
						projection, selection, null, null);
				// 返回cursor，就是把cursor交给adapter
				return cursor;
			}
		});
		initTitleBar();
	}

	@Override
	public void initListner() {
		iv_newmsg_select_contact.setOnClickListener(this);
		bt_newmsg_send.setOnClickListener(this);
	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_titlebar_back_btn:
			finish();
			break;
		case R.id.iv_newmsg_select_contact:
			// 跳转至系统提供的联系人选择Activity
			Intent intent = new Intent(Intent.ACTION_PICK);
			intent.setType("vnd.android.cursor.dir/contact");// 通讯录
			// 使用startActivityForResult启动，那么选好联系人，改Activity销毁，回调onActivityResult
			startActivityForResult(intent, 0);
			break;
		case R.id.bt_newmsg_send:
			String address = actv_newmsg_address.getText().toString();
			String body = et_newmsg_body.getText().toString();
			if (!TextUtils.isEmpty(body) && !TextUtils.isEmpty(address)) {
				SmsDao.sendSms(this, body, address);
			}
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		// 如果没有选择联系人，则返回的数据为空,不用刷新UI
		if (null == data) {
			return;
		}
		Uri uri = data.getData();
		if (null != uri) {
			// 查询这个Uri，获取联系人的_id和是否有号码
			String[] projection = new String[] { "_id", "has_phone_number" };
			// 不需要where条件，因为Uri是"一个"联系人的Uri
			Cursor cursor = getContentResolver().query(uri, projection, null,
					null, null);

			CursorUtils.printCursor(cursor);

			// 不需要判断是否查到，但必须移动指针
			cursor.moveToFirst();
			String _id = cursor.getString(0);
			int has_phone_number = cursor.getInt(1);

			if (0 == has_phone_number) {
				ToastUtils.showToast(this, "该联系人没有号码");
			} else {
				// 如果有电话号码，拿着联系人_id去Phone.CONTENT_URI查询号码
				String selection = "contact_id=" + _id;
				cursor = getContentResolver().query(Phone.CONTENT_URI,
						new String[] { "data1" }, selection, null, null);
				cursor.moveToFirst();
				String data1 = cursor.getString(0);
				actv_newmsg_address.setText(data1);
				// 内容获得焦点
				et_newmsg_body.requestFocus();
			}
		}

	}

	private void initTitleBar() {
		findViewById(R.id.iv_titlebar_back_btn).setOnClickListener(this);
		((TextView) findViewById(R.id.tv_title_bact_title)).setText("发送短信");
	}

}
