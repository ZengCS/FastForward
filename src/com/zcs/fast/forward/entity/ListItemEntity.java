package com.zcs.fast.forward.entity;

import java.io.Serializable;

public class ListItemEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 侧滑菜单使用字段 */
	private String type;
	private String name;
	private String desc;
	private int drawable;

	/** 帮助FAQ使用字段 */
	private String question;
	private String answer;

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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getDrawable() {
		return drawable;
	}

	public void setDrawable(int drawable) {
		this.drawable = drawable;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
}
