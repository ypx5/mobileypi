package com.flashzhang.ypilibrary;


import android.os.Bundle;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;

public class MainActivity extends TabActivity
{  
	
	private TabHost tabHost;// 主界面的导航
    private TabWidget tabWidget;// 主界面底部的标签
    private String id;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);// 取出标题 必须先调用
		setContentView(R.layout.activity_main);
		Intent intent=getIntent();
		id=intent.getStringExtra("id");
		tabHost = (TabHost) this.findViewById(android.R.id.tabhost);// 找到对应的tabhost
		tabHost.setup();// 启动
		tabWidget = (TabWidget) this.findViewById(android.R.id.tabs);
		
		// 为tabhost添加选项
		Intent intent1 = new Intent(MainActivity.this, PersonnainfoActivity.class);
		intent1.putExtra("id",id);//通过intent传递id
		tabHost.addTab(tabHost
					   .newTabSpec("tag1")
					   .setContent(intent1)
					   .setIndicator(createTabView(R.drawable.bottom_personnalinfo_selected, "个人信息")));
		
		Intent intent2 =new Intent(MainActivity.this, BorrowinfoActivity.class);
		intent2.putExtra("id",id);
		tabHost.addTab(tabHost
				.newTabSpec("tag2")
				.setContent(intent2)
				.setIndicator(
						createTabView(R.drawable.bottom_borrowinginfo_normal, "当前借阅")));
		
		
		Intent intent4=new Intent(MainActivity.this,MarksinfoActivity.class);
		intent4.putExtra("id", id);
		tabHost.addTab(tabHost
				.newTabSpec("tag4")
				.setContent(intent4)
				.setIndicator(
						createTabView(R.drawable.bottom_marksinfo_normal, "成绩查询")));
		
		
		
		
		
		tabHost.addTab(tabHost
				.newTabSpec("tag3")
				.setContent(new Intent(MainActivity.this, MoreinfoActivity.class))
				.setIndicator(
						createTabView(R.drawable.bottom_moreinfo_normal, "更多功能")));
		
		tabHost.setCurrentTab(0);// 设置第0个为默认
		
		tabHost.setOnTabChangedListener(new OnTabChangeListener(){
			
			@Override
			public void onTabChanged(String tabId) 
			{
				if (tabId.equals("tag1")) {
					View view = tabWidget.getChildTabViewAt(0);
					view.findViewById(R.id.image_main_tab)
					.setBackgroundResource(
							R.drawable.bottom_personnalinfo_selected);
					
				} else {
					View view = tabWidget.getChildTabViewAt(0);
					view.findViewById(R.id.image_main_tab)
							.setBackgroundResource(
									R.drawable.bottom_personnalinfo_normal);
				}
				
				if (tabId.equals("tag2")) {
					View view = tabWidget.getChildTabViewAt(1);
					view.findViewById(R.id.image_main_tab)
					.setBackgroundResource(
							R.drawable.bottom_borrowinginfo_selected);
				} else {
					View view = tabWidget.getChildTabViewAt(1);
					view.findViewById(R.id.image_main_tab)
					.setBackgroundResource(
							R.drawable.bottom_borrowinginfo_normal);
				}
				
				if (tabId.equals("tag4")) {
					View view = tabWidget.getChildTabViewAt(2);
					view.findViewById(R.id.image_main_tab)
					.setBackgroundResource(
							R.drawable.bottom_marksinfo_selected);
				} else {
					View view = tabWidget.getChildTabViewAt(2);
					view.findViewById(R.id.image_main_tab)
					.setBackgroundResource(
							R.drawable.bottom_marksinfo_normal);
				}
				
				
				if (tabId.equals("tag3")) {
					View view = tabWidget.getChildTabViewAt(3);
					view.findViewById(R.id.image_main_tab)
					.setBackgroundResource(
							R.drawable.bottom_moreinfo_selected);
				} else {
					View view = tabWidget.getChildTabViewAt(3);
					view.findViewById(R.id.image_main_tab)
					.setBackgroundResource(
							R.drawable.bottom_moreinfo_normal);
				}
				
			}
		
		
		}
		);
		
		
	}
	
	private View createTabView(int res, String name) 
	{
		View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.maintab_itme, null);

		ImageView iv = (ImageView) view.findViewById(R.id.image_main_tab);
		TextView tv = (TextView) view.findViewById(R.id.tv_main_tab);
		iv.setBackgroundResource(res);
		tv.setText(name);
		return view;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
