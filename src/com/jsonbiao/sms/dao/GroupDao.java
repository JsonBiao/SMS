package com.jsonbiao.sms.dao;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import com.jsonbiao.sms.globle.Constant;

public class GroupDao {

	/**
	 * 插入群组
	 * 
	 * @param resolver
	 * @param text
	 */
	public static void insertGroup(ContentResolver resolver, String groupName) {
		ContentValues values = new ContentValues();
		values.put("name", groupName);
		values.put("create_date", System.currentTimeMillis());
		values.put("thread_count", 0);
		// 符合内容提供者给出的路径匹配规则，就可以将想要插入的数据插入到group表当中了
		resolver.insert(Constant.URI.URI_GROUP_INSERT, values);
	}

	/**
	 * 更新群组名
	 * 
	 * @param resolver
	 * @param groupName
	 * @param _id
	 */
	public static void updateGroupName(ContentResolver resolver,
			String groupName, int _id) {
		ContentValues values = new ContentValues();
		values.put("name", groupName);
		resolver.update(Constant.URI.URI_GROUP_UPDATE, values, "_id=" + _id,
				null);
	}

	/**
	 * 删除群组
	 * 
	 * @param resolver
	 * @param _id
	 *            群组_id
	 */
	public static void deleteGroup(ContentResolver resolver, int _id) {
		ThreadGroupDao.deleteThreadGroupByGroupId(resolver, _id);
		resolver.delete(Constant.URI.URI_GROUP_DELETE, "_id=" + _id, null);
	}

	/**
	 * 通过主键_id,获取组名字
	 * 
	 * @param resovler
	 * @param _id
	 * @return
	 */
	public static String getGroupNameByGroupId(ContentResolver resovler, int _id) {
		String name = null;
		Cursor cursor = resovler.query(Constant.URI.URI_GROUP_QUERY,
				new String[] { "name" }, "_id=" + _id, null, null);
		if (cursor.moveToFirst()) {
			name = cursor.getString(0);
		}
		return name;
	}

	/**
	 * 获取组中会话的数量
	 * 
	 * @param resolver
	 * @param _id
	 * @return
	 */
	public static int getThreadCount(ContentResolver resolver, int _id) {
		int thread_count = -1;
		Cursor cursor = resolver.query(Constant.URI.URI_GROUP_QUERY,
				new String[] { "thread_count" }, "_id=" + _id, null, null);
		if (cursor.moveToFirst()) {
			thread_count = cursor.getInt(0);
		}
		return thread_count;
	}

	/**
	 * 更新会话数量
	 * 
	 * @param resolver
	 * @param thread_count
	 * @param _id
	 */
	public static void updateThreadCount(ContentResolver resolver,
			int thread_count, int _id) {
		if (thread_count >= 0) {
			ContentValues values = new ContentValues();
			values.put("thread_count", thread_count);
			resolver.update(Constant.URI.URI_GROUP_UPDATE, values,
					"_id=" + _id, null);
		}
	}

}
