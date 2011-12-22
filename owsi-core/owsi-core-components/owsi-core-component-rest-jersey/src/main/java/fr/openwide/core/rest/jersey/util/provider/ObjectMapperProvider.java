package fr.openwide.core.rest.jersey.util.provider;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.springframework.stereotype.Component;

@Provider
@Component("objectMapperProvider")
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {
	
	private ObjectMapper objectMapper;
	
	public ObjectMapperProvider() {
		this.objectMapper = new ObjectMapper();
		this.objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
	}

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return objectMapper;
	}

}
