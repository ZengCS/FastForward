package com.zcs.fast.forward.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zcs.fast.forward.R;
import com.zcs.fast.forward.base.BaseActivity;
import com.zcs.fast.forward.utils.LogUtil;

public class ViewFlipActivity extends BaseActivity {
	/** constant */
	private static final String CURR_TITLE = "View旋转";

	/** Views */
	private View firstView, secondView;
	private Interpolator accelerator = new AccelerateInterpolator();
	private Interpolator decelerator = new DecelerateInterpolator();
	private boolean isFlipping = false;
	private Button hideGridBtn;

	private View gridLineV, gridLineH;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_flip);
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
		case R.id.btn_start_flip:// 旋转
			// TODO 旋转
			if (!isFlipping) {
				flipViews();
			}
			break;
		case R.id.btn_hide_grid:// 隐藏网格
			setGrid();
			break;
		default:
			break;
		}
	}

	/**
	 * 设置网格
	 */
	private void setGrid() {
		int visibility = View.INVISIBLE;
		if (hideGridBtn.getText().equals("隐藏网格")) {
			hideGridBtn.setText("显示网格");
			hideGridBtn.setBackgroundResource(R.drawable.bg_corner_blue_selector);
			visibility = View.INVISIBLE;
		} else {
			hideGridBtn.setText("隐藏网格");
			hideGridBtn.setBackgroundResource(R.drawable.bg_corner_red_selector);
			visibility = View.VISIBLE;
		}
		gridLineH.setVisibility(visibility);
		gridLineV.setVisibility(visibility);
	}

	/**
	 * Flip The Views
	 */
	private void flipViews() {
		// 设置为正在Flipping
		isFlipping = true;

		final View visibleList;
		final View invisibleList;
		if (firstView.getVisibility() == View.GONE) {
			visibleList = secondView;
			invisibleList = firstView;
		} else {
			invisibleList = secondView;
			visibleList = firstView;
		}
		ObjectAnimator visToInvis = ObjectAnimator.ofFloat(visibleList, "rotationY", 0f, 90f);
		visToInvis.setDuration(500);
		visToInvis.setInterpolator(accelerator);
		final ObjectAnimator invisToVis = ObjectAnimator.ofFloat(invisibleList, "rotationY", -90f, 0f);
		invisToVis.setDuration(500);
		invisToVis.setInterpolator(decelerator);
		visToInvis.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator anim) {
				visibleList.setVisibility(View.GONE);
				invisToVis.start();
				invisibleList.setVisibility(View.VISIBLE);
				invisToVis.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator anim) {
						// Flip End
						isFlipping = false;
					}
				});
			}
		});
		visToInvis.start();
	}

	@Override
	protected void initTitlebar() {
		// TODO 初始化标题栏
		super.titlebarView = findViewById(R.id.titlebar_view_flip);

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
		firstView = findViewById(R.id.first_view);
		secondView = findViewById(R.id.second_view);

		hideGridBtn = (Button) findViewById(R.id.btn_hide_grid);
		hideGridBtn.setOnClickListener(this);

		gridLineV = findViewById(R.id.grid_line_v);
		gridLineH = findViewById(R.id.grid_line_h);

		// TODO 绑定事件
		findViewById(R.id.btn_start_flip).setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
