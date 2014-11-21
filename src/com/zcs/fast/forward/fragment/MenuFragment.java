package com.zcs.fast.forward.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.zcs.fast.forward.R;
import com.zcs.fast.forward.activities.HelpActivity;
import com.zcs.fast.forward.adapter.MenuListAdapter;
import com.zcs.fast.forward.base.BaseFragment;
import com.zcs.fast.forward.commons.MenuType;
import com.zcs.fast.forward.entity.MenuItemEntity;
import com.zcs.fast.forward.utils.dialog.DialogParam;
import com.zcs.fast.forward.utils.dialog.DialogUtil;

public class MenuFragment extends BaseFragment {
	private View root;
	private Button btnExit;
	private Dialog msgDialog;

	/** The Menu ListView */
	private ListView mListView;
	private List<MenuItemEntity> menuList;
	private MenuListAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_menu, null);
		super.init();
		initMenuListView();
		return root;
	}

	/**
	 * 初始化菜单列表
	 */
	private void initMenuListView() {
		// TODO 初始化菜单列表
		menuList = new ArrayList<MenuItemEntity>(0);
		String[] menuArr = mContext.getResources().getStringArray(R.array.menu_list);
		for (String name : menuArr) {
			MenuItemEntity item = new MenuItemEntity();
			String[] names = name.split(";");
			item.setName(names[0]);
			item.setType(names[1]);
			item.setDrawable(R.drawable.ic_cocos2d);
			menuList.add(item);
		}
		mAdapter = new MenuListAdapter(mContext, menuList);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				MenuItemEntity item = (MenuItemEntity) mAdapter.getItem(position);
				dispatchMenu(item);
			}
		});
	}

	/**
	 * 分发菜单操作
	 * 
	 * @param item
	 *            被点击的菜单
	 */
	private void dispatchMenu(MenuItemEntity item) {
		if (item.getType().equals(MenuType.MENU_TYPE_SYSTEM_SETTING)) {// 系统设置
			mListener.showToast("Menu:系统设置" + System.currentTimeMillis());
			return;
		}
		if (item.getType().equals(MenuType.MENU_TYPE_CLEAR_CACHE)) {// 清空缓存
			mListener.showToast("Menu:清空缓存" + System.currentTimeMillis());
			return;
		}
		if (item.getType().equals(MenuType.MENU_TYPE_FEEDBACK)) {// 意见反馈
			mListener.showToast("Menu:意见反馈" + System.currentTimeMillis());
			return;
		}
		if (item.getType().equals(MenuType.MENU_TYPE_UPDATE)) {// 检测更新
			mListener.showToast("Menu:检测更新" + System.currentTimeMillis());
			return;
		}
		if (item.getType().equals(MenuType.MENU_TYPE_NAVIGATE)) {// 新手引导
			mListener.showToast("Menu:新手引导" + System.currentTimeMillis());
			return;
		}
		if (item.getType().equals(MenuType.MENU_TYPE_VERSION)) {// 版本信息
			mListener.showToast("Menu:版本信息" + System.currentTimeMillis());
			return;
		}
		if (item.getType().equals(MenuType.MENU_TYPE_HELP_FAQ)) {// 帮助FAQ
			mListener.showToast("Menu:帮助FAQ" + System.currentTimeMillis());
			mListener.changeActivity(HelpActivity.class);
			mListener.hideMenu();
			return;
		}
		if (item.getType().equals(MenuType.MENU_TYPE_ABOUT)) {// 关于
			if (msgDialog == null) {
				initDialog();
			}
			msgDialog.show();
			return;
		}
		if (item.getType().equals(MenuType.MENU_TYPE_UNKNOWN)) {// 未知
			mListener.showToast("Menu:未知" + System.currentTimeMillis());
			return;
		}
	}

	/**
	 * 初始化Dialog
	 */
	private void initDialog() {
		DialogParam param = new DialogParam(mContext, getString(R.string.link_info), true);
		param.setTitle(mContext.getString(R.string.txt_about));
		param.setTextIsSelectable(false);
		param.setOkBtnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				msgDialog.dismiss();
			}
		});
		String[] highStrs = new String[] { "ZengCS", "zengcs@vip.qq.com", "zcs417327734@gmail.com", "417327734", "天府软件园D·成都·四川·中国" };
		int highColor = Color.parseColor("#1F76B6");// 蓝色
		// int highColor = Color.parseColor("#E96B69");// 红色
		msgDialog = DialogUtil.createMessageDialog(param, highStrs, highColor);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_menu_exit:
			mListener.exitApp();
			break;
		default:
			break;
		}
	}

	@Override
	protected void initComponent() {
		btnExit = (Button) root.findViewById(R.id.btn_menu_exit);
		mListView = (ListView) root.findViewById(R.id.menu_listview);

		btnExit.setOnClickListener(this);
	}
}
