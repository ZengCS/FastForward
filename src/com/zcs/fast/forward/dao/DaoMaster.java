package com.zcs.fast.forward.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.identityscope.IdentityScopeType;

/**
 * Master of DAO (schema version 1000): knows all DAOs.
 */
public class DaoMaster extends AbstractDaoMaster {
	public static final int SCHEMA_VERSION = 1000;

	/** Creates underlying database table using DAOs. */
	public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
		// TODO Setp:1 添加调用createTable()方法
		NoteDao.createTable(db, ifNotExists);
		RecommendDao.createTable(db, ifNotExists);
	}

	/** Drops underlying database table using DAOs. */
	public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
		// TODO Setp:2 添加调用dropTable()方法
		NoteDao.dropTable(db, ifExists);
		RecommendDao.dropTable(db, ifExists);
	}

	public static abstract class OpenHelper extends SQLiteOpenHelper {
		public OpenHelper(Context context, String name, CursorFactory factory) {
			super(context, name, factory, SCHEMA_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
			createAllTables(db, false);
		}
	}

	/** WARNING: Drops all table on Upgrade! Use only during development. */
	public static class DevOpenHelper extends OpenHelper {
		public DevOpenHelper(Context context, String name, CursorFactory factory) {
			super(context, name, factory);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
			dropAllTables(db, true);
			onCreate(db);
		}
	}

	public DaoMaster(SQLiteDatabase db) {
		// TODO Setp:3 注册registerDaoClass
		super(db, SCHEMA_VERSION);
		registerDaoClass(NoteDao.class);
		registerDaoClass(RecommendDao.class);
	}

	public DaoSession newSession() {
		return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
	}

	public DaoSession newSession(IdentityScopeType type) {
		return new DaoSession(db, type, daoConfigMap);
	}

}
