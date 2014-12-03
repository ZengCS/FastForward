package com.zcs.fast.forward.dao;

import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.zcs.fast.forward.entity.RecommendEntity;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

public class RecommendDao extends AbstractDao<RecommendEntity, Long> {
	public static final String TABLENAME = "RECOMMEND";

	/**
	 * Properties of entity Recommend. Can be used for QueryBuilder and for
	 * referencing column names.
	 */
	public static class Properties {
		public final static Property Id = new Property(0, Long.class, "id", true, "_id");
		public final static Property CourseId = new Property(1, Long.class, "course_id", true, "COURSE_ID");
		public final static Property CourseTitle = new Property(2, String.class, "course_title", false, "COURSE_TITLE");
		public final static Property CoursePeriod = new Property(3, String.class, "course_period", false, "COURSE_PERIOD");
		public final static Property Teacher = new Property(4, String.class, "teacher", false, "TEACHER");
		public final static Property CurrentLearner = new Property(5, String.class, "current_learner", false, "CURRENT_LEARNER");
		public final static Property CourseAvatar = new Property(6, String.class, "course_avatar", false, "COURSE_AVATAR");
	};

	public RecommendDao(DaoConfig config) {
		super(config);
	}

	public RecommendDao(DaoConfig config, DaoSession daoSession) {
		super(config, daoSession);
	}

	/**
	 * 覆盖插入
	 * 
	 * @param list
	 */
	public void coverList(List<RecommendEntity> list) {
		deleteAll();
		for (RecommendEntity entity : list) {
			super.insert(entity);
		}
	}

	/**
	 * Creates the underlying database table.
	 */
	public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
		String constraint = ifNotExists ? "IF NOT EXISTS " : "";
		String sql = "CREATE TABLE " + constraint + "'" + TABLENAME + "' (" + //
				"'_id' INTEGER PRIMARY KEY ," + // 0: id
				"'COURSE_ID' INTEGER ," + // 1: COURSE_ID
				"'COURSE_TITLE' TEXT NOT NULL ," + // 2: COURSE_TITLE
				"'COURSE_PERIOD' TEXT ," + // 3: COURSE_PERIOD
				"'TEACHER' TEXT ," + // 4: TEACHER
				"'CURRENT_LEARNER' TEXT ," + // 5: CURRENT_LEARNER
				"'COURSE_AVATAR' TEXT);"; // 6: COURSE_AVATAR
		db.execSQL(sql);
	}

	/**
	 * Drops the underlying database table.
	 */
	public static void dropTable(SQLiteDatabase db, boolean ifExists) {
		String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'" + TABLENAME + "'";
		db.execSQL(sql);
	}

	@Override
	protected void bindValues(SQLiteStatement stmt, RecommendEntity entity) {
		stmt.clearBindings();

		Long id = entity.getId();
		if (id != null) {
			stmt.bindLong(1, id);
		}

		Long course_id = entity.getCourse_id();
		if (course_id != null) {
			stmt.bindLong(2, course_id);
		}

		stmt.bindString(3, entity.getCourse_title());
		stmt.bindString(4, entity.getCourse_period());
		stmt.bindString(5, entity.getTeacher());
		stmt.bindString(6, entity.getCurrent_learner());
		stmt.bindString(7, entity.getCourse_avatar());
	}

	@Override
	protected Long getKey(RecommendEntity entity) {
		if (entity != null) {
			return entity.getId();
		} else {
			return null;
		}
	}

	@Override
	protected boolean isEntityUpdateable() {
		return true;
	}

	@Override
	protected RecommendEntity readEntity(Cursor cursor, int offset) {
		RecommendEntity entity = new RecommendEntity();
		entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));// id
		entity.setCourse_id(cursor.getLong(offset + 1));
		entity.setCourse_title(cursor.getString(offset + 2));// course_title
		entity.setCourse_period(cursor.getString(offset + 3));
		entity.setTeacher(cursor.getString(offset + 4));// teacher
		entity.setCurrent_learner(cursor.getString(offset + 5));
		entity.setCourse_avatar(cursor.getString(offset + 6));
		return entity;
	}

	@Override
	protected void readEntity(Cursor cursor, RecommendEntity entity, int offset) {
		entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));// id
		entity.setCourse_id(cursor.getLong(offset + 1));
		entity.setCourse_title(cursor.getString(offset + 2));// course_title
		entity.setCourse_period(cursor.getString(offset + 3));
		entity.setTeacher(cursor.getString(offset + 4));// teacher
		entity.setCurrent_learner(cursor.getString(offset + 5));// Current_learner
		entity.setCourse_avatar(cursor.getString(offset + 6));// Course_avatar
	}

	@Override
	protected Long readKey(Cursor cursor, int offset) {
		return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
	}

	@Override
	protected Long updateKeyAfterInsert(RecommendEntity entity, long rowId) {
		entity.setId(rowId);
		return rowId;
	}

}
