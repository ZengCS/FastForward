package com.zcs.fast.forward.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.zcs.fast.forward.R;
import com.zcs.fast.forward.entity.RecommendEntity;
import com.zcs.fast.forward.utils.BitmapCache;

@SuppressLint("InflateParams")
public class RecommendListAdapter extends BaseAdapter {
	private final String TAG = "RecommendListAdapter";

	private RequestQueue mQueue;
	private List<RecommendEntity> mItems;
	private ImageLoader mImageLoader;
	private LayoutInflater mInflater;

	public RecommendListAdapter(Context context, RequestQueue queue, List<RecommendEntity> RecommendEntitys) {
		super();
		Log.d(TAG, "new RecommendListAdapter");
		mQueue = queue;
		mItems = RecommendEntitys;
		// 初始化Volley图片Loader
		mImageLoader = new ImageLoader(mQueue, new BitmapCache());
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
		return mItems.get(position).getCourse_id();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_recommend_list_fill, null);

			holder = new ViewHolder();
			holder.tvCourseName = (TextView) convertView.findViewById(R.id.textView_recommend_item_coursename);
			holder.tvTeacherName = (TextView) convertView.findViewById(R.id.textView_recommend_item_techername);
			holder.tvPeriod = (TextView) convertView.findViewById(R.id.textView_recommend_item_period);
			holder.tvPlayTimes = (TextView) convertView.findViewById(R.id.textView_recommend_item_playtimes);
			holder.imAvatar = (ImageView) convertView.findViewById(R.id.imageView_recomend_list_avatar);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		RecommendEntity item = mItems.get(position);
		holder.tvCourseName.setText(item.getCourse_title());
		holder.tvPeriod.setText(item.getCourse_period() + "课时");
		holder.tvPlayTimes.setText(item.getCurrent_learner());
		holder.tvTeacherName.setText(item.getTeacher());
		// 利用Volley加载图片
		ImageListener listener = ImageLoader.getImageListener(holder.imAvatar, 0, 0);
		mImageLoader.get(item.getCourse_avatar(), listener);

		return convertView;
	}

	private static class ViewHolder {
		TextView tvCourseName;
		TextView tvTeacherName;
		TextView tvPeriod;
		TextView tvPlayTimes;
		ImageView imAvatar;
	}

	/**
	 * 刷新列表
	 */
	public void notifyDataSetChanged(List<RecommendEntity> items) {
		mItems = items;
		super.notifyDataSetChanged();
	}

}
