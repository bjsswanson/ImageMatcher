package com.swansonb.imagematching.model;

public class Status {
	private String status;
	private String message;

	public Status(String status, String message) {
		this.status = status;
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}
}
