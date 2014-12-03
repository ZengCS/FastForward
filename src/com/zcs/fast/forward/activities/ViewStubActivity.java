package com.zcs.fast.forward.activities;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zcs.fast.forward.R;
import com.zcs.fast.forward.base.BaseActivity;
import com.zcs.fast.forward.utils.LogUtil;

public class ViewStubActivity extends BaseActivity {
	/** constant */
	private static final String CURR_TITLE = "ViewStubActivity";

	/** Views */
	// private Button showTextBtn, showImageBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_stub);
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
		case R.id.btn_show_text:// 显示TextView
			ViewStub stub_text = (ViewStub) findViewById(R.id.viewstub_demo_text);
			stub_text.inflate();
			TextView text = (TextView) findViewById(R.id.viewstub_demo_textview);
			text.setText(getString(R.string.source_info));
			((Button) v).setEnabled(false);
			break;
		case R.id.btn_show_img:// 显示ImageView
			ViewStub stub_img = (ViewStub) findViewById(R.id.viewstub_demo_image);
			stub_img.inflate();
			ImageView image = (ImageView) findViewById(R.id.viewstub_demo_imageview);
			image.setImageResource(R.drawable.bg4);
			((Button) v).setEnabled(false);
			break;
		default:
			break;
		}
	}

	@Override
	protected void initTitlebar() {
		// TODO 初始化标题栏
		super.titlebarView = findViewById(R.id.titlebar_view_stub);

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
		findViewById(R.id.btn_show_text).setOnClickListener(this);
		findViewById(R.id.btn_show_img).setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
