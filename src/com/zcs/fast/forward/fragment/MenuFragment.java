package com.zcs.fast.forward.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zcs.fast.forward.R;
import com.zcs.fast.forward.base.BaseFragment;

public class MenuFragment extends BaseFragment {
	private View root;
	private Button btnExit;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_menu, null);
		super.init();
		return root;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_menu_exit:
			mListener.exitApp();
			break;

		default:
			break;
		}
	}

	@Override
	protected void initComponent() {
		btnExit = (Button) root.findViewById(R.id.btn_menu_exit);
		btnExit.setOnClickListener(this);
	}
}
