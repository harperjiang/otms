package org.harper.otms.calendar.service.dto;

import java.util.List;

import org.harper.otms.common.dto.PagingDto;
import org.harper.otms.common.dto.ResponseDto;

public class GetLessonHistoryResponseDto extends ResponseDto {

	private List<LessonItemDto> result;

	private PagingDto paging;

	public List<LessonItemDto> getResult() {
		return result;
	}

	public void setResult(List<LessonItemDto> result) {
		this.result = result;
	}

	public PagingDto getPaging() {
		return paging;
	}

	public void setPaging(PagingDto paging) {
		this.paging = paging;
	}

}
