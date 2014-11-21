package com.zcs.fast.forward.utils;

import java.util.Map;
import java.util.Map.Entry;

import android.graphics.Bitmap;
import android.net.Uri;
import android.net.Uri.Builder;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.NetworkImageView;

public class VolleyUtil {
	public static final String TAG = "Volley/Debug";

	/**
	 * 利用Volley异步加载图片
	 * 
	 * @param mQueue
	 *            Volley RequestQueue对象
	 * @param imageUrl
	 *            图片url
	 * @param mImageView
	 *            显示图片的ImageView
	 * @param defaultImageResId
	 *            默认显示的图片,无图片请传 0
	 * @param errorImageResId
	 *            加载失败显示的图片,无图片请传 0
	 */
	public static void loadImageByVolley(RequestQueue mQueue, String imageUrl, ImageView mImageView, int defaultImageResId, int errorImageResId) {
		try {
			int maxSize = 10 * 1024 * 1024;
			final LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(maxSize);
			ImageCache imageCache = new ImageCache() {
				@Override
				public void putBitmap(String key, Bitmap value) {
					lruCache.put(key, value);
				}

				@Override
				public Bitmap getBitmap(String key) {
					return lruCache.get(key);
				}
			};
			ImageLoader imageLoader = new ImageLoader(mQueue, imageCache);
			ImageListener listener = ImageLoader.getImageListener(mImageView, defaultImageResId, errorImageResId);
			imageLoader.get(imageUrl, listener);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.e(TAG, "Error:" + e.getMessage());
		}
	}

	/**
	 * 利用NetworkImageView显示网络图片
	 */
	public static void loadImageByNetworkImageView(RequestQueue mQueue, String imageUrl, NetworkImageView mNetworkImageView) {
		final LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(20);
		ImageCache imageCache = new ImageCache() {
			@Override
			public void putBitmap(String key, Bitmap value) {
				lruCache.put(key, value);
			}

			@Override
			public Bitmap getBitmap(String key) {
				return lruCache.get(key);
			}
		};
		ImageLoader imageLoader = new ImageLoader(mQueue, imageCache);
		mNetworkImageView.setTag("url");
		mNetworkImageView.setImageUrl(imageUrl, imageLoader);
	}

	/**
	 * 拼装url
	 * 
	 * @param relativePath
	 *            原始地址
	 * @param qparams
	 *            参数Map
	 * @return 带参数的url
	 */
	public static String getAbsolutUrl(String relativePath, Map<String, Object> qparams) {
		Uri uri = Uri.parse(relativePath);
		if (qparams != null) {
			Builder builder = uri.buildUpon();
			for (Entry<String, Object> entry : qparams.entrySet()) {
				String entryValue = String.valueOf(entry.getValue());
				if (!entryValue.equalsIgnoreCase("null")) {
					builder.appendQueryParameter(entry.getKey(), entryValue);
				}
			}
			return builder.build().toString();
		}
		return uri.toString();
	}

	/**
	 * 将Unicode字符串转成Utf-8字符串 (暂时注释此方法,此方法会导致Gson类型转换出异常,直接使用Unicode字符串是没问题的)
	 * 
	 * @param theString
	 * @return
	 */
	public static String decodeUnicode(String theString) {
		// char aChar;
		// int len = theString.length();
		// StringBuffer outBuffer = new StringBuffer(len);
		// for (int x = 0; x < len;) {
		// aChar = theString.charAt(x++);
		// if (aChar == '\\') {
		// aChar = theString.charAt(x++);
		// if (aChar == 'u') {
		// // Read the xxxx
		// int value = 0;
		// for (int i = 0; i < 4; i++) {
		// aChar = theString.charAt(x++);
		// switch (aChar) {
		// case '0':
		// case '1':
		// case '2':
		// case '3':
		// case '4':
		// case '5':
		// case '6':
		// case '7':
		// case '8':
		// case '9':
		// value = (value << 4) + aChar - '0';
		// break;
		// case 'a':
		// case 'b':
		// case 'c':
		// case 'd':
		// case 'e':
		// case 'f':
		// value = (value << 4) + 10 + aChar - 'a';
		// break;
		// case 'A':
		// case 'B':
		// case 'C':
		// case 'D':
		// case 'E':
		// case 'F':
		// value = (value << 4) + 10 + aChar - 'A';
		// break;
		// default:
		// throw new
		// IllegalArgumentException("Malformed   \\uxxxx   encoding.");
		// }
		//
		// }
		// outBuffer.append((char) value);
		// } else {
		// if (aChar == 't')
		// aChar = '\t';
		// else if (aChar == 'r')
		// aChar = '\r';
		// else if (aChar == 'n')
		// aChar = '\n';
		// else if (aChar == 'f')
		// aChar = '\f';
		// outBuffer.append(aChar);
		// }
		// } else
		// outBuffer.append(aChar);
		// }
		// return outBuffer.toString();
		return theString;
	}
}
