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
	 * ��ʾɾ���ĶԻ���
	 * @param context
	 * @param maxProgress
	 * @return
	 */
	//������д�뾲̬������ֱ�ӻ�ȡ����Ķ���
	public static DeleteMsgDialog showDeleteDialog(Context context, int maxProgress, OnDeleteCancelListener onDeleteCancelListener) {
		DeleteMsgDialog dialog = new DeleteMsgDialog(context, maxProgress,onDeleteCancelListener);
		//���ｫ�Ὣ������dialog��ʾ��ҳ��֮�ϡ�
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
		tv_deletemsg_title.setText("����ɾ��(0/" + maxProgress + ")");
		//���������������ֵ
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
	 * ˢ�½�����
	 * @param progress
	 */
	public void updateProgressAndTitle(int progress) {
		tv_deletemsg_title.setText("����ɾ��(" + progress +"/" + maxProgress + ")");
		pb_deletemsg.setProgress(progress);
	}

}
