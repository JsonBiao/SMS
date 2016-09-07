package com.jsonbiao.sms.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jsonbiao.sms.R;

public class DeleteMsgDialog extends BaseDialog {

	private TextView tv_deletemsg_title;
	private ProgressBar pb_deletemsg;
	private Button bt_deletesmg_cancle;

	private OnDeleteCancelListener onDeleteCancelListener;
	
	private int maxProgress;
	protected DeleteMsgDialog(Context context, int maxProgress, OnDeleteCancelListener onDeleteCancelListener) {
		super(context);
		this.maxProgress = maxProgress;
		this.onDeleteCancelListener = onDeleteCancelListener;
	}
	
	/**
	 * 显示删除的对话框
	 * @param context
	 * @param maxProgress
	 * @return
	 */
	//在类中写入静态方法，直接获取该类的对象
	public static DeleteMsgDialog showDeleteDialog(Context context, int maxProgress, OnDeleteCancelListener onDeleteCancelListener) {
		DeleteMsgDialog dialog = new DeleteMsgDialog(context, maxProgress,onDeleteCancelListener);
		//这里将会将常见的dialog显示在页面之上。
		dialog.show();
		return dialog;
	}

	@Override
	public void initView() {
		setContentView(R.layout.dialog_delete);
		
		tv_deletemsg_title = (TextView) findViewById(R.id.tv_deletemsg_title);
		pb_deletemsg = (ProgressBar) findViewById(R.id.pb_deletemsg);
		bt_deletesmg_cancle = (Button) findViewById(R.id.bt_deletesmg_cancle);
	}

	@Override
	public void initData() {
		tv_deletemsg_title.setText("正在删除(0/" + maxProgress + ")");
		//给进度条设置最大值
		pb_deletemsg.setMax(maxProgress);
	}

	@Override
	public void initListener() {
		bt_deletesmg_cancle.setOnClickListener(this);
	}

	@Override
	public void processClick(View view) {
		switch (view.getId()) {
		case R.id.bt_deletesmg_cancle:
			if(null != onDeleteCancelListener) {
				onDeleteCancelListener.onCancel();
			}
			dismiss();
			break;

		default:
			break;
		}
	}
	
	public interface OnDeleteCancelListener {
		void onCancel();
	}
	
	/**
	 * 刷新进度条
	 * @param progress
	 */
	public void updateProgressAndTitle(int progress) {
		tv_deletemsg_title.setText("正在删除(" + progress +"/" + maxProgress + ")");
		pb_deletemsg.setProgress(progress);
	}

}
