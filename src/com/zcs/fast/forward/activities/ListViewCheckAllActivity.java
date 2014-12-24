package com.zcs.fast.forward.activities;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zcs.fast.forward.R;
import com.zcs.fast.forward.adapter.CheckListAdapter;
import com.zcs.fast.forward.base.BaseActivity;
import com.zcs.fast.forward.entity.CheckItem;
import com.zcs.fast.forward.utils.LogUtil;

public class ListViewCheckAllActivity extends BaseActivity implements OnItemClickListener {
	/** constant */
	private static final String CURR_TITLE = "EmptyActivity";

	/** Views */
	private ListView mListView;// 列表
	private CheckListAdapter mAdapter;
	private List<CheckItem> itemList;
	private Button checkAll, checkNull;// 全选,全不选按钮

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_view_check_all);
		super.init();
		initListView();
	}

	/**
	 * 初始化列表
	 */
	private void initListView() {
		itemList = new ArrayList<CheckItem>();
		for (int i = 0; i < 20; i++) {
			CheckItem item = new CheckItem(i, "Name-" + i, false);
			itemList.add(item);
		}
		mAdapter = new CheckListAdapter(this, itemList);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO 列表元素点击事件
		CheckItem item = (CheckItem) mAdapter.getItem(position);
		// 设置当前的选择状态与上次的相反,保证能在选中和非选中状态之间切换
		item.setCheck(!item.isCheck());
		// 通知ListView刷新数据
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.titlebtn_left_act:// 返回
			finish();
			break;
		case R.id.titlebtn_right_act:// 查看
			displayHelpDialog();
			break;
		case R.id.btn_check_all:// 全选
			checkAll(true);
			break;
		case R.id.btn_check_null:// 全不选
			checkAll(false);
			break;
		default:
			break;
		}
	}

	/**
	 * 全选
	 */
	private void checkAll(boolean checkState) {
		for (CheckItem item : itemList) {
			item.setCheck(checkState);
		}
		// 通知ListView刷新数据
		mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void initTitlebar() {
		// TODO 初始化标题栏
		super.titlebarView = findViewById(R.id.titlebar_check_all);

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
		mListView = (ListView) findViewById(R.id.check_listview);

		// 全选和全不选按钮
		checkAll = (Button) findViewById(R.id.btn_check_all);
		checkNull = (Button) findViewById(R.id.btn_check_null);

		checkAll.setOnClickListener(this);
		checkNull.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
