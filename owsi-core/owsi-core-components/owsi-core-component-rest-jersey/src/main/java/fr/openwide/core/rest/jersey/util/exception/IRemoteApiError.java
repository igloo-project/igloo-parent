package fr.openwide.core.rest.jersey.util.exception;

public interface IRemoteApiError {
	
	int getCode();

	String getMessage();

}