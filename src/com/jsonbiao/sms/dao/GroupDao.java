package com.jsonbiao.sms.dao;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import com.jsonbiao.sms.globle.Constant;

public class GroupDao {

	/**
	 * ����Ⱥ��
	 * 
	 * @param resolver
	 * @param text
	 */
	public static void insertGroup(ContentResolver resolver, String groupName) {
		ContentValues values = new ContentValues();
		values.put("name", groupName);
		values.put("create_date", System.currentTimeMillis());
		values.put("thread_count", 0);
		// ���������ṩ�߸�����·��ƥ����򣬾Ϳ��Խ���Ҫ��������ݲ��뵽group������
		resolver.insert(Constant.URI.URI_GROUP_INSERT, values);
	}

	/**
	 * ����Ⱥ����
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
	 * ɾ��Ⱥ��
	 * 
	 * @param resolver
	 * @param _id
	 *            Ⱥ��_id
	 */
	public static void deleteGroup(ContentResolver resolver, int _id) {
		ThreadGroupDao.deleteThreadGroupByGroupId(resolver, _id);
		resolver.delete(Constant.URI.URI_GROUP_DELETE, "_id=" + _id, null);
	}

	/**
	 * ͨ������_id,��ȡ������
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
	 * ��ȡ���лỰ������
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
	 * ���»Ự����
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
