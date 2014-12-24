package com.zcs.fast.forward.commons;

import com.zcs.fast.forward.R;
import com.zcs.fast.forward.activities.CustomDialogActivity;
import com.zcs.fast.forward.activities.ExpandableListActivity;
import com.zcs.fast.forward.activities.GifViewActivity;
import com.zcs.fast.forward.activities.GreenDaoActivity;
import com.zcs.fast.forward.activities.ImageSwitcherActivity;
import com.zcs.fast.forward.activities.ListViewCheckAllActivity;
import com.zcs.fast.forward.activities.MultiTouchActivity;
import com.zcs.fast.forward.activities.OfflineDownloadActivity;
import com.zcs.fast.forward.activities.RecommendCourseActivity;
import com.zcs.fast.forward.activities.TextSwitcherActivity;
import com.zcs.fast.forward.activities.VideoThumActivity;
import com.zcs.fast.forward.activities.ViewFlipActivity;
import com.zcs.fast.forward.activities.ViewPagerFocusImageActivity;
import com.zcs.fast.forward.activities.ViewStubActivity;
import com.zcs.fast.forward.activities.VolleyActivity;

/**
 * 主界面GridView数据提供者
 * 
 * @author ZengCS
 * @since 2014年11月19日15:22:07
 */
public class MainGridDataProvider {
	public static Integer[] icons = { R.drawable.ic_android,// Android
			R.drawable.ic_android,// Android
			R.drawable.ic_android,// Android
			R.drawable.ic_android,// Android
			R.drawable.ic_android,// Android
			R.drawable.ic_android,// Android
			R.drawable.ic_android,// Android
			R.drawable.ic_android,// Android
			R.drawable.ic_android,// Android
			R.drawable.ic_android,// Android
			R.drawable.ic_android,// Android
			R.drawable.ic_android,// Android
			R.drawable.ic_android,// Android
			R.drawable.ic_android,// Android
			R.drawable.ic_java,// Android
	};

	public static String[] names = { "多点触控",// 第A1个
			"视频缩略图",// 第A2个
			"对话框",// 第A3个
			"Volley",// 第A4个
			"GreenDAO",// 第A5个
			"数字时钟",// 第A6个
			"我的好友",// 第A7个
			"焦点图",// 第A8个
			"焦点图2",// 第A8.1个
			"ViewStub",// 第A9个
			"GifView",// 第A10个
			"离线下载",// 第A11个
			"View旋转",// 第A12个
			"ListView全选",// 第A13个
			"推荐课程",// 第B1个
	};

	public static Object[] targets = { MultiTouchActivity.class,// 第1个
			VideoThumActivity.class,// 第2个
			CustomDialogActivity.class,// 第3个
			VolleyActivity.class,// 第4个
			GreenDaoActivity.class,// 第5个
			TextSwitcherActivity.class,// 第6个
			ExpandableListActivity.class,// 第7个
			ImageSwitcherActivity.class,// 第8个
			ViewPagerFocusImageActivity.class,// 第8.1个
			ViewStubActivity.class,// 第9个
			GifViewActivity.class,// 第10个
			OfflineDownloadActivity.class,// 第11个
			ViewFlipActivity.class,// 第A12个
			ListViewCheckAllActivity.class,// 第A13个
			RecommendCourseActivity.class,// 第B1个
	};
}
