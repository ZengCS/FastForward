package com.zcs.fast.forward.commons;

import com.zcs.fast.forward.R;
import com.zcs.fast.forward.activities.CustomDialogActivity;
import com.zcs.fast.forward.activities.EmptyActivity;
import com.zcs.fast.forward.activities.MultiTouchActivity;
import com.zcs.fast.forward.activities.VideoThumActivity;

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
	};

	public static String[] names = { "多点触控",// 第1个
			"视频缩略图",// 第2个
			"对话框",// 第3个
			"DemoName",// 第4个
			"DemoName",// 第5个
			"DemoName",// 第6个
			"DemoName",// 第7个
			"DemoName",// 第8个
	};

	public static Object[] targets = { MultiTouchActivity.class,// 第1个
			VideoThumActivity.class,// 第2个
			CustomDialogActivity.class,// 第3个
			EmptyActivity.class,// 第4个
			EmptyActivity.class,// 第5个
			EmptyActivity.class,// 第6个
			EmptyActivity.class,// 第7个
			EmptyActivity.class,// 第8个
	};
}
