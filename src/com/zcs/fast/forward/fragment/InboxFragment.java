package com.zcs.fast.forward.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zcs.fast.forward.R;
import com.zcs.fast.forward.base.BaseFragment;

public class InboxFragment extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(TAG, "InboxFragment onCreateView called!");
		root = inflater.inflate(R.layout.fragment_inbox, null);
		super.init();
		mListener.showToast("初始化:InboxFragment");
		return root;
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if (hidden) {// 被隐藏
			Log.d(TAG, "InboxFragment hidden");
		} else {// 被显示
			Log.d(TAG, "InboxFragment display");
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void initComponent() {
		// TODO Auto-generated method stub
	}

}
