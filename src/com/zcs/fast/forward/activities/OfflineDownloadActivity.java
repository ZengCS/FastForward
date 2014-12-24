package com.zcs.fast.forward.activities;

import java.io.File;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zcs.fast.forward.Downloader;
import com.zcs.fast.forward.R;
import com.zcs.fast.forward.adapter.DownloadListAdapter;
import com.zcs.fast.forward.base.BaseActivity;
import com.zcs.fast.forward.dao.DownloadInfoDao;
import com.zcs.fast.forward.entity.DownloadInfoEntity;
import com.zcs.fast.forward.sqlite.GreenDAOManger;
import com.zcs.fast.forward.utils.FileUtils;
import com.zcs.fast.forward.utils.LogUtil;
import com.zcs.fast.forward.utils.dialog.DialogUtil;
import com.zcs.fast.forward.widget.CustomProgressBar;

@SuppressLint("HandlerLeak")
public class OfflineDownloadActivity extends BaseActivity {
	/** constant */
	public static final String TAG = "Download";
	public static final String CURR_TITLE = "离线断点续传";
	public static final String VIDEO_HOST = "http://192.168.23.1:8090/examples/res/video_";
	public static final String VIDEO_TITLE = "Android-深入浅出,从此走上人生巅峰系列";
	public static final int MAX_DOWN_COUNT = 33;

	/** Views */
	private Button btnInit, btnDel, btnStop;
	private ListView downListView;
	private Dialog bottomDialog;

	private List<DownloadInfoEntity> downloadList;
	private DownloadListAdapter mAdapter;

