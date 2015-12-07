package fr.openwide.core.jpa.externallinkchecker.business.model;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.apache.http.client.CircularRedirectException;
import org.apache.http.client.ClientProtocolException;

public enum ExternalLinkErrorType {

	HTTP,
	INVALID_IDN,
	URI_SYNTAX,
	MALFORMED_URL,
	IO,
	TIMEOUT,
	CLIENT_PROTOCOL,
	CIRCULAR_REDIRECT,
	UNKNOWN_HTTPCLIENT_ERROR;
	
	
	public static ExternalLinkErrorType fromException(Exception e) {
		if (e instanceof SocketTimeoutException) {
			return TIMEOUT;
		} else if (e instanceof ClientProtocolException) {
			if (e.getCause() instanceof CircularRedirectException) {
				return CIRCULAR_REDIRECT;
			}
			return CLIENT_PROTOCOL;
		} else if (e instanceof IOException) {
			return IO;
		}
		return UNKNOWN_HTTPCLIENT_ERROR;
	}
}
