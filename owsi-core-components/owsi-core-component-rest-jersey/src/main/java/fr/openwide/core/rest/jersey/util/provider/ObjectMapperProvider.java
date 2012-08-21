package fr.openwide.core.rest.jersey.util.provider;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Provider
@Component("objectMapperProvider")
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {
	
	private ObjectMapper objectMapper;
	
	public ObjectMapperProvider() {
		this.objectMapper = new ObjectMapper();
		this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	}

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return objectMapper;
	}

}
