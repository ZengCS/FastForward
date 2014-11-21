package com.zcs.fast.forward.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
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
import com.zcs.fast.forward.utils.dialog.DialogParam;
import com.zcs.fast.forward.utils.dialog.DialogUtil;

/**
 * 主界面Activity
 * 
 * @author ZengCS
 * @since 2014年11月14日16:28:48
 */
@SuppressLint("HandlerLeak")
public class IndexActivity extends BaseFragmentActivity implements MainListener {
	private final static int HIDE_SPLASH_LAYOUT = 0;
	private long lastExitTime = 0;
	private boolean isSplashFinish = false;// 是否已经初始化完成
	/** Dialogs */
	private Dialog confirmDialog;

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
		this.initConfirmDialog();
		// 延时隐藏Splash界面
		mHandler.sendEmptyMessageDelayed(HIDE_SPLASH_LAYOUT, 1500);
	}

	private void initConfirmDialog() {
		DialogParam param = new DialogParam();
		param.setContext(IndexActivity.this);
		param.setCancelable(true);
		param.setTitle(getString(R.string.txt_alertdialog_title));
		param.setMsg(getString(R.string.exit_confirm_msg));
		param.setOkBtnStr(getString(R.string.mz_btn_ok));
		param.setOkBtnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				confirmDialog.dismiss();
				finish();
			}
		});
		param.setCancelBtnStr(getString(R.string.mz_btn_cancel));
		param.setCancelBtnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				confirmDialog.dismiss();
			}
		});
		confirmDialog = DialogUtil.createConfirmDialog(param);
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case HIDE_SPLASH_LAYOUT:// 隐藏Splash界面
				Animation mAlphaanim = new AlphaAnimation(1, 0);
				mAlphaanim.setDuration(500);
				splashLayout.startAnimation(mAlphaanim);
				splashLayout.setVisibility(View.GONE);
				mAlphaanim.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						isSplashFinish = true;
					}
				});
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void initTitlebar() {
		// TODO 初始化Title栏
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
		splashLayout.setBackgroundResource(R.drawable.splash_repeat_bg);
	}

	@Override
	public void showMenu() {
		menu.showMenu(true);
	}

	@Override
	public void hideMenu() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				menu.showContent(false);
			}
		}, 300);
	}

	@Override
	public void exitApp() {
		confirmDialog.show();
		// finish();
	}

	@Override
	public void onClick(View v) {
		if (!isSplashFinish) {
			return;
		}
		switch (v.getId()) {
		case R.id.titlebtn_left:// TitleBar左侧按钮
			showMenu();
			break;
		case R.id.titlebtn_right:// TitleBar右侧按钮
			// showToast("Click右侧按钮");
			startActivity(new Intent(IndexActivity.this, SearchActivity.class));
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
		}
		mToast.show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (!isSplashFinish) {
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// TODO 监控、拦截、屏蔽返回键
			if (menu.isMenuShowing()) {
				menu.showContent();
			} else {
				if ((System.currentTimeMillis() - lastExitTime) < 2000) {
					mToast.cancel();
					super.onBackPressed();
				} else {
					lastExitTime = System.currentTimeMillis();
					showToast("再按一次退出！");
				}
			}
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			// TODO 监控、拦截、屏蔽菜单键
			if (menu.isMenuShowing()) {
				menu.showContent();
			} else {
				menu.showMenu();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void changeActivity(Class<?> cls) {
		if (cls == null) {
			showToast("目标Activity为空！");
			return;
		}
		startActivity(new Intent(this, cls));
	}
}
