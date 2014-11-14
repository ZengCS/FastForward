package com.zcs.fast.forward.base;

import android.support.v4.app.Fragment;

public interface MainListener {
	/**
	 * 切换主Fragment内容
	 * 
	 * @param targetFragment
	 *            要展示的Fragment
	 */
	public void changeMainFragment(Fragment targetFragment);

	/**
	 * 切换主Fragment内容
	 * 
	 * @param currFragment
	 *            当前Fragment
	 * @param targetFragment
	 *            目标Fragment
	 */
	public void changeMainFragment(Fragment currFragment, Fragment targetFragment);

	public void doPopBackStack();

	public void showToast(String text);

	public void showToastSingle(String text);

	/**
	 * 展示左侧菜单
	 */
	public void showMenu();

	/**
	 * 结束
	 */
	public void exitApp();
}
