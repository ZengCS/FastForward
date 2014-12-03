package com.zcs.fast.forward.dao;

import java.util.Map;

import android.database.sqlite.SQLiteDatabase;

import com.zcs.fast.forward.entity.Note;
import com.zcs.fast.forward.entity.RecommendEntity;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

	// TODO Step:1 定义一个DaoConfig对象
	private final DaoConfig noteDaoConfig;
	private final DaoConfig recommendDaoConfig;

	// TODO Step:2 定义一个Dao对象
	private final NoteDao noteDao;
	private final RecommendDao recommendDao;

	public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig> daoConfigMap) {
		super(db);
		// TODO Step:3 克隆Config对象并初始化
		noteDaoConfig = daoConfigMap.get(NoteDao.class).clone();
		noteDaoConfig.initIdentityScope(type);

		recommendDaoConfig = daoConfigMap.get(RecommendDao.class).clone();
		recommendDaoConfig.initIdentityScope(type);

		// TODO Step:4 初始化Dao对象并注册
		noteDao = new NoteDao(noteDaoConfig, this);
		recommendDao = new RecommendDao(recommendDaoConfig, this);

		registerDao(Note.class, noteDao);
		registerDao(RecommendEntity.class, recommendDao);
	}

	public void clear() {
		// TODO Step:5 添加Clear
		noteDaoConfig.getIdentityScope().clear();
		recommendDaoConfig.getIdentityScope().clear();
	}

	// TODO Step:6 添加Dao的get方法
	public NoteDao getNoteDao() {
		return noteDao;
	}

	public RecommendDao getRecommendDao() {
		return recommendDao;
	}

}
