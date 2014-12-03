package com.zcs.fast.forward.activities;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.reflect.TypeToken;
import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.lee.pullrefresh.ui.PullToRefreshListView;
import com.zcs.fast.forward.R;
import com.zcs.fast.forward.adapter.RecommendListAdapter;
import com.zcs.fast.forward.base.BaseActivity;
import com.zcs.fast.forward.dao.DaoMaster;
import com.zcs.fast.forward.dao.DaoMaster.DevOpenHelper;
import com.zcs.fast.forward.dao.DaoSession;
import com.zcs.fast.forward.dao.RecommendDao;
import com.zcs.fast.forward.entity.RecommendEntity;
import com.zcs.fast.forward.http.ServerHost;
import com.zcs.fast.forward.utils.LogUtil;
import com.zcs.fast.forward.utils.PullRefreshUtil;
import com.zcs.fast.forward.utils.SharedPreferencesUtil;
import com.zcs.fast.forward.utils.VolleyUtil;

@SuppressLint("HandlerLeak")
public class RecommendCourseActivity extends BaseActivity implements OnRefreshListener<ListView>, OnItemClickListener {
	/** constant */
	private static final String CURR_TITLE = "推荐课程";

	protected static final int WHAT_GET_RECOMMEND_SUCCESS = 0;
	protected static final int WHAT_GET_DATA_FAIL = 1;

	private boolean isNetworkAble = true;

	private int mStart = 15; // 起始加载数据数量
	private int mPageSize = 15; // 每次增加数据数量

	/** Views */
	private TextView stateTip;// 加载状态

	private PullToRefreshListView mPullListView;
	private ListView mListView;
	private RecommendListAdapter mAdapter;
	private RequestQueue mQueue;
	private List<RecommendEntity> mList;// 总的List
	private List<RecommendEntity> mShowList;// 需展示的List

	/** GreenDAO */
	private SQLiteDatabase db;
	private DaoMaster daoMaster;
	private DaoSession daoSession;
	private RecommendDao dao;

	// private Cursor cursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recommend_course);
		super.init();
		// TODO 设置上次更新时间
		setLastUpdateTimeFromCache();

		// TODO 初始化工具对象
		mQueue = Volley.newRequestQueue(this);
		mList = new ArrayList<RecommendEntity>(0);
		mShowList = new ArrayList<RecommendEntity>(0);

