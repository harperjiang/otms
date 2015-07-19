package org.harper.otms.common.service;

public class DataException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2341050709078524640L;

	private int errorCode;

	public DataException(int errorCode) {
		super();
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}
}
