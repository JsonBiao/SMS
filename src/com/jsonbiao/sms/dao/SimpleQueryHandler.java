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

	
	//查询完毕时调用
	//arg0、arg1查询开始时携带的数据
	//arg1:查询的结果
	@Override
	protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
		super.onQueryComplete(token, cookie, cursor);
		if(null != cookie && cookie instanceof CursorAdapter) {
			//查询得到的cursor，交给CursorAdapter,由它把Cursor的内容显示至listView;
			CursorUtils.printCursor(cursor);
			((CursorAdapter)cookie).changeCursor(cursor);
//			while(cursor.moveToNext()) {
//				Conversation c = Conversation.createFromCursor(cursor);
//				System.out.println(c.toString());
//			}
		}
	}
	
}
