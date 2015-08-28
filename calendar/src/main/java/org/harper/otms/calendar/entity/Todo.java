package org.harper.otms.calendar.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.persistence.annotations.Convert;
import org.harper.otms.auth.entity.User;
import org.harper.otms.common.dao.Entity;

import com.google.gson.JsonObject;

@javax.persistence.Entity
@Table(name = "todo")
public class Todo extends Entity {

	public static enum Type {
		CLIENT_FEEDBACK
	};

	@OneToOne
	@JoinColumn(name = "owner_id")
	private User owner;

	@Column(name = "type")
	private String type;

	@Column(name = "ref_id")
	private int refId;

	@Column(name = "context")
	@Convert("jsonConverter")
	private JsonObject context = new JsonObject();

	@Column(name = "expire_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date expireTime;

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public String getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type.name();
	}

	public JsonObject getContext() {
		return context;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public int getRefId() {
		return refId;
	}

	public void setRefId(int refId) {
		this.refId = refId;
	}

	public static String DK_LESSON_TITLE = "lessonTitle";

	public static String DK_LESSON_WITH = "lessonWith";

	public static String DK_LESSON_FROM = "lessonFrom";

	public static String DK_LESSON_TO = "lessonTo";

	public static String DK_LESSON_TUTORID = "lessonTutorId";
}