		// TODO 初始化数据库
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "fast-forward-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
		dao = daoSession.getRecommendDao();
		mList = dao.loadAll();
		if (mList.size() > 0) {
			// TODO Step1: 存在数据库缓存数据
			displayData(true);
			showTipMsg(getString(R.string.data_load_db), true);
		} else {
			// TODO Step2: 判断网络是否可用
			if (isNetworkAble) {
				// TODO Step3-1: 从网络加载数据
				mPullListView.doPullRefreshing(true, 200);
			} else {
				// TODO Step3-2: 告知网络错误
				showToast("请开启网络！");
			}
		}
	}

	/**
	 * 展示数据到ListView
	 * 
	 * @param isRefresh
	 *            是否是刷新整个ListView
	 */
	private void displayData(boolean isRefresh) {
		// 初始化Adapter
		mAdapter = new RecommendListAdapter(this, mQueue, mShowList);
		mListView.setAdapter(mAdapter);

		loadMoreData(isRefresh);
		// if (mList.size() < mPageSize) {
		// mShowList = mList;
		// } else {
		// for (int i = 0; i < mPageSize; i++) {
		// mShowList.add(mList.get(i));
		// }
		// }
		//
		// mAdapter.notifyDataSetChanged(mShowList);
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case WHAT_GET_RECOMMEND_SUCCESS:
				showTipMsg(getString(R.string.data_load_success), true);
				mPullListView.onPullDownRefreshComplete();
				setLastUpdateTime();
				displayData(true);
				// 覆盖数据库
				dao.coverList(mList);
				break;
			case WHAT_GET_DATA_FAIL:
				showTipMsg(getString(R.string.data_load_failed), true);
				mPullListView.onPullDownRefreshComplete();
				break;
			case PullRefreshUtil.SHOW_TIP_LATER_ALWAYS:
				// TODO 延时展示提示信息,不自动隐藏
				stateTip.setVisibility(View.VISIBLE);
				break;
			case PullRefreshUtil.SHOW_TIP_LATER:
				stateTip.setVisibility(View.VISIBLE);
				// TODO 开启定时隐藏
				this.removeMessages(PullRefreshUtil.AUTO_HIDE_TIP);
				this.sendEmptyMessageDelayed(PullRefreshUtil.AUTO_HIDE_TIP, PullRefreshUtil.SHOW_TIP_DURATION);
				break;
			case PullRefreshUtil.AUTO_HIDE_TIP:// TODO 关闭提示信息
				stateTip.setVisibility(View.GONE);
				break;
			case PullRefreshUtil.LOAD_MORE_DATA_LATER:// TODO 加载更多
				loadMoreData(false);
			default:
				break;
			}
		}
	};

	/**
	 * 根据是刷新数据还是上拉加载更多，显示不同数量item
	 */
	private void loadMoreData(boolean isUpdateData) {
		boolean hasMoreData = true;

		int start = mStart;
		int end = mStart + mPageSize;

		if (isUpdateData) {
			mShowList.clear();
			start = 0;
			end = mPageSize;
		}
		if (end >= mList.size()) {
			end = mList.size();
			// 数据已加载全部
			hasMoreData = false;
		}
		if (end == 0) {
			return;
		}

		for (int i = start; i < end; i++) {
			mShowList.add(mList.get(i));
		}

		mAdapter.notifyDataSetChanged();

		mPullListView.onPullUpRefreshComplete();
		mStart = end;
		mPullListView.setHasMoreData(hasMoreData);
	}

	/**
	 * 获取推荐数据
	 */
	private void getDataFromNet() {
		Map<String, Object> qparams = new HashMap<String, Object>();
		qparams.put("client", "android");
		String url = VolleyUtil.getAbsolutUrl(ServerHost.RECOMMEND_COURSE, qparams);

		StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// GSON解析
				Type type = new TypeToken<List<RecommendEntity>>() {
				}.getType();
				mList = gson.fromJson(response, type);

				if (mList.size() > 0) {
					mHandler.sendEmptyMessage(WHAT_GET_RECOMMEND_SUCCESS);
				} else {
					mHandler.sendEmptyMessage(WHAT_GET_DATA_FAIL);
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(TAG, error.getMessage(), error);
				mHandler.sendEmptyMessage(WHAT_GET_DATA_FAIL);
			}
		});
		mQueue.add(stringRequest);
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
		super.titlebarView = findViewById(R.id.titlebar_recommend);

		super.titleBtnLeft = (LinearLayout) titlebarView.findViewById(R.id.titlebtn_left_act);
		super.titleBtnRight = (ImageView) titlebarView.findViewById(R.id.titlebtn_right_act);
		super.titleTxtCenter = (TextView) titlebarView.findViewById(R.id.titletxt_center_act);

		super.titleTxtCenter.setText(CURR_TITLE);

		titleBtnLeft.setOnClickListener(this);
		titleBtnRight.setOnClickListener(this);

		LogUtil.d(TAG, "initTitlebar complete");
	}

	/**
	 * 显示提示信息
	 * 
	 * @param msg
	 *            信息内容
	 * @param autoHide
	 *            是否自动隐藏
	 */
	private void showTipMsg(String msg, boolean autoHide) {
		stateTip.setText(msg);
		if (autoHide) {
			mHandler.removeMessages(PullRefreshUtil.SHOW_TIP_LATER);
			mHandler.sendEmptyMessageDelayed(PullRefreshUtil.SHOW_TIP_LATER, PullRefreshUtil.ANIM_DURATION);
		} else {// 一直显示,直到数据加载完成
			mHandler.removeMessages(PullRefreshUtil.SHOW_TIP_LATER_ALWAYS);
			mHandler.sendEmptyMessageDelayed(PullRefreshUtil.SHOW_TIP_LATER_ALWAYS, PullRefreshUtil.ANIM_DURATION);
		}
	}

	@Override
	protected void initComponent() {
		// TODO 初始化组件
		stateTip = (TextView) findViewById(R.id.state_tip);

		// 初始化可下拉刷新组件
		mPullListView = (PullToRefreshListView) findViewById(R.id.pull_listview_recomend_list);
		mPullListView.setPullLoadEnabled(false);
		mPullListView.setScrollLoadEnabled(true);

		// 从mPullListView中获取可刷新的ListView对象
		mListView = mPullListView.getRefreshableView();
		mListView.setDividerHeight(0);
		mListView.setDivider(null);

		mPullListView.setOnRefreshListener(this);
		mListView.setOnItemClickListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO 下拉刷新
		getDataFromNet();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO 上拉加载更多
		mHandler.removeMessages(PullRefreshUtil.LOAD_MORE_DATA_LATER);
		mHandler.sendEmptyMessageDelayed(PullRefreshUtil.LOAD_MORE_DATA_LATER, PullRefreshUtil.ANIM_DURATION);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO 列表元素点击
		RecommendEntity entity = (RecommendEntity) mAdapter.getItem(position);
		// showTipMsg(entity.getCourse_title(), true);
		showToast(entity.getCourse_title());
	}

	/**
	 * 从缓存中读取上次更新时间
	 */
	private void setLastUpdateTimeFromCache() {
		String temp = (String) SharedPreferencesUtil.getParam(this, "lastUpdateTime_" + getClass().getSimpleName(), "");
		LogUtil.d(TAG, "cache time:" + temp);
		if (temp.isEmpty()) {
			setLastUpdateTime();
		} else {
			mPullListView.setLastUpdatedLabel(temp);
		}
	}

	private void setLastUpdateTime() {
		LogUtil.d(TAG, "setLastUpdateTime called");

		String text = PullRefreshUtil.mDateFormat.format(new Date());
		mPullListView.setLastUpdatedLabel(text);
		SharedPreferencesUtil.setParam(this, "lastUpdateTime_" + getClass().getSimpleName(), text);
	}
}
