package com.zcs.fast.forward.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zcs.fast.forward.R;
import com.zcs.fast.forward.entity.CheckItem;

public class CheckListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<CheckItem> items;

	public CheckListAdapter(Context context, List<CheckItem> itemList) {
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
			convertView = mInflater.inflate(R.layout.item_check, null);

			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.item_check_name);
			holder.check = (CheckBox) convertView.findViewById(R.id.item_check_box);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		CheckItem item = items.get(position);

		holder.name.setText(item.getName());
		holder.check.setChecked(item.isCheck());
		// ListView的item中加入checkbox后导致ListView对OnItemClick事件无法响应，原因是因为checkbox的事件响应优先级高于ListItem，所以屏蔽了ListItem的单击事件。
		// 解决方案：设置checkbox的android:focusable="false"即可！
		holder.check.setFocusable(false);
		// 这里我们不希望用户之间点击checkbox对象,而是之间点击整个Item触发选中事件
		holder.check.setClickable(false);

		return convertView;
	}

	private static class ViewHolder {
		TextView name;
		CheckBox check;
	}

	/**
	 * 刷新列表
	 * 
	 * @param newList
	 */
	public void notifyDataSetChanged(List<CheckItem> newList) {
		this.items = newList;
		notifyDataSetChanged();
	}

}
