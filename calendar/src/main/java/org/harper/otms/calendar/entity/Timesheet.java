package org.harper.otms.calendar.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Timesheet {

	@Column(name="ts_mon")
	private String mondayExpression;

	@Column(name="ts_tue")
	private String tuesdayExpression;

	@Column(name="ts_wed")
	private String wednesdayExpression;

	@Column(name="ts_thu")
	private String thursdayExpression;

	@Column(name="ts_fri")
	private String fridayExpression;

	@Column(name="ts_sat")
	private String saturdayExpression;

	@Column(name="ts_sun")
	private String sundayExpression;

	public String getMondayExpression() {
		return mondayExpression;
	}

	public void setMondayExpression(String mondayExpression) {
		this.mondayExpression = mondayExpression;
	}

	public String getTuesdayExpression() {
		return tuesdayExpression;
	}

	public void setTuesdayExpression(String tuesdayExpression) {
		this.tuesdayExpression = tuesdayExpression;
	}

	public String getWednesdayExpression() {
		return wednesdayExpression;
	}

	public void setWednesdayExpression(String wednesdayExpression) {
		this.wednesdayExpression = wednesdayExpression;
	}

	public String getThursdayExpression() {
		return thursdayExpression;
	}

	public void setThursdayExpression(String thursdayExpression) {
		this.thursdayExpression = thursdayExpression;
	}

	public String getFridayExpression() {
		return fridayExpression;
	}

	public void setFridayExpression(String fridayExpression) {
		this.fridayExpression = fridayExpression;
	}

	public String getSaturdayExpression() {
		return saturdayExpression;
	}

	public void setSaturdayExpression(String saturdayExpression) {
		this.saturdayExpression = saturdayExpression;
	}

	public String getSundayExpression() {
		return sundayExpression;
	}

	public void setSundayExpression(String sundayExpression) {
		this.sundayExpression = sundayExpression;
	}

}
