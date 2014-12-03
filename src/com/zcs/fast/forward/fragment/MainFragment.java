package com.zcs.fast.forward.fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.lee.pullrefresh.ui.PullToRefreshGridView;
import com.zcs.fast.forward.R;
import com.zcs.fast.forward.adapter.MainGridAdapter;
import com.zcs.fast.forward.base.BaseFragment;
import com.zcs.fast.forward.commons.MainGridDataProvider;
import com.zcs.fast.forward.entity.MainGridItemEntity;
import com.zcs.fast.forward.utils.LogUtil;
import com.zcs.fast.forward.utils.PullRefreshUtil;

@SuppressLint("HandlerLeak")
public class MainFragment extends BaseFragment {
	protected static final int HIDE_LOADING_STATE_DELAY = 0;// 延时隐藏
	/** My Views */

	/** PullToRefresh */
	private PullToRefreshGridView mPullGridView;
	private GridView mGridView;
	private MainGridAdapter mGridAdapter;
	private List<MainGridItemEntity> mGridList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(TAG, "MainFragment onCreateView called!");
		root = inflater.inflate(R.layout.fragment_main, null);
		super.init();
		initMainGridView();
		return root;
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case HIDE_LOADING_STATE_DELAY:
				mPullGridView.onPullDownRefreshComplete();
				mPullGridView.onPullUpRefreshComplete();

				setLastUpdateTime();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 初始化GridView
	 */
	private void initMainGridView() {
		mPullGridView = (PullToRefreshGridView) root.findViewById(R.id.main_gridview);

		mPullGridView.setPullLoadEnabled(false);
		mPullGridView.setScrollLoadEnabled(false);

		mGridView = mPullGridView.getRefreshableView();
		mGridView.setNumColumns(3);
		mGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		mGridView.setSelector(R.color.transparent);

		// 初始化数据List
		if (mGridList == null || mGridList.size() == 0) {
			mGridList = new ArrayList<MainGridItemEntity>(0);
			for (int i = 0; i < MainGridDataProvider.names.length; i++) {
				try {
					MainGridItemEntity m = new MainGridItemEntity();
					m.setName(MainGridDataProvider.names[i]);
					m.setIconId(MainGridDataProvider.icons[i]);
					m.setActivityClass(MainGridDataProvider.targets[i]);
					mGridList.add(m);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}

		mGridAdapter = new MainGridAdapter(mContext, mGridList);
		mGridView.setAdapter(mGridAdapter);
		// 添加消息处理
		mGridView.setOnItemClickListener(new GridItemClickListener());

		mPullGridView.setOnRefreshListener(new OnRefreshListener<GridView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
				mHandler.removeMessages(HIDE_LOADING_STATE_DELAY);
				mHandler.sendEmptyMessageDelayed(0, 2000);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
				// TODO 上拉加载更多,这里没有更多
			}
		});
		setLastUpdateTime();
		// mPullGridView.doPullRefreshing(true, 200);
	}

	// GridView的Item点击时触发
	private class GridItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
			MainGridItemEntity item = (MainGridItemEntity) mGridAdapter.getItem(position);

			Class<?> targetActivity = (Class<?>) item.getActivityClass();
			mListener.changeActivity(targetActivity);
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if (hidden) {// 被隐藏
			Log.d(TAG, "MainFragment hidden");
		} else {// 被显示
			Log.d(TAG, "MainFragment display");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case 0:
			break;
		default:
			break;
		}
	}

	@Override
	protected void initComponent() {
		// TODO 初始化组件
		LogUtil.d(TAG, "initComponent complete");
	}

	public void setLastUpdateTime() {
		String text = formatDateTime(System.currentTimeMillis());
		mPullGridView.setLastUpdatedLabel(text);
	}

	private String formatDateTime(long time) {
		if (0 == time) {
			return "";
		}
		return PullRefreshUtil.mDateFormat.format(new Date(time));
	}
}
