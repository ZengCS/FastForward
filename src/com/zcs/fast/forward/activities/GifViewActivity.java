package com.zcs.fast.forward.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zcs.fast.forward.R;
import com.zcs.fast.forward.base.BaseActivity;
import com.zcs.fast.forward.utils.LogUtil;
import com.zcs.fast.forward.widget.GifView;

public class GifViewActivity extends BaseActivity {
	/** constant */
	private static final String CURR_TITLE = "GifView";

	/** Views */
	private GifView gv1, gv2, gv3;
	private Button btnNotice;
	private View noticeLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gif_view);
		super.init();
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
		case R.id.btn_notice:// 查看注意事项
			if (noticeLayout.getVisibility() == View.VISIBLE) {
				noticeLayout.setVisibility(View.GONE);
				btnNotice.setText("查看注意事项");
				btnNotice.setBackgroundResource(R.drawable.bg_corner_blue_selector);
			} else {
				noticeLayout.setVisibility(View.VISIBLE);
				btnNotice.setText("关闭");
				btnNotice.setBackgroundResource(R.drawable.bg_corner_red_selector);
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void initTitlebar() {
		// TODO 初始化标题栏
		super.titlebarView = findViewById(R.id.titlebar_gifview);

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
		gv1 = (GifView) findViewById(R.id.gif_view1);
		gv2 = (GifView) findViewById(R.id.gif_view2);
		gv3 = (GifView) findViewById(R.id.gif_view3);

		gv1.setGifName("aoda_cat_5");
		gv2.setGifName("aoda_cat_6");
		gv3.setGifName("aoda_cat_7");

		btnNotice = (Button) findViewById(R.id.btn_notice);
		btnNotice.setOnClickListener(this);
		noticeLayout = findViewById(R.id.notice_layout);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
