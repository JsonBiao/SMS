package com.jsonbiao.sms.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.jsonbiao.sms.database.GroupOpenHelper;

public class GroupProvider extends ContentProvider {
	
	public static final String AUTHORITY = "com.jsonbiao.sms";
	public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);
	
	private static final int CODE_GROUPS_INSERT = 0;
	private static final int CODE_GROUPS_QUERY = 1;
	private static final int CODE_GROUPS_UPDATE = 2;
	private static final int CODE_GROUPS_DELETE = 4;
	private static final int CODE_THREAD_GROUP_INSERT = 5;
	private static final int CODE_THREAD_GROUP_QUERY = 6;
	private static final int CODE_THREAD_GROUP_UPDATE = 7;
	private static final int CODE_THREAD_GROUP_DELETE = 8;
	
	private static final String TABLE_GROUPS  = "groups";
	private static final String TABLE_THREAD_GROUP = "thread_group";
	
	
	private UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

	private GroupOpenHelper helper;
	private SQLiteDatabase db;
	{
		//���ƥ�����
		matcher.addURI(AUTHORITY, "groups/insert", CODE_GROUPS_INSERT);
		matcher.addURI(AUTHORITY, "groups/query", CODE_GROUPS_QUERY);
		matcher.addURI(AUTHORITY, "groups/update", CODE_GROUPS_UPDATE);
		matcher.addURI(AUTHORITY, "groups/delete", CODE_GROUPS_DELETE);
		matcher.addURI(AUTHORITY, "thread_group/insert", CODE_THREAD_GROUP_INSERT);
		matcher.addURI(AUTHORITY, "thread_group/query", CODE_THREAD_GROUP_QUERY);
		matcher.addURI(AUTHORITY, "thread_group/update", CODE_THREAD_GROUP_UPDATE);
		matcher.addURI(AUTHORITY, "thread_group/delete", CODE_THREAD_GROUP_DELETE);
	}
	
	@Override
	public boolean onCreate() {
		helper = GroupOpenHelper.getInstance(getContext());
		db = helper.getWritableDatabase();
		//����true�����ṩ�߲Żᱻ����
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor cursor = null;
		switch (matcher.match(uri)) {
		//����Ǳ�ʾ��ͬ�ı�����id�жϳ���ͬ�ı�Ȼ��ѡ����롣
		case CODE_GROUPS_QUERY:			
			cursor = db.query(TABLE_GROUPS, projection, selection, selectionArgs, null, null, sortOrder);
			//����uri�����ݸı�һ�������ݹ۲���
			//ֻҪ��uri�ϵ����ݸı䣬���ݹ۲��߻����̷��֣����²�ѯ
			cursor.setNotificationUri(getContext().getContentResolver(), AUTHORITY_URI);
			break;
		case CODE_THREAD_GROUP_QUERY:
			cursor = db.query(TABLE_THREAD_GROUP, projection, selection, selectionArgs, null, null, sortOrder);
			cursor.setNotificationUri(getContext().getContentResolver(), AUTHORITY_URI);
			break;
		default:
			throw new IllegalArgumentException("δʶ���uri:" + uri);
		}
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		switch (matcher.match(uri)) {
		case CODE_GROUPS_INSERT:
			long rawId = db.insert(TABLE_GROUPS, null, values);
			//����ʧ��
			if(-1 == rawId) {
				return null;
			}else {
				//������Լ����������ݿ�ֻ�ܵ���notifychange�����������æˢ�¡�
				getContext().getContentResolver().notifyChange(AUTHORITY_URI, null);
				//�ѷ��ص���id��ƴ����uri�ĺ��棬Ȼ�󷵻�
				return ContentUris.withAppendedId(uri, rawId);
			}
		case CODE_THREAD_GROUP_INSERT:
			rawId = db.insert(TABLE_THREAD_GROUP, null, values);
			//����ʧ��
			if(-1 == rawId) {
				return null;
			}else {
				//������Լ����������ݿ�ֻ�ܵ���notifychange�����������æˢ�¡�
				getContext().getContentResolver().notifyChange(AUTHORITY_URI, null);
				//�ѷ��ص���id��ƴ����uri�ĺ��棬Ȼ�󷵻�
				return ContentUris.withAppendedId(uri, rawId);
			}
		default:
			throw new IllegalArgumentException("δʶ���uri:" + uri);		
		}		
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int raw = 0;
		switch (matcher.match(uri)) {
		case CODE_GROUPS_DELETE:
			raw = db.delete(TABLE_GROUPS, selection, selectionArgs);
			getContext().getContentResolver().notifyChange(AUTHORITY_URI, null);
			break;
		case CODE_THREAD_GROUP_DELETE:
			raw = db.delete(TABLE_THREAD_GROUP, selection, selectionArgs);
			getContext().getContentResolver().notifyChange(AUTHORITY_URI, null);
			break;
		default:
			throw new IllegalArgumentException("δʶ���uri:" + uri);
		}
		return raw;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int rows = 0;
		
		switch (matcher.match(uri)) {
		case CODE_GROUPS_UPDATE:
			rows = db.update(TABLE_GROUPS, values, selection, selectionArgs);
			getContext().getContentResolver().notifyChange(AUTHORITY_URI, null);
			break;
		case CODE_THREAD_GROUP_UPDATE:
			rows = db.update(TABLE_THREAD_GROUP, values, selection, selectionArgs);
			getContext().getContentResolver().notifyChange(AUTHORITY_URI, null);
			break;
		default:
			throw new IllegalArgumentException("δʶ���uri:" + uri);
		}
		return rows;
	}
	
}
