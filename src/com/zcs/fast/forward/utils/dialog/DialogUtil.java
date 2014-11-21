package com.zcs.fast.forward.utils.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zcs.fast.forward.R;
import com.zcs.fast.forward.utils.TextUtils;

/**
 * Dialog生成工具类
 * 
 * @author ZengCS
 * @since 2014年10月16日
 */
@SuppressWarnings("deprecation")
public class DialogUtil {
	private static Button okBtn, cancelBtn;// 按钮

	/**
	 * 创建加载提示窗口
	 * 
	 * @param param
	 * @return
	 */
	public static Dialog createLoadingDialog(DialogParam param) {
		LayoutInflater inflater = LayoutInflater.from(param.getContext());
		View v = inflater.inflate(R.layout.mz_loading_dialog, null);// 得到加载view
		// LinearLayout layout = (LinearLayout)
		// v.findViewById(R.id.dialog_view);// 加载布局
		RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.dialog_view);// 加载布局
		// hwgis_loading_dialog.xml中的ImageView
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		TextView tipTextView = (TextView) v.findViewById(R.id.dialog_loading_title);// 提示文字
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(param.getContext(), R.anim.mz_loading_animation);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(param.getMsg());// 设置加载信息

		Dialog dialog = new Dialog(param.getContext(), R.style.mz_dialog_style);// 创建自定义样式dialog

		dialog.setCancelable(param.isCancelable());// 不可以用“返回键”取消
		dialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
		return dialog;
	}

	/**
	 * 创建消息提示框,只有一个"确定"按钮
	 * 
	 * @return
	 */
	public static Dialog createMessageDialog(DialogParam param) {
		LayoutInflater inflater = LayoutInflater.from(param.getContext());
		View v = inflater.inflate(R.layout.mz_message_dialog, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view_msg);// 加载布局
		TextView title = (TextView) v.findViewById(R.id.dialog_txt_title);// 提示标题
		if (param.getTitle() != null && !"".equals(param.getTitle())) {
			title.setText(param.getTitle());
		}
		TextView msg = (TextView) v.findViewById(R.id.dialog_txt_msg);// 提示文字
		// msg.setText(param.getMsg());// 设置提示信息
		// 蓝色#1F76B6 红色#E96B69
		msg.setText(TextUtils.highlight(param.getMsg(), "MultiTouchActivity", Color.parseColor("#E96B69")));// 设置提示信息(带高亮)

		// TODO 初始化组件
		okBtn = (Button) v.findViewById(R.id.dialog_btn_ok);

		// 设置确定按钮文字及点击事件
		if (param.getOkBtnStr() != null && !"".equals(param.getOkBtnStr())) {
			okBtn.setText(param.getOkBtnStr());
		}
		okBtn.setOnClickListener(param.getOkBtnClickListener());

		Dialog confirmDialog = new Dialog(param.getContext(), R.style.mz_dialog_style);// 创建自定义样式dialog

		confirmDialog.setCancelable(param.isCancelable());// 不可以用“返回键”
		confirmDialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
		return confirmDialog;
	}

	/**
	 * 创建消息提示框,只有一个"确定"按钮
	 * 
	 * @param param
	 *            Dialog配置属性
	 * @param highStrs
	 *            高亮字符串
	 * @param highColors
	 *            高亮颜色
	 * @return
	 */
	public static Dialog createMessageDialog(DialogParam param, String[] highStrs, int highColor) {
		LayoutInflater inflater = LayoutInflater.from(param.getContext());
		View v = inflater.inflate(R.layout.mz_message_dialog, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view_msg);// 加载布局
		TextView title = (TextView) v.findViewById(R.id.dialog_txt_title);// 提示标题
		if (param.getTitle() != null && !"".equals(param.getTitle())) {
			title.setText(param.getTitle());
		}
		TextView msg = (TextView) v.findViewById(R.id.dialog_txt_msg);// 提示文字
		msg.setTextIsSelectable(param.isTextIsSelectable());

		// 设置提示信息(带高亮)
		msg.setText(TextUtils.highlight(param.getMsg(), highStrs, highColor));
		msg.setMovementMethod(LinkMovementMethod.getInstance());

		// TODO 初始化组件
		okBtn = (Button) v.findViewById(R.id.dialog_btn_ok);

		// 设置确定按钮文字及点击事件
		if (param.getOkBtnStr() != null && !"".equals(param.getOkBtnStr())) {
			okBtn.setText(param.getOkBtnStr());
		}
		okBtn.setOnClickListener(param.getOkBtnClickListener());

		Dialog confirmDialog = new Dialog(param.getContext(), R.style.mz_dialog_style);// 创建自定义样式dialog

		confirmDialog.setCancelable(param.isCancelable());// 不可以用“返回键”
		confirmDialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
		return confirmDialog;
	}

	/**
	 * 创建确认对话框
	 * 
	 * @return
	 */
	public static Dialog createConfirmDialog(DialogParam param) {
		LayoutInflater inflater = LayoutInflater.from(param.getContext());
		View v = inflater.inflate(R.layout.mz_confirm_dialog, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view_msg);// 加载布局
		TextView title = (TextView) v.findViewById(R.id.dialog_txt_title);// 提示标题
		if (param.getTitle() != null && !"".equals(param.getTitle())) {
			title.setText(param.getTitle());
		}
		TextView msg = (TextView) v.findViewById(R.id.dialog_txt_msg);// 提示文字
		msg.setText(param.getMsg());// 设置加载信息

		// TODO 初始化组件
		okBtn = (Button) v.findViewById(R.id.dialog_btn_ok);
		cancelBtn = (Button) v.findViewById(R.id.dialog_btn_cancel);

		// 设置确定按钮文字及点击事件
		if (param.getOkBtnStr() != null && !"".equals(param.getOkBtnStr())) {
			okBtn.setText(param.getOkBtnStr());
		}
		okBtn.setOnClickListener(param.getOkBtnClickListener());

		// 设置取消按钮文字及点击事件
		if (param.getCancelBtnStr() != null && !"".equals(param.getCancelBtnStr())) {
			cancelBtn.setText(param.getCancelBtnStr());
		}
		cancelBtn.setOnClickListener(param.getCancelBtnClickListener());

		Dialog confirmDialog = new Dialog(param.getContext(), R.style.mz_dialog_style);// 创建自定义样式dialog

		confirmDialog.setCancelable(param.isCancelable());// 不可以用“返回键”
		confirmDialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
		return confirmDialog;
	}
}
