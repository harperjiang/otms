package org.harper.otms.support.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.harper.otms.auth.entity.User;
import org.harper.otms.common.dao.Entity;

@javax.persistence.Entity
@Table(name = "action_log")
public class ActionLog extends Entity {

	@Column(name = "action_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date actionDate;

	@OneToOne
	@JoinColumn(name = "operator_id")
	private User operator;

	@Column(name = "type")
	private String type;

	@Column(name = "from_val")
	private String from;

	@Column(name = "to_val")
	private String to;

	@Column(name = "comment")
	private String comment;

	public Date getActionDate() {
		return actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}

	public User getOperator() {
		return operator;
	}

	public void setOperator(User operator) {
		this.operator = operator;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
