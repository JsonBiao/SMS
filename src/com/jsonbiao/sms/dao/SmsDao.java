package com.jsonbiao.sms.dao;

import java.util.ArrayList;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import com.jsonbiao.sms.globle.Constant;
import com.jsonbiao.sms.receiver.SendSmsReceiver;


public class SmsDao {
	public static void sendSms(Context context,String body,String address) {
		SmsManager manager = SmsManager.getDefault();
		ArrayList<String> smss = manager.divideMessage(body);
		
		Intent intent = new Intent(SendSmsReceiver.ACTION_SEND_SMS);
		//���ŷ���ȥ��ϵͳ�ᷢ��һ���㲥����֪���Ƕ��ŷ��ͳɹ�������ʧ��
		PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
		for(String text:smss) {
			//���apiֻ���𷢶��ţ�����д�����ݿ�
			manager.sendTextMessage(address, null, text, sentIntent, null);
			//�Ѷ��Ų���������ݿ�
			insertSms(context,address,text);
		}

	}

	private static void insertSms(Context context, String address, String body) {
		//ֻҪ����address�Ϳ����ˣ�thread_id����Ҫ���룬���Զ�ʶ��ġ�
		ContentValues values = new ContentValues();
		values.put("address", address);
		values.put("body", body);
		values.put("type", Constant.SMS.TYPE_SEND);
		
		context.getContentResolver().insert(Constant.URI.URI_SMS, values);
	}
}
