package com.jsonbiao.sms.ui.activity;


import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jsonbiao.sms.R;
import com.jsonbiao.sms.adapter.MainPagerAdapter;
import com.jsonbiao.sms.base.BaseActivity;
import com.jsonbiao.sms.ui.fragment.ConversationFragment;
import com.jsonbiao.sms.ui.fragment.GroupFragment;
import com.jsonbiao.sms.ui.fragment.SearchFragment;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class MainActivity extends BaseActivity {

	
	
	private ViewPager viewPager;
	private TextView tv_tab_conversation;
	private TextView tv_tab_group;
	private TextView tv_tab_search;
	private LinearLayout ll_tab_conversation;
	private LinearLayout ll_tab_group;
	private LinearLayout ll_tab_search;
	private View v_indicate_line;

	@Override
	public void initView() {
		setContentView(R.layout.activity_main);		
		//获取布局文件中的组件
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		
		tv_tab_conversation = (TextView) findViewById(R.id.tv_tab_conversation);
		tv_tab_group = (TextView) findViewById(R.id.tv_tab_group);
		tv_tab_search = (TextView) findViewById(R.id.tv_tab_search);
		
		ll_tab_conversation = (LinearLayout) findViewById(R.id.ll_tab_conversation);
		ll_tab_group = (LinearLayout) findViewById(R.id.ll_tab_group);
		ll_tab_search = (LinearLayout) findViewById(R.id.ll_tab_search);
		
		
		
	}
	
	@Override
	public void initListner() {
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			/*
			 * 页面选择
			 */
			@Override
			public void onPageSelected(int position) {
				textLightAndScale();
			}

			/*
			 * 页面滚动的地方
			 */
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				//System.out.println(position + ":" + positionOffset + ":" + positionOffsetPixels);
				ViewPropertyAnimator.animate(v_indicate_line).translationX(position * v_indicate_line.getWidth() + positionOffsetPixels / 3).setDuration(0);
			}
			
			/*
			 * 页面滚动状态
			 */
			@Override
			public void onPageScrollStateChanged(int state) {
				
			}
		});
		
		
		//设置监听事件
		ll_tab_conversation.setOnClickListener(this);
		ll_tab_group.setOnClickListener(this);
		ll_tab_search.setOnClickListener(this);
		
		v_indicate_line = findViewById(R.id.v_indicate_line);
		
	}

	@Override
	public void initData() {
		//创建Fragment对象存入集合
		List<Fragment> fragments = new ArrayList<Fragment>();
		ConversationFragment fragment1 = new ConversationFragment();
		fragments.add(fragment1);
		GroupFragment fragment2 = new GroupFragment();
		fragments.add(fragment2);
		SearchFragment fragment3 = new SearchFragment();
		fragments.add(fragment3);	
		//设置页面视图适配
		viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), fragments));
		
		//高亮显示文本内容
		textLightAndScale();
		
		//设置红线的宽度
		computeIndicateLineWidth();
		
		
	}

	private void computeIndicateLineWidth() {
		@SuppressWarnings("deprecation")
		int width = getWindowManager().getDefaultDisplay().getWidth();
		v_indicate_line.getLayoutParams().width = width / 3;
	}

	

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.ll_tab_conversation:
			viewPager.setCurrentItem(0);
			break;
		case R.id.ll_tab_group:
			viewPager.setCurrentItem(1);
			break;
		case R.id.ll_tab_search:
			viewPager.setCurrentItem(2);
			break;

		default:
			break;
		}
	}

	private void textLightAndScale() {
		int item = viewPager.getCurrentItem();
		tv_tab_conversation.setTextColor(0 == item ? Color.WHITE : 0xaa666666);
		tv_tab_group.setTextColor(1 == item ? Color.WHITE : 0xaa666666);
		tv_tab_search.setTextColor(2 == item ? Color.WHITE : 0xaa666666);
		
		//							要操作的对象					缩放比例	
		ViewPropertyAnimator.animate(tv_tab_conversation).scaleX(0 == item ? 1.2f: 1f).setDuration(200);
		ViewPropertyAnimator.animate(tv_tab_group).scaleX(1 == item ? 1.2f: 1f).setDuration(200);
		ViewPropertyAnimator.animate(tv_tab_search).scaleX(2 == item ? 1.2f: 1f).setDuration(200);
		ViewPropertyAnimator.animate(tv_tab_conversation).scaleY(0 == item ? 1.2f: 1f).setDuration(200);
		ViewPropertyAnimator.animate(tv_tab_group).scaleY(1 == item ? 1.2f: 1f).setDuration(200);
		ViewPropertyAnimator.animate(tv_tab_search).scaleY(2 == item ? 1.2f: 1f).setDuration(200);
	}
    
}
