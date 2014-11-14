package com.zcs.fast.forward.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.zcs.fast.forward.R;
import com.zcs.fast.forward.base.BaseFragmentActivity;
import com.zcs.fast.forward.base.MainListener;
import com.zcs.fast.forward.fragment.MainFragment;
import com.zcs.fast.forward.fragment.MenuFragment;
import com.zcs.fast.forward.fragment.TabFragment;
import com.zcs.fast.forward.utils.LogUtil;

/**
 * 主界面Activity
 * 
 * @author ZengCS
 * @since 2014年11月14日16:28:48
 */
@SuppressLint("HandlerLeak")
public class IndexActivity extends BaseFragmentActivity implements MainListener {
	private final static int HIDE_SPLASH_LAYOUT = 0;

	/** SlidingMenu */
	private SlidingMenu menu;

	/** Views */
	private View splashLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set the Above View
		setContentView(R.layout.fragment_index);

		// 设置内容Fragment(主)
		TabFragment.mainFragment = new MainFragment();
		TabFragment.currFragment = TabFragment.mainFragment;
		changeMainFragment(null, TabFragment.mainFragment);

		getSupportFragmentManager().beginTransaction().replace(R.id.tab_frame, new TabFragment()).commit();

		// configure the SlidingMenu
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		// menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		// menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		int offset = getScreenWidth() / 4;
		menu.setBehindOffset(offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu.setMenu(R.layout.menu_frame);
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new MenuFragment()).commit();

		super.init();
		// 延时隐藏Splash界面
		mHandler.sendEmptyMessageDelayed(HIDE_SPLASH_LAYOUT, 2000);
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case HIDE_SPLASH_LAYOUT:// 隐藏Splash界面
				Animation anim = new AlphaAnimation(1, 0);
				anim.setDuration(500);
				splashLayout.startAnimation(anim);
				splashLayout.setVisibility(View.GONE);
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void initTitlebar() {
		// TODO init
		super.titlebarView = findViewById(R.id.titlebar_main);

		super.titleBtnLeft = (ImageView) titlebarView.findViewById(R.id.titlebtn_left);
		super.titleBtnRight = (ImageView) titlebarView.findViewById(R.id.titlebtn_right);
		super.titleTxtCenter = (TextView) titlebarView.findViewById(R.id.titletxt_center);

		titleBtnLeft.setOnClickListener(this);
		titleBtnRight.setOnClickListener(this);

		LogUtil.d(TAG, "initTitlebar complete");
	}

	@Override
	protected void initComponent() {
		splashLayout = findViewById(R.id.splash_layout);
	}

	@Override
	public void showMenu() {
		menu.showMenu(true);
	}

	@Override
	public void exitApp() {
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.titlebtn_left:// TitleBar左侧按钮
			showMenu();
			break;
		case R.id.titlebtn_right:// TitleBar右侧按钮
			showToast("Click右侧按钮");
			break;
		default:
			break;
		}
	}

	@Override
	public void changeMainFragment(Fragment currFragment, Fragment targetFragment) {
		if (targetFragment == currFragment) {
			return;
		}
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();

		// 先判断是否被add过
		if (targetFragment.isAdded()) {
			// 隐藏当前的fragment，显示下一个
			if (currFragment != null) {
				transaction.hide(currFragment);
			}
			transaction.show(targetFragment);
		} else {
			// 隐藏当前的fragment，add下一个到Activity中
			if (currFragment != null) {
				transaction.hide(currFragment);
			}
			transaction.add(R.id.content_frame, targetFragment);
		}
		transaction.commit();
	}

	@Override
	public void changeMainFragment(Fragment targetFragment) {
	}

	@Override
	public void doPopBackStack() {

	}

	@Override
	public void showToastSingle(String text) {

	}

	private static Toast mToast;
	private static Context context;

	@Override
	public void showToast(String text) {
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

	@Override
	public void onBackPressed() {
		if (menu.isMenuShowing()) {
			menu.showContent();
		} else {
			super.onBackPressed();
		}
	}
}
