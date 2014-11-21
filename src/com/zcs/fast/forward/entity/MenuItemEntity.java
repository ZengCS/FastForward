package com.zcs.fast.forward.entity;

import java.io.Serializable;

public class MenuItemEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String type;
	private String name;
	private int drawable;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDrawable() {
		return drawable;
	}

	public void setDrawable(int drawable) {
		this.drawable = drawable;
	}
}
