package org.harper.otms.calendar.service.dto;

import org.harper.otms.common.dto.RequestDto;

public class FindTutorDto extends RequestDto {

	private String keyword;

	private int rate;

	private int agePrefer;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public int getAgePrefer() {
		return agePrefer;
	}

	public void setAgePrefer(int agePrefer) {
		this.agePrefer = agePrefer;
	}

}
