package com.zcs.fast.forward.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import com.zcs.fast.forward.R;
import com.zcs.fast.forward.base.BaseActivity;
import com.zcs.fast.forward.utils.DisplayUtil;
import com.zcs.fast.forward.utils.LogUtil;

public class ImageSwitcherActivity extends BaseActivity implements ViewFactory, OnTouchListener {
	/** constant */
	private static final String CURR_TITLE = "Android焦点图";

	protected static final int UPDATE_ANIMATION_STATE = 0x1f0001;// 更新动画状态
	protected static final int AUTO_SCROLL = 0x1f0002;// 自动滚动

	protected static final int SHOW_PRE_PIC = 0;// 显示上一张图片
	protected static final int SHOW_NEXT_PIC = 1;// 显示下一张图片

	protected static final int ANIM_TYPE_SLIDE = 0;
	protected static final int ANIM_TYPE_PUSH = 1;
	protected static final int ANIM_TYPE_ZOOM = 2;
	private int currAnimType = ANIM_TYPE_SLIDE;

	/** Views */
	private ImageSwitcher imageSwicher;
	private View[] circles;
	private boolean isAnimation = false;

	private Button btnSlide, btnPush, btnZoom;

	// 图片数组
	private int[] arrayPictures = { R.drawable.bg1, R.drawable.bg2, R.drawable.bg3, R.drawable.bg4 };
	// 要显示的图片在图片数组中的Index
	private int pictureIndex;
	// 左右滑动时手指按下的X坐标
	private float touchDownX, touchDownY;
	// 左右滑动时手指松开的X坐标
	private float touchUpX, touchUpY;
	private long downTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_switcher);
		super.init();

		imageSwicher = (ImageSwitcher) findViewById(R.id.imageSwicher);

		// 为ImageSwicher设置Factory，用来为ImageSwicher制造ImageView
		imageSwicher.setFactory(this);
		// 设置ImageSwitcher左右滑动事件
		imageSwicher.setOnTouchListener(this);

		initCircles();
	}

	@Override
	protected void onStart() {
		super.onStart();
		// 开启自动滚动
		startAutoScroll();
	}

	/**
	 * 初始化小圆点
	 */
	private void initCircles() {
		LinearLayout circle_layout = (LinearLayout) findViewById(R.id.circle_layout);

		int size = DisplayUtil.dip2px(this, 8);
		int margin = DisplayUtil.dip2px(this, 3);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(size, size);
		lp.setMargins(margin, 0, margin, 0);
		circles = new View[arrayPictures.length];

		for (int i = 0; i < arrayPictures.length; i++) {
			View circle = new View(this);
			circle.setLayoutParams(lp);
			if (i == 0) {
				circle.setBackgroundResource(R.drawable.circle_white);
			} else {
				circle.setBackgroundResource(R.drawable.circle_gray);
			}
			circle_layout.addView(circle);
			circles[i] = circle;
		}
	}

	/**
	 * 改变当前高亮小圆点
	 */
	private void updateCircleState() {
		isAnimation = true;
		// 延时更新圆点状态,是为了让图片动画执行完成
		mHandler.removeMessages(UPDATE_ANIMATION_STATE);
		mHandler.sendEmptyMessageDelayed(UPDATE_ANIMATION_STATE, getResources().getInteger(R.integer.anim_fast));

		// 再次开启自动滚动
		startAutoScroll();
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case UPDATE_ANIMATION_STATE:// 更新动画状态
				for (int i = 0; i < circles.length; i++) {
					if (i == pictureIndex) {
						circles[i].setBackgroundResource(R.drawable.circle_white);
					} else {
						circles[i].setBackgroundResource(R.drawable.circle_gray);
					}
				}
				isAnimation = false;
				break;
			case AUTO_SCROLL:// 自动滚动
				showPicture(SHOW_NEXT_PIC);

				startAutoScroll();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.titlebtn_left_act:
			finish();
			break;
		case R.id.titlebtn_right_act:// 查看
			displayHelpDialog();
			break;
		// TODO ****** 切换动画类型-Start ******
		case R.id.anim_slide:
			currAnimType = ANIM_TYPE_SLIDE;
			showPicture(SHOW_NEXT_PIC);

			btnSlide.setEnabled(false);
			btnPush.setEnabled(true);
			btnZoom.setEnabled(true);
			break;
		case R.id.anim_push:
			currAnimType = ANIM_TYPE_PUSH;
			showPicture(SHOW_NEXT_PIC);

			btnSlide.setEnabled(true);
			btnPush.setEnabled(false);
			btnZoom.setEnabled(true);
			break;
		case R.id.anim_zoom:
			currAnimType = ANIM_TYPE_ZOOM;
			showPicture(SHOW_NEXT_PIC);

			btnSlide.setEnabled(true);
			btnPush.setEnabled(true);
			btnZoom.setEnabled(false);
			break;
		// TODO ****** 切换动画类型-End ******
		default:
			break;
		}
	}

	@Override
	protected void initTitlebar() {
		// TODO 初始化标题栏
		super.titlebarView = findViewById(R.id.titlebar_image_switcher);

		super.titleBtnLeft = (LinearLayout) titlebarView.findViewById(R.id.titlebtn_left_act);
		super.titleBtnRight = (ImageView) titlebarView.findViewById(R.id.titlebtn_right_act);
		super.titleTxtCenter = (TextView) titlebarView.findViewById(R.id.titletxt_center_act);

		super.titleTxtCenter.setText(CURR_TITLE);

		titleBtnLeft.setOnClickListener(this);
		titleBtnRight.setOnClickListener(this);

		LogUtil.d(TAG, "initTitlebar complete");
	}

	@Override
	protected void initComponent() {
		// TODO 初始化组件
		btnSlide = (Button) findViewById(R.id.anim_slide);
		btnPush = (Button) findViewById(R.id.anim_push);
		btnZoom = (Button) findViewById(R.id.anim_zoom);

		btnSlide.setOnClickListener(this);
		btnPush.setOnClickListener(this);
		btnZoom.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mHandler.removeMessages(UPDATE_ANIMATION_STATE);
		mHandler.removeMessages(AUTO_SCROLL);
	}

	private boolean isMoving = false;

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (isAnimation) {
			// 如果当前正在切换图片,禁止再次拖动图片
			return false;
		}
		boolean isResponse = false;

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			LogUtil.d(TAG, "***************ACTION_DOWN");
			// 禁止父类View滚动
			v.getParent().requestDisallowInterceptTouchEvent(true);

			// 取得左右滑动时手指按下的X坐标
			touchDownX = event.getX();
			touchDownY = event.getY();
			// 记录触摸时间
			downTime = System.currentTimeMillis();
			// 手动滑的时候,暂停自动滚动
			mHandler.removeMessages(AUTO_SCROLL);
			isResponse = true;
			break;
		case MotionEvent.ACTION_MOVE:
			if (!isMoving && (event.getX() != touchDownX || event.getY() != touchDownY)) {
				LogUtil.d(TAG, "***************ACTION_MOVE");
				float mx = event.getX();
				float my = event.getY();
				LogUtil.d(TAG, "mx:" + mx);
				LogUtil.d(TAG, "touchDownX:" + touchDownX);

				LogUtil.d(TAG, "my:" + my);
				LogUtil.d(TAG, "touchDownY:" + touchDownY);
				isMoving = true;

				// 开启父类View滚动
				v.getParent().requestDisallowInterceptTouchEvent(false);
			}
			break;
		case MotionEvent.ACTION_UP:
			LogUtil.d(TAG, "***************ACTION_UP");
			// 取得左右滑动时手指松开的X坐标
			touchUpX = event.getX();
			touchUpY = event.getY();
			// 从左往右滑，看前一张
			if (touchUpX - touchDownX > 100) {
				showPicture(SHOW_PRE_PIC);
			}
			// 从右往左滑，看下一张
			else if (touchDownX - touchUpX > 100) {
				showPicture(SHOW_NEXT_PIC);
			}
			// 触发单击事件
			else if (Math.abs(touchDownY - touchUpY) < 10 && Math.abs(touchDownX - touchUpX) < 10 && System.currentTimeMillis() - downTime < 200) {
				showToast("触发点击事件,pictureIndex:" + pictureIndex);
				showDetail();
			} else {
				// 因为我们在触摸的时候曾关闭过自动滚动,所以这里再次开启
				startAutoScroll();
			}
			isResponse = true;
			// 开启父类View滚动
			v.getParent().requestDisallowInterceptTouchEvent(false);
			isMoving = false;
		}

		return isResponse;
	}

	private void showDetail() {
		startActivity(new Intent(this, EmptyActivity.class));
	}

	/**
	 * 开启自动滚动
	 */
	private void startAutoScroll() {
		mHandler.removeMessages(AUTO_SCROLL);
		mHandler.sendEmptyMessageDelayed(AUTO_SCROLL, getResources().getInteger(R.integer.auto_scroll_time));
	}

	/**
	 * 展示图片
	 * 
	 * @param flag
	 */
	private void showPicture(int flag) {
		if (flag == SHOW_PRE_PIC) {// 显示上一张
			// 取得当前要看的图片的index
			pictureIndex = pictureIndex == 0 ? arrayPictures.length - 1 : pictureIndex - 1;
			// 设置图片切换的动画
			switch (currAnimType) {
			case ANIM_TYPE_SLIDE:
				imageSwicher.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_left));
				imageSwicher.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_right));
				break;
			case ANIM_TYPE_PUSH:
				imageSwicher.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_down_in));
				imageSwicher.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_down_out));
				break;
			case ANIM_TYPE_ZOOM:
				imageSwicher.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.zoom_in));
				imageSwicher.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.zoom_out));
				break;
			default:
				break;
			}

		} else if (flag == SHOW_NEXT_PIC) {// 显示下一张
			// 取得当前要看的图片的index
			pictureIndex = pictureIndex == arrayPictures.length - 1 ? 0 : pictureIndex + 1;
			// 设置图片切换的动画
			switch (currAnimType) {
			case ANIM_TYPE_SLIDE:
				imageSwicher.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right));
				imageSwicher.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_left));
				break;
			case ANIM_TYPE_PUSH:
				imageSwicher.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_in));
				imageSwicher.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_out));
				break;
			case ANIM_TYPE_ZOOM:
				imageSwicher.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.zoom_in));
				imageSwicher.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.zoom_out));
				break;
			default:
				break;
			}
		}
		// 设置当前要看的图片
		imageSwicher.setImageResource(arrayPictures[pictureIndex]);
		// 更新圆点状态
		updateCircleState();
	}

	@Override
	public View makeView() {
		ImageView imageView = new ImageView(this);
		imageView.setImageResource(arrayPictures[pictureIndex]);
		return imageView;
	}

}
