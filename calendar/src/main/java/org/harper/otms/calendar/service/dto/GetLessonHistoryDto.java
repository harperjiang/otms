package org.harper.otms.calendar.service.dto;

import java.util.Date;

import org.harper.otms.common.dto.PagingDto;
import org.harper.otms.common.dto.RequestDto;

public class GetLessonHistoryDto extends RequestDto {

	private Date fromTime;

	private Date toTime;

	private PagingDto paging;

	public Date getFromTime() {
		return fromTime;
	}

	public void setFromTime(Date fromTime) {
		this.fromTime = fromTime;
	}

	public Date getToTime() {
		return toTime;
	}

	public void setToTime(Date toTime) {
		this.toTime = toTime;
	}

	public PagingDto getPaging() {
		return paging;
	}

	public void setPaging(PagingDto paging) {
		this.paging = paging;
	}

}
