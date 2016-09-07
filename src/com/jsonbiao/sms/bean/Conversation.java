package com.jsonbiao.sms.bean;

import android.database.Cursor;

/**
 * @author admin
 *
 */
public class Conversation {
	private String snippet;
	private Integer thread_id;
	private String msg_count;
	private String address;
	private Long date;
	
	
	
	private Conversation() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static Conversation createFromCursor(Cursor cursor) {
		Conversation conversation = new Conversation();
//		"sms.body AS snippet",
//		"sms.thread_id AS _id",//必须有一个“_id”
//		"groups.msg_count AS msg_count",
//		"address AS address",
//		"date AS date"
		conversation.setSnippet(cursor.getString(cursor.getColumnIndex("snippet")));
		conversation.setThread_id(cursor.getInt(cursor.getColumnIndex("_id")));
		conversation.setMsg_count(cursor.getString(cursor.getColumnIndex("msg_count")));
		conversation.setAddress(cursor.getString(cursor.getColumnIndex("address")));
		conversation.setDate(cursor.getLong(cursor.getColumnIndex("date")));
		return conversation;
	}

	public String getSnippet() {
		return snippet;
	}

	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}

	public Integer getThread_id() {
		return thread_id;
	}

	public void setThread_id(Integer thread_id) {
		this.thread_id = thread_id;
	}

	public String getMsg_count() {
		return msg_count;
	}

	public void setMsg_count(String msg_count) {
		this.msg_count = msg_count;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Conversation [snippet=" + snippet + ", thread_id=" + thread_id
				+ ", msg_count=" + msg_count + ", address=" + address
				+ ", date=" + date + "]";
	}
	
	
	
}
