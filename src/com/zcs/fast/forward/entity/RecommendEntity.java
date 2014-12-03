package com.zcs.fast.forward.entity;

import java.io.Serializable;

/**
 * 推荐课程对象
 */
public class RecommendEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private long course_id;
	private String course_title;
	private String course_period;
	private String teacher;
	private String current_learner;
	private String course_avatar;

	public RecommendEntity() {
	}

	/**
	 * 构造一个课程列表bean对象
	 * 
	 * @param course_id
	 *            课程id
	 * @param course_title
	 *            课程名称
	 * @param teacher
	 *            讲师名
	 * @param course_period
	 *            课时数
	 * @param current_learner
	 *            学院数量
	 * @param course_avatar
	 *            课程图标地址
	 */
	public RecommendEntity(long course_id, String course_title, String teacher, String course_period, String current_learner, String course_avatar) {
		this.course_id = course_id;
		this.course_title = course_title;
		this.teacher = teacher;
		this.course_period = course_period;
		this.current_learner = current_learner;
		this.course_avatar = course_avatar;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getCourse_id() {
		return course_id;
	}

	public void setCourse_id(long course_id) {
		this.course_id = course_id;
	}

	public String getCourse_title() {
		return course_title;
	}

	public void setCourse_title(String course_title) {
		this.course_title = course_title;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public String getCourse_period() {
		return course_period;
	}

	public void setCourse_period(String course_period) {
		this.course_period = course_period;
	}

	public String getCurrent_learner() {
		return current_learner;
	}

	public void setCurrent_learner(String current_learner) {
		this.current_learner = current_learner;
	}

	public String getCourse_avatar() {
		return course_avatar;
	}

	public void setCourse_avatar(String course_avatar) {
		this.course_avatar = course_avatar;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((current_learner == null) ? 0 : current_learner.hashCode());
		result = prime * result + (int) (course_id ^ (course_id >>> 32));
		result = prime * result + ((course_title == null) ? 0 : course_title.hashCode());
		result = prime * result + ((course_period == null) ? 0 : course_period.hashCode());
		result = prime * result + ((teacher == null) ? 0 : teacher.hashCode());
		result = prime * result + ((course_avatar == null) ? 0 : course_avatar.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RecommendEntity other = (RecommendEntity) obj;
		if (current_learner == null) {
			if (other.current_learner != null)
				return false;
		} else if (!current_learner.equals(other.current_learner))
			return false;
		if (course_id != other.course_id)
			return false;
		if (course_title == null) {
			if (other.course_title != null)
				return false;
		} else if (!course_title.equals(other.course_title))
			return false;
		if (course_period == null) {
			if (other.course_period != null)
				return false;
		} else if (!course_period.equals(other.course_period))
			return false;
		if (teacher == null) {
			if (other.teacher != null)
				return false;
		} else if (!teacher.equals(other.teacher))
			return false;
		if (course_avatar == null) {
			if (other.course_avatar != null)
				return false;
		} else if (!course_avatar.equals(other.course_avatar))
			return false;
		return true;
	}

}
