package com.zcs.fast.forward.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zcs.fast.forward.R;
import com.zcs.fast.forward.base.BaseActivity;
import com.zcs.fast.forward.utils.LogUtil;

public class HelpActivity extends BaseActivity {
	/** constant */
	private static final String CURR_TITLE = "帮助FAQ";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
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
		default:
			break;
		}
	}

	@Override
	protected void initTitlebar() {
		// TODO 初始化标题栏
		super.titlebarView = findViewById(R.id.titlebar_help);

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
