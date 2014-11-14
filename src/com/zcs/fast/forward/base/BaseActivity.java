package com.zcs.fast.forward.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public abstract class BaseActivity extends Activity implements OnClickListener {
	protected final static String TAG = "BaseActivity";

	/**
	 * 屏幕尺寸
	 */
	private int screenWidth;
	private int screenHeight;

	/**
	 * 标题栏View
	 */
	protected View titlebarView;
	protected LinearLayout titleBtnLeft;
	protected ImageView titleBtnRight;
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

	private static Toast mToast;
	private static Context context;

	protected void showToast(String text) {
		if (context == null) {
			context = this;
		}
		if (mToast == null) {
			mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(text);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.show();
	}

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
