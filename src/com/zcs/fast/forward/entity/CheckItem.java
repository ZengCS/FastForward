package com.zcs.fast.forward.entity;

public class CheckItem {
	private long id;
	private String name;
	private boolean isCheck;

	public CheckItem() {
		super();
	}

	public CheckItem(long id, String name, boolean isCheck) {
		super();
		this.id = id;
		this.name = name;
		this.isCheck = isCheck;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

}
