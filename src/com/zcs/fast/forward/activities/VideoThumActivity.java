package com.zcs.fast.forward.activities;

import java.io.File;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zcs.fast.forward.R;
import com.zcs.fast.forward.base.BaseActivity;
import com.zcs.fast.forward.utils.LogUtil;
import com.zcs.fast.forward.utils.ThumbnailUtil;

@SuppressLint("HandlerLeak")
public class VideoThumActivity extends BaseActivity {
	/** constant */
	private static final String CURR_TITLE = "视频缩略图";
	private static final int SCAN_VIDEO = 0;

	/** Views */
	private LinearLayout videoListLayout;
	private TextView descTip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_thum);
		super.init();

		// TODO 延时开启视频扫描
		mHandler.sendEmptyMessageDelayed(SCAN_VIDEO, 1000);
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SCAN_VIDEO:
				scanVieo();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 扫描视频文件
	 */
	private void scanVieo() {
		String videoDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "maiziedu" + File.separator + "course";
		File dir = new File(videoDir);
		for (File f : dir.listFiles()) {
			if (f.getName().endsWith(".mp4")) {
				try {
					ImageView mImageView = new ImageView(this);
					TextView mTextView = new TextView(this);
					mImageView.setImageBitmap(ThumbnailUtil.getVideoThumbnail(f.getPath(), 0, 0, MediaStore.Images.Thumbnails.MINI_KIND));
					mTextView.setText(f.getName() + "\n");
					mTextView.setGravity(Gravity.CENTER);

					videoListLayout.addView(mImageView);
					videoListLayout.addView(mTextView);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		descTip.setText(getString(R.string.video_thum_desc));
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
		super.titlebarView = findViewById(R.id.titlebar_video_thum);

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
		videoListLayout = (LinearLayout) findViewById(R.id.video_list_layout);
		descTip = (TextView) findViewById(R.id.demo_desc_tip);
	}

}
