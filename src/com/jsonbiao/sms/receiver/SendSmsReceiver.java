package com.jsonbiao.sms.receiver;

import com.jsonbiao.sms.utils.ToastUtils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SendSmsReceiver extends BroadcastReceiver {

	public static final String ACTION_SEND_SMS = "com.jsonbiao.sms.sendsms";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		//通过结果编码判断是否发送成功
		int code = getResultCode();
		if(code == Activity.RESULT_OK) {
			ToastUtils.showToast(context, "发送成功");
		}else {
			ToastUtils.showToast(context, "发送失败");
		}
	}

}
