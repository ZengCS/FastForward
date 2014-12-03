package com.zcs.fast.forward.utils;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
public class PullRefreshUtil {
	public static SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
	
	public static final int SHOW_TIP_LATER = 0x1f5001;// 延时展示提示信息
	public static final int AUTO_HIDE_TIP = 0x1f5002;// 自动隐藏提示信息
	public static final int AUTO_HIDE_LOADING = 0x1f5003;// 延时隐藏加载中
	public static final int START_NETWORK_TIME_OUT_LISTENER = 0x1f5004;// 监听网络连接超时
	public static final int LOAD_MORE_DATA_LATER = 4;// 延迟加载更多数据
	public static final int SHOW_TIP_LATER_ALWAYS = 0x1f5008;// 延时展示提示信息,不自动隐藏

	public static final long SHOW_TIP_DURATION = 1200;// 提示信息持续时间
	public static final int AUTO_HIDE_LOADING_TIME = 1000;// 延时隐藏加载时间
	public static final long ANIM_DURATION = 350;// 延时展示提示时间
	public static final long NETWORK_TIME_OUT = 8000;// 网络超时时间8秒

	public static final long PULL_REFRESH_TIME_GAP = 5000;// 下拉刷新时间间隔
}
