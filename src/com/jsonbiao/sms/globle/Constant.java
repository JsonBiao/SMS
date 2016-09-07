package com.jsonbiao.sms.globle;

import com.jsonbiao.sms.provider.GroupProvider;

import android.net.Uri;

public class Constant {
	public interface URI {
		Uri URI_SMS_CONVERSATION = Uri.parse("content://sms/conversations");
		Uri URI_SMS = Uri.parse("content://sms");
		
		Uri URI_GROUP_INSERT = Uri.withAppendedPath(GroupProvider.AUTHORITY_URI, "groups/insert");
		Uri URI_GROUP_QUERY = Uri.withAppendedPath(GroupProvider.AUTHORITY_URI, "groups/query");		
		Uri URI_GROUP_UPDATE = Uri.withAppendedPath(GroupProvider.AUTHORITY_URI, "groups/update");
		Uri URI_GROUP_DELETE = Uri.withAppendedPath(GroupProvider.AUTHORITY_URI, "groups/delete");
		Uri URI_THREAD_GROUP_INSERT = Uri.withAppendedPath(GroupProvider.AUTHORITY_URI, "thread_group/insert");
		Uri URI_THREAD_GROUP_QUERY = Uri.withAppendedPath(GroupProvider.AUTHORITY_URI, "thread_group/query");
		Uri URI_THREAD_GROUP_UPDATE = Uri.withAppendedPath(GroupProvider.AUTHORITY_URI, "thread_group/update");
		Uri URI_THREAD_GROUP_DELETE = Uri.withAppendedPath(GroupProvider.AUTHORITY_URI, "thread_group/delete");
	}
	
	public interface SMS {
		int TYPE_RECEIVE = 1;
		int TYPE_SEND = 2;
	}
}
