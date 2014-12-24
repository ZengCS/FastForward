package com.zcs.fast.forward;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;

import android.app.Activity;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.zcs.fast.forward.activities.OfflineDownloadActivity;
import com.zcs.fast.forward.entity.DownloadInfoEntity;
import com.zcs.fast.forward.utils.FileUtils;
import com.zcs.fast.forward.utils.LogUtil;

/**
 * 下载工具
 * 
 * @author ZengCS
 * @since 2014年12月17日11:59:17
 */
public class Downloader {
	public static final String TAG = "Download";
	/**
	 * 每次读取大小4kb
	 */
	public static final int BUFFER_SIZE = 1024 * 4;
	private static boolean hasInit = false;// 是否已经初始化
	public static final boolean INIT_EACH_TIME = true;// 是否每次都初始化

	public static final String DOWNLOAD_FOLDER_NAME = "FastForward//Videos";

	private static ExecutorService pool;
	private static Activity mContext;
	private static Handler mHandler;
	// 断点位置
	private static long cacheDownSize = 0;
	private static long cacheTotalSize = 0;
	private static int cachePercent = 0;

	private static List<DownloadInfoEntity> downList;

	/**
	 * 判断当前下载任务，用户是否点击了暂停。
	 */
	private static boolean isPause = false;

	public static void init(Activity context, Handler handler) {
		mHandler = handler;
		mContext = context;
		if (!hasInit) {
			downList = new ArrayList<DownloadInfoEntity>(0);
			// 效果类似于Timer定时器
			// pool = Executors.newScheduledThreadPool(1);

			// 创建单个线程的线程池，如果当前线程在执行任务时突然中断，则会创建一个新的线程替代它继续执行任务
			pool = Executors.newSingleThreadExecutor();
			hasInit = true;
		}
	}

