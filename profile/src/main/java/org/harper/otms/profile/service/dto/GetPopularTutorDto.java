package org.harper.otms.profile.service.dto;

import org.harper.otms.common.dto.RequestDto;

public class GetPopularTutorDto extends RequestDto {

	private String keyword;

	private int level;

	public GetPopularTutorDto() {
		super();
	}

	public boolean needValidation() {
		return false;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
