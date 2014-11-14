package com.zcs.fast.forward.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zcs.fast.forward.R;
import com.zcs.fast.forward.base.BaseFragment;

public class AccountFragment extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(TAG, "AccountFragment onCreateView called!");
		root = inflater.inflate(R.layout.fragment_account, null);
		super.init();
		return root;
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
