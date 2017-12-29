package org.iglooproject.rest.jersey.util.exception;

public interface IRemoteApiError {
	
	int getCode();

	String getMessage();

}