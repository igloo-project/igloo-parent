package fr.openwide.core.rest.jersey.util.exception;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(value = { "cause", "stackTrace", "localizedMessage" })
public class RemoteApiException extends RuntimeException {

	private static final long serialVersionUID = 2172390724041827232L;
	
	private int code;
	
	public RemoteApiException(IRemoteApiError remoteApiError) {
		this(remoteApiError, null);
	}

	public RemoteApiException(IRemoteApiError remoteApiError, Throwable cause) {
		super(remoteApiError.getMessage(), cause);
		
		this.code = remoteApiError.getCode();
	}
	
	public int getCode() {
		return code;
	}
	
}
