package com.zcs.fast.forward.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zcs.fast.forward.R;
import com.zcs.fast.forward.base.BaseActivity;
import com.zcs.fast.forward.utils.LogUtil;
import com.zcs.fast.forward.utils.dialog.DialogParam;
import com.zcs.fast.forward.utils.dialog.DialogUtil;

public class CustomDialogActivity extends BaseActivity {
	/** constant */
	private static final String CURR_TITLE = "自定义对话框";

	/** Views */
	private Button cfmBtn, msgBtn, proBtn, bottomBtn;
	private Dialog confirmDialog, messageDialog, processDialog, bottomDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom_dialog);
		super.init();
	}

	/**
	 * 初始化确认对话框
	 */
	private void initConfirmDialog() {
		DialogParam param = new DialogParam(this, "你确定你要当一名攻城狮吗？\n\n（点击Back键或者Dialog外部不可以关闭Dialog！）", false);
		param.setOkBtnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				confirmDialog.dismiss();
				showToast("恭喜你成为一名攻城狮！");
			}
		});
		param.setCancelBtnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				confirmDialog.dismiss();
				showToast("Sorry，你依然是程序猿！");
			}
		});
		confirmDialog = DialogUtil.createConfirmDialog(param);
	}

	/**
	 * 初始化消息对话框
	 */
	private void initMessageDialog() {
		DialogParam param = new DialogParam(this, "这是一个消息对话框！\n\n（点击Back键或者Dialog外部可以关闭Dialog！）", true);
		param.setOkBtnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				messageDialog.dismiss();
				showToast("我知道你知道了！");
			}
		});
		messageDialog = DialogUtil.createMessageDialog(param);
	}

	/**
	 * 初始化加载对话框
	 */
	private void initProcessDialog() {
		DialogParam param = new DialogParam(this, "卖力加载中...", false);
		processDialog = DialogUtil.createLoadingDialog(param);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_custom_dialog_confirm:// 确认对话框
			if (confirmDialog == null) {
				initConfirmDialog();
			}
			confirmDialog.show();
			break;
		case R.id.btn_custom_dialog_message:// 消息对话框
			if (messageDialog == null) {
				initMessageDialog();
			}
			messageDialog.show();
			break;
		case R.id.btn_custom_dialog_process:// 加载对话框
			if (processDialog == null) {
				initProcessDialog();
			}
			processDialog.show();
			showToast("数据需要加载3秒中，请等待！");
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					processDialog.hide();
				}
			}, 3000);
			break;
		case R.id.btn_custom_dialog_bottom:// 展示底部弹出式对话框
			if (bottomDialog == null) {
				initBottomDialog();
			}
			bottomDialog.show();
			break;
		case R.id.dialog_bottom_btn_ok:// 底部弹出对话框-退出
			bottomDialog.dismiss();
			finish();
			break;
		case R.id.dialog_bottom_btn_cancel:// 底部弹出对话框-取消
			bottomDialog.dismiss();
			break;
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

	/**
	 * 初始化底部Dialog
	 */
	private void initBottomDialog() {
		View dialogView = getLayoutInflater().inflate(R.layout.dialog_bottom, null);
		TextView titleView = (TextView) dialogView.findViewById(R.id.dialog_bottom_title);

		titleView.setText("你确定要关闭当前Activity吗？");
		dialogView.findViewById(R.id.dialog_bottom_btn_ok).setOnClickListener(this);
		dialogView.findViewById(R.id.dialog_bottom_btn_cancel).setOnClickListener(this);

		// 创建Dialog
		bottomDialog = DialogUtil.createBottomDialog(dialogView, this);
	}

	@Override
	protected void initTitlebar() {
		// TODO 初始化标题栏
		super.titlebarView = findViewById(R.id.titlebar_custom_dialog);

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
		msgBtn = (Button) findViewById(R.id.btn_custom_dialog_message);
		cfmBtn = (Button) findViewById(R.id.btn_custom_dialog_confirm);
		proBtn = (Button) findViewById(R.id.btn_custom_dialog_process);
		bottomBtn = (Button) findViewById(R.id.btn_custom_dialog_bottom);

		msgBtn.setOnClickListener(this);
		cfmBtn.setOnClickListener(this);
		proBtn.setOnClickListener(this);
		bottomBtn.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (processDialog != null) {
			processDialog.dismiss();
		}
	}

}