	/** GreenDAO */
	private GreenDAOManger daoManger;
	private DownloadInfoDao downloadInfoDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offline_download);
		Downloader.init(this, mHandler);
		super.init();

		// TODO 初始化数据库
		daoManger = GreenDAOManger.getInstance();
		downloadInfoDao = daoManger.getDaoSession().getDownloadInfoDao();

		downloadList = downloadInfoDao.loadAll();

		if (downloadList.size() <= 0) {
			initDownList();
		}
		bindData2List();
	}

	/**
	 * 初始化数据
	 */

	/**
	 * 绑定数据到List
	 */
	private void bindData2List() {
		if (mAdapter == null) {
			mAdapter = new DownloadListAdapter(this, downloadList);
			downListView.setAdapter(mAdapter);

			downListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					DownloadInfoEntity item = (DownloadInfoEntity) mAdapter.getItem(position);
					switch (item.getDownState()) {
					case DownloadInfoEntity.COMPLETE:// 下载完成
						showToast("播放视频:" + item.getName());
						break;
					case DownloadInfoEntity.DOWNLOADING:// 正在下载
						showToast("点击：正在下载，进入暂停状态。");
						item.setDownState(DownloadInfoEntity.PAUSE);
						break;
					case DownloadInfoEntity.PAUSE:// 暂停状态
						// TODO 进入等待状态
						showToast("已加入下载队列！");
						item.setDownState(DownloadInfoEntity.WAIT);
						Downloader.addDownloadTask(item);
						break;
					case DownloadInfoEntity.WAIT:// 等待下载
						showToast("正在等待...");
						break;
					case DownloadInfoEntity.ERROR:// 下载错误
						// TODO 进入等待状态
						showToast("重新下载！");
						item.setDownState(DownloadInfoEntity.WAIT);
						Downloader.addDownloadTask(item);
						break;
					default:
						break;
					}
					mAdapter.notifyDataSetChanged();
				}
			});
		} else {
			mAdapter.notifyDataSetChanged(downloadList);
		}
	}

	/**
	 * 初始化下载列表
	 */
	private void initDownList() {
		for (int i = 1; i <= MAX_DOWN_COUNT; i++) {
			DownloadInfoEntity d = new DownloadInfoEntity();
			d.setId((long) i);
			if (i < 10) {
				d.setName(VIDEO_TITLE + "-0" + i + ".mp4");
				d.setUrl(VIDEO_HOST + "0" + i + ".mp4");
			} else {
				d.setName(VIDEO_TITLE + "-" + i + ".mp4");
				d.setUrl(VIDEO_HOST + i + ".mp4");
			}
			d.setLength(0);
			d.setDownSize(0);
			d.setPercent(0);
			d.setDownState(DownloadInfoEntity.PAUSE);

			downloadList.add(d);
		}
		downloadInfoDao.coverList(downloadList);
	}

	public static final int WHAT_NOTIFY_DOWN_LIST = 0;
	public static final int WHAT_SET_CURR_ITEM = 1;

	private Handler mHandler = new Handler() {
		DownloadInfoEntity item = null;

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case WHAT_SET_CURR_ITEM:// 设置当前下载的对象
				item = (DownloadInfoEntity) msg.obj;
				for (DownloadInfoEntity entity : downloadList) {
					if (item == entity || item.equals(entity) || item.getId() == entity.getId()) {
						continue;
					}
					if (entity.getDownState() == DownloadInfoEntity.DOWNLOADING && entity.getPercent() < 100) {
						entity.setDownState(DownloadInfoEntity.ERROR);
					}
				}
				try {
					mAdapter.notifyDataSetChanged();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case WHAT_NOTIFY_DOWN_LIST:// 更新下载进度
				item = (DownloadInfoEntity) msg.obj;
				if (item != null) {
					// LogUtil.d(TAG, "更新下载进度[" + item.getName() + "],已完成:" +
					// item.getPercent() + "%");
					if (item.getPercent() >= 100) {
						item.setDownState(DownloadInfoEntity.COMPLETE);
						mAdapter.notifyDataSetChanged();
					} else {
						notifySingle(item);
					}
					downloadInfoDao.update(item);
				}
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 更新单个对象
	 * 
	 * @param item
	 */
	private void notifySingle(DownloadInfoEntity item) {
		// 获取到当前对象的真实Position位置
		int position = item.getPosition();
		// 获取当前列表的第一个元素的Position
		int firstPos = downListView.getFirstVisiblePosition();
		// 获取到当前对象对应的View
		View currView = downListView.getChildAt(position - firstPos);
		LogUtil.d("notifySingle", "position:" + position + ", firstPos:" + firstPos);

		if (currView != null) {
			LogUtil.d("notifySingle", "currView:" + currView);
			TextView downPercent = (TextView) currView.findViewById(R.id.down_percent);
			TextView downDesc = (TextView) currView.findViewById(R.id.down_desc);
			CustomProgressBar progress = (CustomProgressBar) currView.findViewById(R.id.down_percent_progress);

			downPercent.setText(item.getPercent() + "%");
			downPercent.setTextColor(0xFF999999);
			// 设置下载大小/总大小
			downDesc.setText(FileUtils.formatFileSize(item.getDownSize()) + " / " + FileUtils.formatFileSize(item.getLength()));
			progress.setProgress(item.getPercent());
		} else {
			// 当前需要更新的对象不可见
			LogUtil.w("notifySingle", "currView is invisible");
		}
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
		case R.id.btn_init:// 初始化
			if (downloadList.size() > 0) {
				showToast("数据已存在！");
			} else {
				if (downloadList.size() <= 0) {
					initDownList();
				}
				mAdapter.notifyDataSetChanged(downloadList);
			}
			break;
		case R.id.btn_delete:// 删除
			if (bottomDialog == null) {
				initBottomDialog();
			}
			bottomDialog.show();
			break;
		case R.id.btn_stop:// 停止
			shutdown();
			break;
		case R.id.dialog_bottom_btn_ok:// 确认删除
			File folder = Environment.getExternalStoragePublicDirectory(Downloader.DOWNLOAD_FOLDER_NAME);
			FileUtils.deleteDir(folder);

			downloadInfoDao.deleteAll();
			downloadList.clear();
			mAdapter.notifyDataSetChanged();

			bottomDialog.dismiss();
			break;
		case R.id.dialog_bottom_btn_cancel:// 底部弹出对话框-取消
			bottomDialog.dismiss();
			break;
		default:
			break;
		}
	}

	/**
	 * 停止下载
	 */
	private void shutdown() {
		// TODO 先暂停,再shutdown
		Downloader.setPause(true);

		for (DownloadInfoEntity entity : downloadList) {
			if (entity.getDownState() == DownloadInfoEntity.WAIT || entity.getDownState() == DownloadInfoEntity.DOWNLOADING) {
				entity.setDownState(DownloadInfoEntity.PAUSE);
			}
			downloadInfoDao.update(entity);
		}
		mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void initTitlebar() {
		// TODO 初始化标题栏
		super.titlebarView = findViewById(R.id.titlebar_download);

		super.titleBtnLeft = (LinearLayout) titlebarView.findViewById(R.id.titlebtn_left_act);
		super.titleBtnRight = (ImageView) titlebarView.findViewById(R.id.titlebtn_right_act);
		super.titleTxtCenter = (TextView) titlebarView.findViewById(R.id.titletxt_center_act);

		super.titleTxtCenter.setText(CURR_TITLE);

		titleBtnLeft.setOnClickListener(this);
		titleBtnRight.setOnClickListener(this);

		LogUtil.d(TAG, "initTitlebar complete");
	}

	/**
	 * 初始化底部Dialog
	 */
	private void initBottomDialog() {
		View dialogView = getLayoutInflater().inflate(R.layout.dialog_bottom, null);
		TextView titleView = (TextView) dialogView.findViewById(R.id.dialog_bottom_title);

		titleView.setText("即将删除所有下载记录（包括视频文件）!");
		((Button) dialogView.findViewById(R.id.dialog_bottom_btn_ok)).setText(getString(R.string.down_delete_ok));
		dialogView.findViewById(R.id.dialog_bottom_btn_ok).setOnClickListener(this);
		dialogView.findViewById(R.id.dialog_bottom_btn_cancel).setOnClickListener(this);

		// 创建Dialog
		bottomDialog = DialogUtil.createBottomDialog(dialogView, this);
	}

	@Override
	protected void initComponent() {
		// TODO 初始化组件
		btnInit = (Button) findViewById(R.id.btn_init);
		btnDel = (Button) findViewById(R.id.btn_delete);
		btnStop = (Button) findViewById(R.id.btn_stop);

		btnInit.setOnClickListener(this);
		btnDel.setOnClickListener(this);
		btnStop.setOnClickListener(this);

		downListView = (ListView) findViewById(R.id.down_list);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		downloadList.clear();
	}

}
