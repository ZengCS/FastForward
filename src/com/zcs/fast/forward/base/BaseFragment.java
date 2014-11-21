package com.zcs.fast.forward.base;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;

public abstract class BaseFragment extends Fragment implements OnClickListener {
	protected final static String TAG = "BaseFragment";

	protected View root;
	protected MainListener mListener;
	protected Context mContext;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mListener = (MainListener) activity;
		mContext = activity;
	}

	/**
	 * 初始化布局,内部调用initTitlebar()和initComponent()
	 */
	protected void init() {
		initComponent();
	}

	/**
	 * 初始化组件
	 */
	protected abstract void initComponent();
}
