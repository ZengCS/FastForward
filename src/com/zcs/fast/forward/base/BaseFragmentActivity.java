package com.zcs.fast.forward.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class BaseFragmentActivity extends FragmentActivity implements OnClickListener {
	protected final static String TAG = "BaseFragmentActivity";

	private int screenWidth;
	private int screenHeight;
	
	/**
	 * 标题栏View
	 */
	protected View titlebarView;
	protected ImageView titleBtnLeft, titleBtnRight;
	protected TextView titleTxtCenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		screenWidth = mDisplayMetrics.widthPixels;
		screenHeight = mDisplayMetrics.heightPixels;
	}
	
	/**
	 * 初始化布局,内部调用initTitlebar()和initComponent()
	 */
	protected void init() {
		initTitlebar();
		initComponent();
	}
	
	/**
	 * 初始化Titlebar
	 */
	protected abstract void initTitlebar();

	/**
	 * 初始化组件
	 */
	protected abstract void initComponent();

	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}
}
