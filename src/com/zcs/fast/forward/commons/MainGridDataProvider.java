package com.zcs.fast.forward.commons;

import com.zcs.fast.forward.R;
import com.zcs.fast.forward.activities.CustomDialogActivity;
import com.zcs.fast.forward.activities.ExpandableListActivity;
import com.zcs.fast.forward.activities.GreenDaoActivity;
import com.zcs.fast.forward.activities.ImageSwitcherActivity;
import com.zcs.fast.forward.activities.MultiTouchActivity;
import com.zcs.fast.forward.activities.TextSwitcherActivity;
import com.zcs.fast.forward.activities.VideoThumActivity;
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
//			R.drawable.ic_java,// Android
	};

	public static String[] names = { "多点触控",// 第1个
			"视频缩略图",// 第2个
			"对话框",// 第3个
			"Volley",// 第4个
			"GreenDAO",// 第5个
			"数字时钟",// 第6个
			"我的好友",// 第7个
			"ViewStub",// 第8个
			"焦点图",// 第9个
//			"推荐课程",// 第10个
	};

	public static Object[] targets = { MultiTouchActivity.class,// 第1个
			VideoThumActivity.class,// 第2个
			CustomDialogActivity.class,// 第3个
			VolleyActivity.class,// 第4个
			GreenDaoActivity.class,// 第5个
			TextSwitcherActivity.class,// 第6个
			ExpandableListActivity.class,// 第7个
			ViewStubActivity.class,// 第8个
			ImageSwitcherActivity.class,// 第9个
//			RecommendCourseActivity.class,// 第10个
	};
}
