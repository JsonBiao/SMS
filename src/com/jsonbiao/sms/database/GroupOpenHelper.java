package com.jsonbiao.sms.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class GroupOpenHelper extends SQLiteOpenHelper {
	//单例模式获取GroupOpenHelper实例，防止并发访问数据库
	private static GroupOpenHelper instance = null;

	private GroupOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public static GroupOpenHelper getInstance(Context context) {
		if(null == instance) {
			instance = new GroupOpenHelper(context,"group.db",null,1);
		}
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//创建group表
		db.execSQL("CREATE TABLE groups(" +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"name VARCHAR," +
				"create_date INTEGER," +
				"thread_count INTEGER" +
				")");
		//创建会话和群组映射表
		db.execSQL("CREATE TABLE thread_group(" +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"group_id INTEGER REFERENCES groups(_id)," +
				"thread_id INTEGER" +
				")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
