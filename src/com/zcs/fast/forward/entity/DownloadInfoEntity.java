package com.zcs.fast.forward.entity;

import java.io.Serializable;

public class DownloadInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 下载中状态
	 */
	public static final int DOWNLOADING = 0;
	/**
	 * 暂停状态
	 */
	public static final int PAUSE = 1;
	/**
	 * 等待状态
	 */
	public static final int WAIT = 2;
	/**
	 * 错误状态
	 */
	public static final int ERROR = 3;
	/**
	 * 下载完成
	 */
	public static final int COMPLETE = 4;

	/**
	 * id
	 */
	private Long id;
	/**
	 * 文件名称
	 */
	private String name;

	/**
	 * 下载路径
	 */
	private String url;

	/**
	 * 文件总大小
	 */
	private long length;

	/**
	 * 已下载大小
	 */
	private long downSize;

	/**
	 * 已下载百分比
	 */
	private int percent;

	/**
	 * 下载状态
	 */
	private int downState;
	
	/**
	 * 在ListView中的Position
	 */
	private int position;

	public DownloadInfoEntity() {
	}

	public DownloadInfoEntity(Long id, String name, String url, long length, long downSize, int percent, int downState) {
		this.id = id;
		this.name = name;
		this.url = url;
		this.length = length;
		this.downSize = downSize;
		this.percent = percent;
		this.downState = downState;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public long getDownSize() {
		return downSize;
	}

	public void setDownSize(long downSize) {
		this.downSize = downSize;
	}

	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}

	public int getDownState() {
		return downState;
	}

	public void setDownState(int downState) {
		this.downState = downState;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
}
