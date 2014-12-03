package com.zcs.fast.forward.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.Volley;
import com.zcs.fast.forward.R;
import com.zcs.fast.forward.base.BaseActivity;
import com.zcs.fast.forward.utils.BitmapCache;
import com.zcs.fast.forward.utils.LogUtil;

public class VolleyActivity extends BaseActivity {
	/** constant */
	private static final String CURR_TITLE = "Google Volley";

	/** Views */
	private ImageView img1, img2;
	private ImageLoader mImageLoader;
	private RequestQueue mQueue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_volley);
		super.init();
		initVolley();
	}

	/**
	 * 初始化Volley
	 */
	private void initVolley() {
		mQueue = Volley.newRequestQueue(this);
		mImageLoader = new ImageLoader(mQueue, new BitmapCache());
		
		// TODO 加载图片
		getImgByVolley();
	}

	/**
	 * 利用Volley加载图片
	 */
	private void getImgByVolley() {
		String imgUrl1 = "http://www.maiziedu.com/sites/all/themes/basic/images/ad4-banner-app.jpg";
		ImageListener listener1 = ImageLoader.getImageListener(img1, 0, 0);
		mImageLoader.get(imgUrl1, listener1);

		String imgUrl2 = "http://img0.bdstatic.com/img/image/%E6%9C%AA%E6%A0%87%E9%A2%98-1.jpg";
		ImageListener listener2 = ImageLoader.getImageListener(img2, 0, 0);
		mImageLoader.get(imgUrl2, listener2);
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
		super.titlebarView = findViewById(R.id.titlebar_volley);

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
		img1 = (ImageView) findViewById(R.id.volley_img_1);
		img2 = (ImageView) findViewById(R.id.volley_img_2);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
