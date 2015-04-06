package com.iek.tcpcomm.stat;

public class BoardResponse {
	private String message;
	private double elapsedTime;

	public BoardResponse() {

	}

	public BoardResponse(String response, double elapsedTime) {
		setMessage(response);
		setElapsedTime(elapsedTime);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public double getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(double elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
}
