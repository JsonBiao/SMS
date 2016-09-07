##ViewPager
* 平滑的切换界面
* 使用Adapter设置子节点，也就是条目
##selector
* 定义在drawable目录下的，可以直接作为背景
* 作用：在不同的情况下，显示不同ui（背景图片，文字颜色，文字大小）
##CursorAdapter
* 继承BaseAdapter
* 刷新listView，使用Adapter的notifyDataSetChanged方法
* 但是CursorAdapter会在数据库数据改变时，自动刷新listView
##查询会话
* 路径为conversations，查询内容提供者时，查询表、字段、条件都已经默认定义好

		select
		sms.body AS snippet, sms.thread_id AS thread_id, groups.msg_count AS msg_count
		from
		sms, (SELECT thread_id AS group_thread_id, MAX(date)AS group_date, COUNT(*) AS msg_count FROM sms GROUP BY thread_id) AS groups
		where
		sms.thread_id = groups.group_thread_id AND sms.date = groups.group_date

##AsyncQueryHandler
* 用于异步增删改查数据

##shape
* 定义在drawable目录下
* 可以定义一个形状
* 可以直接把shape文件指定为组件北京