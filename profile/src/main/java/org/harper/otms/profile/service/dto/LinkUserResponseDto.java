package org.harper.otms.profile.service.dto;

import org.harper.otms.common.dto.ResponseDto;

public class LinkUserResponseDto extends ResponseDto {

	public LinkUserResponseDto() {
		super();
	}

	public LinkUserResponseDto(int errorCode) {
		super(errorCode);
	}

	public LinkUserResponseDto(boolean act) {
		needActivate = act;
	}

	private boolean needActivate;

	public boolean isNeedActivate() {
		return needActivate;
	}

	public void setNeedActivate(boolean needActivate) {
		this.needActivate = needActivate;
	}

}
