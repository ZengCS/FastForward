package com.zcs.fast.forward.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.zcs.fast.forward.R;
import com.zcs.fast.forward.base.BaseActivity;
import com.zcs.fast.forward.utils.LogUtil;

public class ExpandableListActivity extends BaseActivity implements OnChildClickListener {
	/** constant */
	private static final String CURR_TITLE = "ExpandableList";
	private static final String NAME = "NAME";
	private static final String IS_EVEN = "IS_EVEN";

	/** Views */
	private ExpandableListView expList;
	private ExpandableListAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expandable_list);
		super.init();
		initExpList();
	}

	private void initExpList() {
		// TODO 初始化ExpandableList
		List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
		List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();

		String[] parentArr = getResources().getStringArray(R.array.exp_parent);
		String[] childArr = getResources().getStringArray(R.array.exp_child);

		// 设置GroupData
		for (String p : parentArr) {
			Map<String, String> curGroupMap = new HashMap<String, String>();
			groupData.add(curGroupMap);
			curGroupMap.put(NAME, p);
			curGroupMap.put(IS_EVEN, "99+");
		}

		for (String cp : childArr) {
			List<Map<String, String>> children = new ArrayList<Map<String, String>>();
			for (String c : cp.split(",")) {
				Map<String, String> curChildMap = new HashMap<String, String>();
				children.add(curChildMap);
				curChildMap.put(NAME, c);
				curChildMap.put(IS_EVEN, "1");
			}
			childData.add(children);
		}

		// Set up our adapter
		mAdapter = new SimpleExpandableListAdapter(this,// Context
				groupData,// groupData
				R.layout.item_exp_group,// layout
				new String[] { NAME, IS_EVEN },// name
				new int[] { R.id.exp_name_group, R.id.exp_count_group },// id
				childData,// childData
				R.layout.item_exp_child,// layout
				new String[] { NAME, IS_EVEN },// data
				new int[] { R.id.exp_name_child, R.id.exp_count_child }// id
		);
		expList.setAdapter(mAdapter);

		expList.setOnChildClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.titlebtn_left_act:
			finish();
			break;
		case R.id.titlebtn_right_act:// 查看
			displayHelpDialog();
			break;
		default:
			break;
		}
	}

	@Override
	protected void initTitlebar() {
		// TODO 初始化标题栏
		super.titlebarView = findViewById(R.id.titlebar_expandablelist);

		super.titleBtnLeft = (LinearLayout) titlebarView.findViewById(R.id.titlebtn_left_act);
		super.titleBtnRight = (ImageView) titlebarView.findViewById(R.id.titlebtn_right_act);
		super.titleTxtCenter = (TextView) titlebarView.findViewById(R.id.titletxt_center_act);

		super.titleTxtCenter.setText(CURR_TITLE);

		titleBtnLeft.setOnClickListener(this);
		titleBtnRight.setOnClickListener(this);

		LogUtil.d(TAG, "initTitlebar complete");
	}

	@Override
	protected void initComponent() {
		// TODO 初始化组件
		expList = (ExpandableListView) findViewById(R.id.exp_list);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		Map<String, String> map = (Map<String, String>) mAdapter.getChild(groupPosition, childPosition);
		showToast(map.get("NAME"));
		return false;
	}

}
