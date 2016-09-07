package com.jsonbiao.sms.dao;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.jsonbiao.sms.globle.Constant;

public class ThreadGroupDao {
	/**
	 * ��ѯ�Ự�Ƿ���뵽��
	 * @param resolver
	 * @param thread_id
	 * @return
	 */
	public static boolean hasGroup(ContentResolver resolver,int thread_id) {
		Cursor cursor = resolver.query(Constant.URI.URI_THREAD_GROUP_QUERY, null, "thread_id=" + thread_id, null, null);
		return cursor.moveToNext();
	}
	
	/**
	 * ͨ��thread_id��ȡ��group_id
	 * @param reslover
	 * @param thread_id
	 * @return
	 */
	public static int getGroupIdByThreadId(ContentResolver reslover, int thread_id) {
		Cursor cursor = reslover.query(Constant.URI.URI_THREAD_GROUP_QUERY, new String[] {"group_id"}, "thread_id=" + thread_id, null, null);
		cursor.moveToFirst();
		return cursor.getInt(0);
	}
	
	
	/**
	 * ͨ��group_idɾ�����еĻỰ
	 * @param resolver
	 * @param group_id
	 */
	public static void deleteThreadGroupByGroupId(ContentResolver resolver, int group_id) {
		resolver.delete(Constant.URI.URI_THREAD_GROUP_DELETE, "group_id=" + group_id, null);
	}
	
	
	/**
	 * ��Ⱥ����ɾ���Ự
	 * @param resolver
	 * @param thread_id
	 * @param group_id
	 * @return
	 */
	public static boolean deleteThreadGroupByThreadId(ContentResolver resolver,int thread_id,int group_id) {
		int rownum = resolver.delete(Constant.URI.URI_THREAD_GROUP_DELETE, "thread_id=" + thread_id, null);
		if(rownum > 0) {
			int thread_count = GroupDao.getThreadCount(resolver, group_id);
			System.out.println("thread_count=" + thread_count);
			GroupDao.updateThreadCount(resolver, thread_count - 1, group_id);
		}
		return rownum > 0;
	}
	
	
	/**
	 * ���Ự���뵽Ⱥ��
	 * @param resolver
	 * @param thread_id
	 * @param group_id
	 * @return
	 */
	public static boolean insertThreadGroup(ContentResolver resolver, int thread_id,int group_id) {
		ContentValues values = new ContentValues();
		values.put("thread_id", thread_id);
		values.put("group_id", group_id);
		Uri uri = resolver.insert(Constant.URI.URI_THREAD_GROUP_INSERT, values);
		if(uri != null) {
			//����Ự�󣬸ı�Ⱥ��ĻỰ����
			int thread_count = GroupDao.getThreadCount(resolver, group_id);
			GroupDao.updateThreadCount(resolver, thread_count + 1, group_id);
		}
 		return uri != null;
	}
	
	
}
