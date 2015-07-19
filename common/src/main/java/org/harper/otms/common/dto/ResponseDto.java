package org.harper.otms.common.dto;

public class ResponseDto extends Dto {

	private boolean success = true;

	private String errorMessage;

	private int errorCode;
	
	public ResponseDto() {
		
	}
	
	public ResponseDto(int err) {
		setErrorCode(err);
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
		this.success = false;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
