package fr.openwide.core.rest.jersey.util.provider;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import fr.openwide.core.rest.jersey.util.jackson.module.HibernateModule;
import fr.openwide.core.rest.jersey.util.jackson.serializer.HibernateBeanSerializerFactory;

@Provider
@Component("objectMapperProvider")
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {
	
	private ObjectMapper objectMapper;
	
	public ObjectMapperProvider() {
		this.objectMapper = new ObjectMapper();
		// la modification de la factory doit être la première opération
		this.objectMapper.setSerializerFactory(new HibernateBeanSerializerFactory());
		this.objectMapper.registerModule(new HibernateModule());
		this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		this.objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
	}

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return objectMapper;
	}

}
