package com.blackjade.crm.apis;

public class CGlobalExceptionAns {
	
	public static enum globalException {
		SERVER_BUSY
	}
	
	private Enum<?> status;

	public Enum<?> getStatus() {
		return status;
	}

	public void setStatus(Enum<?> status) {
		this.status = status;
	}
	
	
}
