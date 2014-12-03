package com.zcs.fast.forward.activities;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ViewSwitcher.ViewFactory;

import com.zcs.fast.forward.R;
import com.zcs.fast.forward.base.BaseActivity;
import com.zcs.fast.forward.utils.DisplayUtil;
import com.zcs.fast.forward.utils.LogUtil;

@SuppressLint({ "SimpleDateFormat", "HandlerLeak" })
public class TextSwitcherActivity extends BaseActivity implements ViewFactory {
	/** constant */
	private static final String CURR_TITLE = "TextSwitcher";
	private int mCounter = 0;

	/** Views */
	private TextSwitcher ts_y, ts_mm, ts_d, ts_h, ts_m, ts_s;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private TextView currAnimStyle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text_switcher);
		super.init();
		mHandler.sendEmptyMessage(0);
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				setTime();
				break;
			default:
				break;
			}
		}
	};

	private void setTime() {
		String[] dates = sdf.format(new Date()).split(" ");
		// Set Date
		String[] date = dates[0].split("-");
		TextView currView = (TextView) ts_y.getCurrentView();
		if (!currView.getText().equals(date[0])) {
			ts_y.setText(date[0]);
		}
		currView = (TextView) ts_mm.getCurrentView();
		if (!currView.getText().equals(date[1])) {
			ts_mm.setText(date[1]);
		}
		currView = (TextView) ts_d.getCurrentView();
		if (!currView.getText().equals(date[2])) {
			ts_d.setText(date[2]);
		}

		// set Time
		String[] time = dates[1].split(":");

		currView = (TextView) ts_h.getCurrentView();
		if (!currView.getText().equals(time[0])) {
			ts_h.setText(time[0]);
		}
		currView = (TextView) ts_m.getCurrentView();
		if (!currView.getText().equals(time[1])) {
			ts_m.setText(time[1]);
		}
		currView = (TextView) ts_s.getCurrentView();
		if (!currView.getText().equals(time[2])) {
			ts_s.setText(time[2]);
		}

		mHandler.removeMessages(0);
		mHandler.sendEmptyMessageDelayed(0, 100);
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

	private void setAnimationStyle() {
		Animation in = null, out = null;
		String type = "";
		switch (mCounter) {
		case 0:
			in = AnimationUtils.loadAnimation(this, R.anim.push_up_in);
			out = AnimationUtils.loadAnimation(this, R.anim.push_up_out);
			type = "PUSH_UP";
			break;
		case 1:
			in = AnimationUtils.loadAnimation(this, R.anim.push_left_in);
			out = AnimationUtils.loadAnimation(this, R.anim.push_left_out);
			type = "PUSH_LEFT";
			break;
		case 2:
			in = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
			out = AnimationUtils.loadAnimation(this, R.anim.zoom_out);
			type = "ZOOM";
			break;
		case 3:
			in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
			out = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
			type = "FADE";
			break;
		default:
			break;
		}

		currAnimStyle.setText("当前切换动画类型:" + type);

		ts_y.setInAnimation(in);
		ts_y.setOutAnimation(out);
		ts_mm.setInAnimation(in);
		ts_mm.setOutAnimation(out);
		ts_d.setInAnimation(in);
		ts_d.setOutAnimation(out);
		ts_h.setInAnimation(in);
		ts_h.setOutAnimation(out);
		ts_m.setInAnimation(in);
		ts_m.setOutAnimation(out);
		ts_s.setInAnimation(in);
		ts_s.setOutAnimation(out);
	}

	@Override
	protected void initTitlebar() {
		// TODO 初始化标题栏
		super.titlebarView = findViewById(R.id.titlebar_textswitcher);

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
		currAnimStyle = (TextView) findViewById(R.id.curr_anim_style);

		ts_y = (TextSwitcher) findViewById(R.id.date_year);
		ts_mm = (TextSwitcher) findViewById(R.id.date_month);
		ts_d = (TextSwitcher) findViewById(R.id.date_day);
		ts_h = (TextSwitcher) findViewById(R.id.date_hour);
		ts_m = (TextSwitcher) findViewById(R.id.date_minute);
		ts_s = (TextSwitcher) findViewById(R.id.date_second);

		ts_y.setFactory(this);
		ts_mm.setFactory(this);
		ts_d.setFactory(this);
		ts_h.setFactory(this);
		ts_m.setFactory(this);
		ts_s.setFactory(this);

		setAnimationStyle();

		Spinner mSpinner = (Spinner) findViewById(R.id.anim_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.anim_list, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(adapter);
		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// showToast("Spinner1: position=" + position + " id=" + id);
				mCounter = position;
				setAnimationStyle();
			}

			public void onNothingSelected(AdapterView<?> parent) {
				showToast("Spinner1: unselected");
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mHandler.removeMessages(0);
	}

	@Override
	public View makeView() {
		TextView t = new TextView(this);
		t.setGravity(Gravity.CENTER);
		t.setTextSize(DisplayUtil.sp2px(this, 16));
		t.setTextColor(Color.parseColor("#FFFFFF"));
		return t;
	}

}
