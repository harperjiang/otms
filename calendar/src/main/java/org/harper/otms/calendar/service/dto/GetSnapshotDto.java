package org.harper.otms.calendar.service.dto;

import org.harper.otms.common.dto.RequestDto;

public class GetSnapshotDto extends RequestDto {

	private int snapshotId;

	public int getSnapshotId() {
		return snapshotId;
	}

	public void setSnapshotId(int snapshotId) {
		this.snapshotId = snapshotId;
	}

}