	/**
	 * 下载
	 * 
	 * @param item
	 *            下载的Item
	 */
	public static void addDownloadTask(final DownloadInfoEntity item) {
		downList.add(item);
		Thread downThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					download(item);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					downList.remove(item);
				}
			}
		});
		pool.submit(downThread);
	}

	private static void testClose(String url) {
		try {
			System.setProperty("http.keepAlive", "false");
			URLConnection conn = new URL(url).openConnection();
			conn.connect();

			InputStream in = conn.getInputStream();
			LogUtil.d(TAG, "资源加载完成,资源大小:" + FileUtils.formatFileSize(conn.getContentLength()));
			LogUtil.d(TAG, "正在关闭InputStream...");
			long lastTime = System.currentTimeMillis();

			// close()被调用时，为了能够重用这个连接（HTTP KeepAlive的实现要求），
			// 会读取完剩下的数据，然后再close掉连接（关闭Socket）。
			// 因此，当数据没有读取完毕时，close函数是无法立即返回的，而会继续去读取剩下的数据。
			in.close();
			LogUtil.d(TAG, "关闭Input耗时:" + (System.currentTimeMillis() - lastTime) + "ms");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 正常下载
	 * 
	 * @param item
	 * @throws Exception
	 */
	public static void download(DownloadInfoEntity item) throws Exception {
		// testClose(item.getUrl());
		LogUtil.d(TAG, "download() called");
		// 设置当前下载的对象
		setCurrDownItem(item);

		long downSize = 0;
		int percent = 0;
		int lastPercent = 0;
		long totalSize = 0;
		String mPath = item.getUrl();

		InputStream inStream = null;
		OutputStream outStream = null;
		HttpURLConnection urlConn = null;
		URL url = new URL(mPath);
		try {
			System.setProperty("http.keepAlive", "false");
			LogUtil.d(TAG, "资源加载中...");
			urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setConnectTimeout(10000);
			urlConn.setReadTimeout(20000);
			urlConn.setDoInput(true);
			urlConn.setRequestMethod("GET");
			urlConn.connect();
			System.setProperty("http.keepAlive", "false");
			int responseCode = urlConn.getResponseCode();
			if (responseCode != HttpURLConnection.HTTP_OK) {
				throw new Exception("Server Error");
			}
			// 读取服务器上文件的大小,单位:B
			totalSize = urlConn.getContentLength();
			LogUtil.d(TAG, "资源加载完成,资源大小:" + FileUtils.formatFileSize(totalSize));
			item.setLength(totalSize);

			File folder = Environment.getExternalStoragePublicDirectory(DOWNLOAD_FOLDER_NAME);
			if (!folder.exists() || !folder.isDirectory()) {
				LogUtil.d(TAG, "创建资源保存目录:" + folder.getPath());
				folder.mkdirs();
			}
			String downpath = folder + File.separator + item.getName();
			File downFile = new File(downpath);
			if (downFile.exists() && downFile.length() == totalSize) {
				// TODO 文件已存在,并且是完整的
				LogUtil.d(TAG, "文件已存在,并且是完整的！");
				downSize = totalSize;
				item.setDownSize(totalSize);
				item.setPercent(100);
				if (!mContext.isFinishing()) {
					// TODO 发送Message
					Message msg = mHandler.obtainMessage();
					msg.what = OfflineDownloadActivity.WHAT_NOTIFY_DOWN_LIST;
					msg.obj = item;
					mHandler.sendMessage(msg);
				}
				return;
			} else if (downFile.exists()) {
				// TODO 文件已存在,但是不完整
				downFile.delete();
				LogUtil.d(TAG, "文件[" + downFile.getPath() + "]已存在,但是不完整,已删除！");
			}
			outStream = new BufferedOutputStream(new FileOutputStream(downFile));
			byte[] buffer = new byte[BUFFER_SIZE];
			int read = 0;
			percent = 0;
			downSize = 0;
			lastPercent = 0;
			// 开始下载
			LogUtil.d(TAG, "即将开始下载...");
			System.setProperty("http.keepAlive", "false");
			inStream = new BufferedInputStream(urlConn.getInputStream());
			while ((read = inStream.read(buffer)) != -1) {
				if (item.getDownState() == DownloadInfoEntity.PAUSE) {
					LogUtil.d(TAG, "暂停下载！");
					// 暂停 断点续传用
					cacheDownSize = downSize;
					cacheTotalSize = totalSize;
					cachePercent = percent;
					break;
				}

				outStream.write(buffer, 0, read);
				// 设置已下载size
				downSize += read;
				item.setDownSize(downSize);

				// 设置下载百分比
				percent = (int) (downSize / (float) totalSize * 100);
				item.setPercent(percent);
				if (lastPercent != percent) {
					if (percent % 10 == 0) {
						LogUtil.d(TAG, "正在下载[" + item.getName() + "],已完成：" + percent + "%");
					}
					if (!mContext.isFinishing()) {
						// TODO 发送Message
						Message msg = mHandler.obtainMessage();
						msg.what = OfflineDownloadActivity.WHAT_NOTIFY_DOWN_LIST;
						msg.obj = item;
						mHandler.sendMessage(msg);
					}
				}
				lastPercent = percent;
			}
			LogUtil.d(TAG, "当前下载任务已结束,资源文件:" + downFile.getPath());
		} catch (java.net.SocketTimeoutException e) {
			e.printStackTrace();
		} catch (final java.io.IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// closeStreams(in, out);
			if (inStream != null) {
				LogUtil.d(TAG, "正在关闭:InputStream...");
				long lastTime = System.currentTimeMillis();
				inStream.close();
				LogUtil.d(TAG, "成功关闭InputStream,共耗时:" + (System.currentTimeMillis() - lastTime));
			}
			if (outStream != null) {
				outStream.close();
			}
		}
	}

	/**
	 * 新启动一个线程来关闭文件流
	 * 
	 * @param in
	 * @param out
	 */
	protected static void closeStreams(final InputStream in, final OutputStream out) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				LogUtil.d(TAG, "新启动一个线程来关闭文件流!");
				if (in != null) {
					LogUtil.d(TAG, "正在关闭:InputStream...");
					long lastTime = System.currentTimeMillis();
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					LogUtil.d(TAG, "成功关闭InputStream,共耗时:" + (System.currentTimeMillis() - lastTime));
				}
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	/**
	 * 设置当前下载的对象
	 * 
	 * @param item
	 */
	private static void setCurrDownItem(DownloadInfoEntity item) {
		// 设置为下载状态
		item.setDownState(DownloadInfoEntity.DOWNLOADING);
		if (!mContext.isFinishing()) {
			Message msg = mHandler.obtainMessage();
			msg.what = OfflineDownloadActivity.WHAT_SET_CURR_ITEM;
			msg.obj = item;
			mHandler.sendMessage(msg);
		}
	}

	/**
	 * 断点续传
	 * 
	 * @param item
	 * @throws Exception
	 */
	public static void continuesDownload(DownloadInfoEntity item) throws Exception {
		long downSize = 0;
		int percent = 0;
		int lastPercent = 0;
		long totalSize = 0;
		String mPath = item.getUrl();

		InputStream in = null;
		OutputStream out = null;
		HttpURLConnection urlConnection = null;
		URL url = new URL(mPath);
		try {
			LogUtil.d(TAG, "资源加载中...");
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setConnectTimeout(10000);
			urlConnection.setReadTimeout(20000);
			File folder = Environment.getExternalStoragePublicDirectory(DOWNLOAD_FOLDER_NAME);
			if (!folder.exists() || !folder.isDirectory()) {
				folder.mkdirs();
			}
			String downFile = folder + File.separator + item.getName();
			File file = new File(downFile);
			downSize = file.length();
			totalSize = cacheTotalSize;

			if (totalSize == 0 || totalSize < downSize) {// 如果数据库无大小数据或存入了错误的数据
				// 重新获取文件大小
				urlConnection.setDoInput(true);
				urlConnection.setRequestMethod("GET");
				urlConnection.connect();
				totalSize = urlConnection.getContentLength();
				urlConnection.disconnect();
				urlConnection = null;
				urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setConnectTimeout(10000);
				urlConnection.setReadTimeout(20000);
			}
			if (file.exists()) {
				urlConnection.setRequestProperty("RANGE", "bytes=" + file.length() + "-");
				LogUtil.d(TAG, "文件存在,准备断点续传---断点位置：" + file.length() + "-");
				LogUtil.d(TAG, "文件总大小:" + totalSize);
			}
			urlConnection.setDoInput(true);
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();
			int responseCode = urlConnection.getResponseCode();

			if (file.exists() && responseCode != HttpURLConnection.HTTP_PARTIAL) {// 断点返回的头不一样了
				downSize = cacheDownSize;
				totalSize = cacheTotalSize;
				percent = cachePercent;
				throw new Exception("Server Error:" + responseCode);
			} else if (!file.exists() && responseCode != HttpURLConnection.HTTP_OK) {
				throw new Exception("Server Error:" + responseCode);
			}
			in = new BufferedInputStream(urlConnection.getInputStream());
			out = new BufferedOutputStream(new FileOutputStream(file, true));// 追加

			byte[] buffer = new byte[BUFFER_SIZE];
			int read = 0;
			percent = 0;

			lastPercent = cachePercent;
			// 开始下载
			while ((read = in.read(buffer)) != -1) {
				if (isPause) {
					// 暂停 断点续传用
					cacheDownSize = downSize;
					cacheTotalSize = totalSize;
					cachePercent = percent;
					isPause = false;
					return;
				}

				out.write(buffer, 0, read);
				downSize += read;
				percent = (int) (downSize / (float) totalSize * 100);

				if (lastPercent != percent) {
					LogUtil.d(TAG, "下载任务：" + percent + "%");
				}
				lastPercent = percent;
			}
		} catch (java.net.SocketTimeoutException e) {
			e.printStackTrace();
		} catch (final java.io.IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * 断点续传
	 * 
	 * @param item
	 *            下载的Item
	 */
	public static void addContinuesDownloadTask(final DownloadInfoEntity item) {
		pool.submit(new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					continuesDownload(item);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
				}
			}
		}));
	}

	/**
	 * @return 用户是否点击了暂停
	 */
	public static boolean isPause() {
		return isPause;
	}

	public static void setPause(boolean isPause) {
		Downloader.isPause = isPause;
	}

}
