package com.blackjade.crm.exception;

public class SaveEmailVerifyCodeException extends Exception {

	String reason ;
	
	public SaveEmailVerifyCodeException(String reason) {
		// TODO Auto-generated constructor stub
		this.reason = reason;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -742563226229642167L;

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
