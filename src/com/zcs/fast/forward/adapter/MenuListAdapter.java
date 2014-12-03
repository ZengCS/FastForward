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
import com.zcs.fast.forward.entity.ListItemEntity;

public class MenuListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<ListItemEntity> items;

	public MenuListAdapter(Context context, List<ListItemEntity> itemList) {
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
			convertView = mInflater.inflate(R.layout.item_sliding_menu, null);

			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.item_menu_name);
			holder.icon = (ImageView) convertView.findViewById(R.id.item_menu_icon);
			holder.desc = (TextView) convertView.findViewById(R.id.item_menu_desc);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ListItemEntity item = items.get(position);

		holder.name.setText(item.getName());
		holder.icon.setImageResource(item.getDrawable());
		holder.desc.setText(item.getDesc());

		return convertView;
	}

	private static class ViewHolder {
		TextView name;
		TextView desc;
		ImageView icon;
	}

	/**
	 * 刷新列表
	 * 
	 * @param newList
	 */
	public void notifyDataSetChanged(List<ListItemEntity> newList) {
		this.items = newList;
		notifyDataSetChanged();
	}

}
