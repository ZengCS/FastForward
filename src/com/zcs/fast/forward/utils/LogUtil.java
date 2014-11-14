package com.zcs.fast.forward.utils;

import android.util.Log;

/**
 * LogUtil
 * 
 * @author ZengCS
 * @since 2014-11-14
 */
public class LogUtil {
	private static final int LOG_LEVEL = 6;

	public static void e(String tag, String msg) {
		if (LOG_LEVEL > 1)
			Log.e(tag, msg);
	}

	public static void w(String tag, String msg) {
		if (LOG_LEVEL > 2)
			Log.w(tag, msg);
	}

	public static void i(String tag, String msg) {
		if (LOG_LEVEL > 3)
			Log.i(tag, msg);
	}

	public static void d(String tag, String msg) {
		if (LOG_LEVEL > 4)
			Log.d(tag, msg);
	}

	public static void v(String tag, String msg) {
		if (LOG_LEVEL > 5)
			Log.v(tag, msg);
	}
}
