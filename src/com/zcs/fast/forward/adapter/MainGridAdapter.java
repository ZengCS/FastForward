package com.zcs.fast.forward.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zcs.fast.forward.R;
import com.zcs.fast.forward.entity.MainGridItemEntity;

public class MainGridAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<MainGridItemEntity> items;

	public MainGridAdapter(Context context, List<MainGridItemEntity> itemList) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.items = itemList;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.night_item, null);

			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.ItemText);
			holder.icon = (ImageView) convertView.findViewById(R.id.ItemImage);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		MainGridItemEntity item = items.get(position);

		holder.name.setText(item.getName());
		holder.icon.setImageResource(item.getIconId());

		return convertView;
	}

	private static class ViewHolder {
		TextView name;
		ImageView icon;
	}

	/**
	 * 刷新列表
	 * 
	 * @param newList
	 */
	public void notifyDataSetChanged(List<MainGridItemEntity> newList) {
		this.items = newList;
		notifyDataSetChanged();
	}

}
