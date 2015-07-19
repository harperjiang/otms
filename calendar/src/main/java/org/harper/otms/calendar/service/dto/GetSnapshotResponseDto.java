package org.harper.otms.calendar.service.dto;

import org.harper.otms.common.dto.ResponseDto;

public class GetSnapshotResponseDto extends ResponseDto {

	private SnapshotDto snapshot;

	private boolean owner;

	public GetSnapshotResponseDto() {
		super();
	}

	public GetSnapshotResponseDto(int err) {
		super(err);
	}

	public SnapshotDto getSnapshot() {
		return snapshot;
	}

	public void setSnapshot(SnapshotDto snapshot) {
		this.snapshot = snapshot;
	}

	public boolean isOwner() {
		return owner;
	}

	public void setOwner(boolean owner) {
		this.owner = owner;
	}

}
