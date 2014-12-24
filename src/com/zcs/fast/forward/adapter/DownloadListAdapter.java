package com.zcs.fast.forward.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zcs.fast.forward.R;
import com.zcs.fast.forward.entity.DownloadInfoEntity;
import com.zcs.fast.forward.utils.FileUtils;
import com.zcs.fast.forward.widget.CustomProgressBar;

@SuppressLint("InflateParams")
public class DownloadListAdapter extends BaseAdapter {
	private final String TAG = "DownloadListAdapter";

	private List<DownloadInfoEntity> mItems;
	private LayoutInflater mInflater;
	private Context context;

	public DownloadListAdapter(Context context, List<DownloadInfoEntity> DownloadInfos) {
		super();
		Log.d(TAG, "new DownloadListAdapter");
		this.context = context;
		mItems = DownloadInfos;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_offline_down, null);

			holder = new ViewHolder();
			holder.downState = (ImageView) convertView.findViewById(R.id.down_state);
			holder.downName = (TextView) convertView.findViewById(R.id.down_name);
			holder.downDesc = (TextView) convertView.findViewById(R.id.down_desc);
			holder.downPercent = (TextView) convertView.findViewById(R.id.down_percent);
			holder.progress = (CustomProgressBar) convertView.findViewById(R.id.down_percent_progress);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		DownloadInfoEntity item = mItems.get(position);
		item.setPosition(position);

		// TODO 根据不同的下载状态设置不同的图标和进度条颜色
		switch (item.getDownState()) {
		case DownloadInfoEntity.DOWNLOADING:// 正在下载
			holder.downState.setImageResource(R.drawable.down);
			holder.progress.setProgressColor(Color.parseColor("#3BB753"));
			break;
		case DownloadInfoEntity.PAUSE:// 已暂停
			holder.downState.setImageResource(R.drawable.down_pause);
			holder.progress.setProgressColor(Color.parseColor("#AAAAAA"));
			break;
		case DownloadInfoEntity.WAIT:// 等待下载
			holder.downState.setImageResource(R.drawable.down_wait);
			holder.progress.setProgressColor(Color.parseColor("#CCCCCC"));
			break;
		case DownloadInfoEntity.ERROR:// 下载错误
			holder.downState.setImageResource(R.drawable.down_error);
			holder.progress.setProgressColor(Color.parseColor("#E96B69"));
			break;
		case DownloadInfoEntity.COMPLETE:// 下载完成
			holder.downState.setImageResource(R.drawable.down_complete);
			holder.progress.setProgressColor(Color.parseColor("#18CC80"));
			break;
		default:
			break;
		}
		holder.downName.setText(item.getName());
		if (item.getDownSize() > 0 || item.getLength() > 0) {
			// TODO 设置下载大小/总大小
			holder.downDesc.setText(FileUtils.formatFileSize(item.getDownSize()) + " / " + FileUtils.formatFileSize(item.getLength()));
		} else if (item.getDownState() == DownloadInfoEntity.WAIT) {
			// <string name="down_click_remove">已加入下载队列，点击移除</string>
			holder.downDesc.setText(context.getString(R.string.down_click_remove));
		} else {
			// <string name="down_click_start">点击开始下载</string>
			holder.downDesc.setText(context.getString(R.string.down_click_start));
		}
		// 隐藏下载百分比进度
		if (item.getPercent() >= 100) {
			holder.downPercent.setVisibility(View.GONE);
		} else {
			holder.downPercent.setVisibility(View.VISIBLE);
		}
		holder.downPercent.setText(item.getPercent() + "%");
		holder.progress.setProgress(item.getPercent());

		return convertView;
	}

	private static class ViewHolder {
		/**
		 * 当前的下载状态,分别包括:下载中、等待中、暂停、错误
		 */
		ImageView downState;

		/**
		 * 当前下载对象的名称
		 */
		TextView downName;

		/**
		 * 当前下载进度百分比
		 */
		TextView downPercent;

		/**
		 * 当前下载详情,主要是:已下载大小和总大小,可考虑加入速度
		 */
		TextView downDesc;

		/**
		 * 下载进度条对象
		 */
		CustomProgressBar progress;
	}

	/**
	 * 刷新列表
	 */
	public void notifyDataSetChanged(List<DownloadInfoEntity> items) {
		mItems = items;
		super.notifyDataSetChanged();
	}

}
