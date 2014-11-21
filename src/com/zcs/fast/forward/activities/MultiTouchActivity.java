package com.zcs.fast.forward.activities;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zcs.fast.forward.R;
import com.zcs.fast.forward.base.BaseActivity;
import com.zcs.fast.forward.utils.LogUtil;

public class MultiTouchActivity extends BaseActivity implements OnTouchListener {
	/** constant */
	private static final String CURR_TITLE = "多点触控";

	/** Views */
	private ImageView multiImg;

	/** OnTouch */
	private static final int NONE = 0;
	private static final int MOVE = 1;
	private static final int ZOOM = 2;

	private int mode = NONE;

	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	private PointF startPoint = new PointF();
	private PointF mid = new PointF();
	private float oldDistance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multi_touch);
		super.init();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.titlebtn_left_act:// 返回
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
		// TODO 初始化标题栏,super.init()中自动调用
		super.titlebarView = findViewById(R.id.titlebar_multi_touch);

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
		// TODO 初始化组件,super.init()中自动调用
		multiImg = (ImageView) findViewById(R.id.multi_img);
		multiImg.setOnTouchListener(this);
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		ImageView imageView = (ImageView) view;
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			savedMatrix.set(matrix);
			startPoint.set(event.getX(), event.getY());
			mode = MOVE;
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			oldDistance = (float) Math.sqrt((event.getX(0) - event.getX(1)) * (event.getX(0) - event.getX(1)) + (event.getY(0) - event.getY(1))
					* (event.getY(0) - event.getY(1)));
			if (oldDistance > 10f) {
				savedMatrix.set(matrix);
				mid.set((event.getX(0) + event.getX(1)) / 2, (event.getY(0) + event.getY(1)) / 2);
				mode = ZOOM;
			}
		case MotionEvent.ACTION_MOVE:
			if (mode == MOVE) {
				float dx = event.getX() - startPoint.x;
				float dy = event.getY() - startPoint.y;
				matrix.postTranslate(dx, dy);
				startPoint.set(event.getX(), event.getY());
			} else if (mode == ZOOM) {
				float newDistance;
				newDistance = (float) Math.sqrt((event.getX(0) - event.getX(1)) * (event.getX(0) - event.getX(1)) + (event.getY(0) - event.getY(1))
						* (event.getY(0) - event.getY(1)));
				if (newDistance > 10f) {
					matrix.set(savedMatrix);
					matrix.postScale(newDistance / oldDistance, newDistance / oldDistance, mid.x, mid.y);
					oldDistance = newDistance;
					savedMatrix.set(matrix);
				}
			}
			break;
		}
		imageView.setImageMatrix(matrix);
		return true;
	}

}
