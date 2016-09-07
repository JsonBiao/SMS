package com.jsonbiao.sms.utils;

import android.database.Cursor;

public class CursorUtils {
	public static void printCursor(Cursor cursor) {
		if(null == cursor) {
			System.out.println("cursorΪnull");
			return;
		}
		LogUtils.i(cursor,"һ����" + cursor.getCount() + "������");
		while(cursor.moveToNext()) {
			for(int i=0;i < cursor.getColumnCount();i++) {
				String name = cursor.getColumnName(i);
				String content = cursor.getString(i);
				LogUtils.i(cursor, name+":" + content);
				
			}
			
			LogUtils.i(cursor, "=======================");
		}
	}
}
