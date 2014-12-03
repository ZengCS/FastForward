package com.zcs.fast.forward.activities;

import java.text.DateFormat;
import java.util.Date;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.zcs.fast.forward.R;
import com.zcs.fast.forward.base.BaseActivity;
import com.zcs.fast.forward.dao.DaoMaster;
import com.zcs.fast.forward.dao.DaoMaster.DevOpenHelper;
import com.zcs.fast.forward.dao.DaoSession;
import com.zcs.fast.forward.dao.NoteDao;
import com.zcs.fast.forward.entity.Note;
import com.zcs.fast.forward.utils.LogUtil;

public class GreenDaoActivity extends BaseActivity {
	/** constant */
	private static final String CURR_TITLE = "GreenDAO";

	/** Views */
	private Button addBtn;
	private EditText mEditText;
	private ListView mListView;

	/** GreenDAO */
	private SQLiteDatabase db;
	private DaoMaster daoMaster;
	private DaoSession daoSession;
	private NoteDao noteDao;
	private Cursor cursor;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_green_dao);
		super.init();
		// TODO 初始化数据库
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
		noteDao = daoSession.getNoteDao();

//		List<Note> list = noteDao.loadAll();

		String textColumn = NoteDao.Properties.Text.columnName;
		String orderBy = textColumn + " COLLATE LOCALIZED ASC";
		cursor = db.query(noteDao.getTablename(), noteDao.getAllColumns(), null, null, null, null, orderBy);
		String[] from = { textColumn, NoteDao.Properties.Comment.columnName };
		int[] to = { android.R.id.text1, android.R.id.text2 };

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, from, to);
		mListView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.titlebtn_left_act:
			finish();
			break;
		case R.id.titlebtn_right_act:// 查看
			displayHelpDialog();
			break;
		case R.id.green_dao_add:// 添加
			addNote();
			break;
		default:
			break;
		}
	}

	/**
	 * 添加一条记录
	 */
	@SuppressWarnings("deprecation")
	private void addNote() {
		String noteText = mEditText.getText().toString();
		noteText = noteText.isEmpty() ? "DefaultText" : noteText;
		mEditText.setText("");

		final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
		String comment = "Added on " + df.format(new Date());
		Note note = new Note(null, noteText, comment, new Date());
		noteDao.insert(note);

		Log.d("DaoExample", "Inserted new note, ID: " + note.getId());

		cursor.requery();
	}

	@Override
	protected void initTitlebar() {
		// TODO 初始化标题栏
		super.titlebarView = findViewById(R.id.titlebar_green_dao);

		super.titleBtnLeft = (LinearLayout) titlebarView.findViewById(R.id.titlebtn_left_act);
		super.titleBtnRight = (ImageView) titlebarView.findViewById(R.id.titlebtn_right_act);
		super.titleTxtCenter = (TextView) titlebarView.findViewById(R.id.titletxt_center_act);

		super.titleTxtCenter.setText(CURR_TITLE);

		titleBtnLeft.setOnClickListener(this);
		titleBtnRight.setOnClickListener(this);

		LogUtil.d(TAG, "initTitlebar complete");
	}

	@Override
	protected void initComponent() {
		// TODO 初始化组件
		addBtn = (Button) findViewById(R.id.green_dao_add);
		mEditText = (EditText) findViewById(R.id.green_dao_edit);
		mListView = (ListView) findViewById(R.id.green_dao_list);

		addBtn.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
