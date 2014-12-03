package com.zcs.fast.forward.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zcs.fast.forward.R;
import com.zcs.fast.forward.entity.ListItemEntity;

public class HelpListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<ListItemEntity> items;

	public HelpListAdapter(Context context, List<ListItemEntity> itemList) {
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
			convertView = mInflater.inflate(R.layout.item_help_list, null);

			holder = new ViewHolder();
			holder.question = (TextView) convertView.findViewById(R.id.item_help_question);
			holder.answer = (TextView) convertView.findViewById(R.id.item_help_answer);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ListItemEntity item = items.get(position);

		holder.question.setText(item.getQuestion());
		holder.answer.setText(item.getAnswer());

		return convertView;
	}

	private static class ViewHolder {
		TextView question;
		TextView answer;
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
