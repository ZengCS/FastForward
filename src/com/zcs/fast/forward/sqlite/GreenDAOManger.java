package com.zcs.fast.forward.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.zcs.fast.forward.dao.DaoMaster;
import com.zcs.fast.forward.dao.DaoMaster.DevOpenHelper;
import com.zcs.fast.forward.dao.DaoSession;
import com.zcs.fast.forward.utils.LogUtil;

/**
 * GreenDao数据库操作对象统一管理
 * 
 * @author ZengCS
 * @since 2014年12月5日17:24:55
 */
public class GreenDAOManger {
	private SQLiteDatabase db;
	private DaoMaster daoMaster;
	private DaoSession daoSession;
	private boolean initEachTime = true;// 是否每次都初始化
	private static GreenDAOManger instance;

	public static GreenDAOManger getInstance() {
		return instance;
	}

	public static void init(Context context) {
		if (instance == null) {
			new GreenDAOManger(context);
		}
	}

	public GreenDAOManger(Context context) {
		if (initEachTime || db == null || daoMaster == null || daoSession == null) {
			instance = this;
			// TODO 初始化数据库
			long lastTime = System.currentTimeMillis();
			LogUtil.d("dao", "Execute Init!");
			DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "FastForward-db", null);
			db = helper.getWritableDatabase();
			daoMaster = new DaoMaster(db);
			daoSession = daoMaster.newSession();
			LogUtil.d("dao", "Dao init Complete use time:" + (System.currentTimeMillis() - lastTime) + "ms");
		} else {
			// TODO 数据库已存在,什么都不干
			LogUtil.d("dao", "The SQLiteDatabase is exist, do nothing!");
		}
	}

	public SQLiteDatabase getDb() {
		return db;
	}

	public DaoMaster getDaoMaster() {
		return daoMaster;
	}

	public DaoSession getDaoSession() {
		return daoSession;
	}
}
