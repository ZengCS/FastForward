package com.zcs.fast.forward.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.zcs.fast.forward.utils.DisplayUtil;

public class CustomProgressBar extends View {
	public static final boolean NEEX_DRAW_TEXT = false;
	private int max = 100;
	private int progress = 0;
	private int progressColor = Color.parseColor("#3BB753");
	private Context mContext;

	public CustomProgressBar(Context context) {
		super(context);
		mContext = context;
	}

	public CustomProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	public CustomProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		System.out.println("***************onDraw***************");
		System.out.println(this.getWidth() + "*" + this.getHeight());
		System.out.println("Progress:" + progress);
		int w = this.getWidth();
		int h = this.getHeight();

		Paint paint = new Paint();
		paint.setColor(progressColor);

		paint.setAntiAlias(true);
		float draw = ((float) progress / (float) max) * w;
		RectF rectf = new RectF(0, 0, draw, h);
		canvas.drawRect(rectf, paint);

		if (NEEX_DRAW_TEXT) {
			int percent = (int) (((float) progress / (float) max) * 100); // 中间的进度百分比，先转换成float在进行除法运算，不然都为0
			float textWidth = paint.measureText(percent + " %");
			float textSize = DisplayUtil.dip2px(mContext, 16);
			paint.setTextSize(textSize);
			paint.setStrokeWidth(2);
			paint.setColor(Color.parseColor("#FF0000"));

			// 让文本居中
			// canvas.drawText(percent + " %", (w - textWidth) / 2, h - 3 - (h -
			// textSize) / 2, paint);
			// 让文本右对齐
			canvas.drawText(percent + " %", (w - textWidth - DisplayUtil.dip2px(mContext, 30)), h - 3 - (h - textSize) / 2, paint);
		}
	}

	/**
	 * 获取进度.需要同步
	 * 
	 * @return
	 */
	public synchronized int getProgress() {
		return progress;
	}

	/**
	 * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步 刷新界面调用postInvalidate()能在非UI线程刷新
	 * 
	 * @param progress
	 */
	public synchronized void setProgress(int progress) {
		if (progress < 0) {
			throw new IllegalArgumentException("progress not less than 0");
		}
		if (progress > max) {
			progress = max;
		}
		if (progress <= max) {
			this.progress = progress;
			postInvalidate();
		}
	}

	public void setProgressColor(int progressColor) {
		this.progressColor = progressColor;
	}
}
