package com.niit.Models;

public class ErrorClass 
{
	private int errorCode;
	private String message;
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ErrorClass(int errorCode, String message) {
		super();
		this.errorCode = errorCode;
		this.message = message;
	}
	

}
