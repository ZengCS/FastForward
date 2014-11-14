package com.zcs.fast.forward.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zcs.fast.forward.R;
import com.zcs.fast.forward.activities.EmptyActivity;
import com.zcs.fast.forward.base.BaseFragment;
import com.zcs.fast.forward.utils.LogUtil;

public class MainFragment extends BaseFragment {
	/** My Views */
	private Button btnLogin;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(TAG, "MainFragment onCreateView called!");
		root = inflater.inflate(R.layout.fragment_main, null);
		super.init();
		return root;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:// 登录
			Intent mIntent = new Intent(getActivity(), EmptyActivity.class);
			startActivity(mIntent);
			break;
		default:
			break;
		}
	}

	@Override
	protected void initComponent() {
		// TODO init
		btnLogin = (Button) root.findViewById(R.id.btn_login);
		btnLogin.setOnClickListener(this);

		LogUtil.d(TAG, "initComponent complete");
	}
}
