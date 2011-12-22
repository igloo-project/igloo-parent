package fr.openwide.core.rest.jersey.util.exception;

public enum CoreRemoteApiError implements IRemoteApiError {
	
	UNSERIALIZATION_ERROR(90001, "unserialization error");

	private int code;
	
	private String message;
	
	private CoreRemoteApiError(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
