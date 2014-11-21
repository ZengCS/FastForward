package com.zcs.fast.forward.utils;

/**
 * 所有Handler对应的Message的what值管理类
 * 
 * @author ZengCS
 * @since 2014年10月15日
 */
public class HandlerMsgTag {
	public static final int TEST_TAG = 0x1f0001;// 测试
	public static final int MSG_REFRESH_VIEW = 0x1f0002;// 更新界面
	public static final int MSG_SHOW_NET_ERROR = 0x1f0003;// 提示网络错误
	public static final int MSG_GET_NEWS = 0x1f0004;// 得到新闻数据
	public static final int MSG_GET_NEWS_SUCCESS = 0x1f0005;// 得到新闻成功，刷新页面并开始加载广告数据
	public static final int MSG_GET_RECOMMEND = 0x1f0006;// 得到推荐课程数据
	public static final int MSG_GET_RECOMMEND_SUCCESS = 0x1f0007;// 得到推荐课程成功，刷新页面并开始加载广告数据
	public static final int MSG_GET_DATA_FAIL = 0x1f5005; // 当数据获取失败
	public static final int MSG_GET_DATA_SUCCESS = 0x1f5006; // 获取数据成功
	public static final int MSG_SHOW_TOP_BUTTON = 0x1f5007; // 显示点击回顶部按钮
	public static final int MSG_HIDE_TOP_BUTTON = 0x1f5008; // 隐藏点击回顶部按钮

	public static final int MSG_PASSWORD_FIND_RESULT_TAG = 0x1f0008;// 密码找回结果
	public static final int MSG_REGISTER_RESULT_TAG = 0x1f0009;// 账号注册结果
	public static final int TAG_START_SENSOR_LATER = 0x1f000A;// 延时开启重力感应
	public static final int TAG_START_SENSOR_NOW = 0x1f000B;// 立即开启重力感应
	public static final int TAG_VLC_PLAY_TIME_PROGRESS = 0x1f000C;// VLC视频播放时间进度
	
	public static final int MSG_SHOW_SHARE_RESPONSE_TOAST = 0x1f000D; // 显示分享结果
	public static final int AUTO_HIDE_ERROR_LAYOUT = 0x1f000E; // 自动隐藏自定义错误提示消息
	
	public static final int SHOW_ADD_FAVORITE_RESULT = 0x1f000F; // 显示收藏结果
}
