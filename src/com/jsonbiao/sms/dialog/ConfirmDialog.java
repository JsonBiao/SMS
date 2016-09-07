package com.jsonbiao.sms.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jsonbiao.sms.R;

public class ConfirmDialog extends BaseDialog {

	private String title;
	private String message;
	private TextView tv_dialog_title;
	private TextView tv_dialog_message;
	private Button bt_dialog_cancel;
	private Button bt_dialog_confirm;
	private OnConfirmListener onConfirmListener;
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
		this.onConfirmListener = onConfirmListener;
	}

	protected ConfirmDialog(Context context) {
		super(context);
	}
	
	public static void showDialog(Context context,String title,String message,OnConfirmListener confirmListener) {
		ConfirmDialog dialog = new ConfirmDialog(context);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setOnConfirmListener(confirmListener);
		dialog.show();
	}

	@Override
	public void initView() {
		//设置对话框显示的布局文件
		setContentView(R.layout.dialog_confirm);
		
		tv_dialog_title = (TextView) findViewById(R.id.tv_dialog_title);
		tv_dialog_message = (TextView) findViewById(R.id.tv_dialog_message);
		
		bt_dialog_cancel = (Button) findViewById(R.id.bt_dialog_cancel);
		bt_dialog_confirm = (Button) findViewById(R.id.bt_dialog_confirm);
	}

	@Override
	public void initData() {
		tv_dialog_title.setText(title);
		tv_dialog_message.setText(message);
	}

	@Override
	public void initListener() {
		bt_dialog_cancel.setOnClickListener(this);
		bt_dialog_confirm.setOnClickListener(this);
	}

	@Override
	public void processClick(View view) {
		switch (view.getId()) {
		case R.id.bt_dialog_cancel:
			if(null != onConfirmListener) {
				onConfirmListener.onCancle();
			}
			break;
		case R.id.bt_dialog_confirm:
			if(null != onConfirmListener) {
				onConfirmListener.onConfirm();
			}
			break;

		default:
			break;
		}
		//对话框消失
		dismiss();
	}
	
	public interface OnConfirmListener {
		void onCancle();
		void onConfirm();
	}
}
