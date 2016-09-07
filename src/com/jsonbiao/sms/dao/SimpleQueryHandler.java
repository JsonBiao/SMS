package com.jsonbiao.sms.dao;

import com.jsonbiao.sms.utils.CursorUtils;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.widget.CursorAdapter;

public class SimpleQueryHandler extends AsyncQueryHandler {

	public SimpleQueryHandler(ContentResolver cr) {
		super(cr);
	}

	
	//��ѯ���ʱ����
	//arg0��arg1��ѯ��ʼʱЯ��������
	//arg1:��ѯ�Ľ��
	@Override
	protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
		super.onQueryComplete(token, cookie, cursor);
		if(null != cookie && cookie instanceof CursorAdapter) {
			//��ѯ�õ���cursor������CursorAdapter,������Cursor��������ʾ��listView;
			CursorUtils.printCursor(cursor);
			((CursorAdapter)cookie).changeCursor(cursor);
//			while(cursor.moveToNext()) {
//				Conversation c = Conversation.createFromCursor(cursor);
//				System.out.println(c.toString());
//			}
		}
	}
	
}
