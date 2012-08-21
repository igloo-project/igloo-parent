package fr.openwide.core.rest.jersey.service;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.sun.jersey.api.JResponse;
import com.sun.jersey.api.JResponse.JResponseBuilder;
import com.sun.jersey.multipart.FormDataBodyPart;

import fr.openwide.core.rest.jersey.util.exception.CoreRemoteApiError;
import fr.openwide.core.rest.jersey.util.exception.IRemoteApiError;
import fr.openwide.core.rest.jersey.util.exception.RemoteApiException;

public abstract class AbstractRestServiceImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRestServiceImpl.class);
	
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	protected JResponseBuilder<Object> ok() {
		return JResponse.ok();
	}
	
	protected <E> JResponseBuilder<E> ok(E entity) {
		return JResponse.ok(entity);
	}
	
	protected RemoteApiException getException(IRemoteApiError error) {
		return new RemoteApiException(error);
	}
	
	protected RemoteApiException getException(IRemoteApiError error, Throwable cause) {
		LOGGER.error(error.getCode() + " - " + error.getMessage(), cause);
		
		return new RemoteApiException(error, cause);
	}
	
	protected <K, V> Map<K, V> getMapFromJsonString(String jsonString, Class<K> keyClass, Class<V> valueClass) {
		try {
			return OBJECT_MAPPER.readValue(jsonString,
					MapType.construct(LinkedHashMap.class,
							SimpleType.construct(keyClass),
							SimpleType.construct(valueClass)));
		} catch (Exception e) {
			LOGGER.error(CoreRemoteApiError.UNSERIALIZATION_ERROR.getMessage(), e);
			
			throw(getException(CoreRemoteApiError.UNSERIALIZATION_ERROR, e));
		}
	}
	
	protected <V> List<V> getListFromJsonString(String jsonString, Class<V> valueClass) {
		try {
			return OBJECT_MAPPER.readValue(jsonString,
					CollectionType.construct(ArrayList.class,
							SimpleType.construct(valueClass)));
		} catch (Exception e) {
			LOGGER.error(CoreRemoteApiError.UNSERIALIZATION_ERROR.getMessage(), e);
			
			throw(getException(CoreRemoteApiError.UNSERIALIZATION_ERROR, e));
		}
	}
	
	protected <V> V getObjectFromJsonString(String jsonString, Class<V> valueClass) {
		try {
			return OBJECT_MAPPER.readValue(jsonString, valueClass);
		} catch (Exception e) {
			LOGGER.error("Unserialization error", e);
			
			throw(getException(CoreRemoteApiError.UNSERIALIZATION_ERROR, e));
		}
	}
	
	protected Map<String, File> getFileMapFromFormDataBodyPartList(List<FormDataBodyPart> parts) {
		Map<String, File> files = new LinkedHashMap<String, File>();
		
		if (parts != null) {
			for (FormDataBodyPart part : parts) {
				String fileName = part.getContentDisposition().getFileName();
				File file = part.getValueAs(File.class);
				
				if (StringUtils.hasText(fileName) && file != null && file.canRead()) {
					files.put(fileName, file);
				}
			}
		}
		
		return files;
	}
	
	protected File getFileFromFormDataBodyPart(FormDataBodyPart part) {
		File file = part.getValueAs(File.class);
		if (file != null && file.canRead()) {
			return file;
		} else {
			return null;
		}
	}
}