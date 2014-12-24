package com.zcs.fast.forward.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.zcs.fast.forward.R;
import com.zcs.fast.forward.utils.DrawableUtil;

@SuppressLint("DrawAllocation")
public class GifView extends View {
	private Movie mMovie;
	private long mMovieStart;
	private String gifName;

	public GifView(Context context) {
		this(context, null);
	}

	public GifView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public GifView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setFocusable(true);

		TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.GifView);

		// 获取自定义属性和默认值
		gifName = mTypedArray.getString(R.styleable.GifView_gif_name);
		initMovie();
		mTypedArray.recycle();
	}

	/**
	 * 初始化Movie对象
	 */
	private void initMovie() {
		try {
			int gifId = DrawableUtil.getDrawableIdByName(gifName);
			mMovie = Movie.decodeStream(getResources().openRawResource(gifId));
		} catch (Exception e) {
			mMovie = null;
			e.printStackTrace();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// 设置画布背景颜色
		canvas.drawColor(0xFFFFFFFF);

		// 消除锯齿
		Paint paint = new Paint();
		paint.setAntiAlias(true);

		long now = android.os.SystemClock.uptimeMillis();
		if (mMovieStart == 0) {// first time
			mMovieStart = now;
		}
		if (mMovie != null) {
			int dur = mMovie.duration();
			if (dur == 0) {
				dur = 1000;
			}
			int relTime = (int) ((now - mMovieStart) % dur);
			mMovie.setTime(relTime);
			// 设置居中
			int x = (getWidth() - mMovie.width()) / 2;
			int y = (getHeight() - mMovie.height()) / 2;
			mMovie.draw(canvas, x, y);
			// 强制重绘
			invalidate();
		} else {
			paint.setColor(Color.RED);
			paint.setTextSize(24);
			float textWidth = paint.measureText("无效图片"); // 测量字体宽度
			int x = (int) ((getWidth() - textWidth) / 2);
			int y = (getHeight() + 24) / 2;
			canvas.drawText("无效图片", x, y, paint);
		}
	}

	/**
	 * 动态设置图片
	 * 
	 * @param gifName
	 */
	public void setGifName(String gifName) {
		this.gifName = gifName;
		initMovie();
		invalidate();
	}
}
