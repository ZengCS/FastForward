package com.zcs.fast.forward.activities;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zcs.fast.forward.R;
import com.zcs.fast.forward.base.BaseActivity;
import com.zcs.fast.forward.utils.DisplayUtil;
import com.zcs.fast.forward.utils.LogUtil;

public class ViewPagerFocusImageActivity extends BaseActivity implements OnPageChangeListener {
	/** constant */
	private static final String CURR_TITLE = "ViewPager焦点图";

	/**
	 * ViewPager
	 */
	private ViewPager viewPager;

	/**
	 * 装点点的ImageView数组
	 */
	private View[] circles;

	/**
	 * 装ImageView数组
	 */
	private ImageView[] mImageViews;

	/**
	 * 图片资源id
	 */
	private int[] imgIdArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_pager_focus_image);
		super.init();

		viewPager = (ViewPager) findViewById(R.id.viewPager);

		// 载入图片资源ID
		imgIdArray = new int[] { R.drawable.item01, R.drawable.item02, R.drawable.item03, R.drawable.item04, R.drawable.item05, R.drawable.item06,
				R.drawable.item07, R.drawable.item08 };

		// 将点点加入到ViewGroup中
		initCircles();

		// 将图片装载到数组中
		mImageViews = new ImageView[imgIdArray.length];
		for (int i = 0; i < mImageViews.length; i++) {
			ImageView imageView = new ImageView(this);
			mImageViews[i] = imageView;
			imageView.setBackgroundResource(imgIdArray[i]);
		}

		// 设置Adapter
		viewPager.setAdapter(new MyAdapter());
		// 设置监听，主要是设置点点的背景
		viewPager.setOnPageChangeListener(this);
		// 设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动
		viewPager.setCurrentItem((mImageViews.length) * 100);
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
		circles = new View[imgIdArray.length];

		for (int i = 0; i < imgIdArray.length; i++) {
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

	public class MyAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			if (imgIdArray.length > 3) {
				((ViewPager) container).removeView(mImageViews[position % mImageViews.length]);
			}
		}

		/**
		 * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
		 */
		@Override
		public Object instantiateItem(View container, int position) {
			try {
				((ViewPager) container).addView(mImageViews[position % mImageViews.length], 0);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return mImageViews[position % mImageViews.length];
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		setImageBackground(arg0 % mImageViews.length);
	}

	/**
	 * 设置选中的tip的背景
	 * 
	 * @param selectItems
	 */
	private void setImageBackground(int selectItems) {
		for (int i = 0; i < circles.length; i++) {
			if (i == selectItems) {
				circles[i].setBackgroundResource(R.drawable.circle_white);
			} else {
				circles[i].setBackgroundResource(R.drawable.circle_gray);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.titlebtn_left_act:
			finish();
			break;
		case R.id.titlebtn_right_act:// 查看
			displayHelpDialog();
			break;
		default:
			break;
		}
	}

	@Override
	protected void initTitlebar() {
		// TODO 初始化标题栏
		super.titlebarView = findViewById(R.id.titlebar_focus_img);

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
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
