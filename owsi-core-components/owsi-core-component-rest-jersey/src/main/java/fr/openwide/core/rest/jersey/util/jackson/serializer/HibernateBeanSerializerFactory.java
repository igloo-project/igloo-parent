package fr.openwide.core.rest.jersey.util.jackson.serializer;

import org.hibernate.proxy.HibernateProxy;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.cfg.SerializerFactoryConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class HibernateBeanSerializerFactory extends BeanSerializerFactory {

	private static final long serialVersionUID = -7256206834065593118L;

	public HibernateBeanSerializerFactory() {
		super(null);
	}

	protected HibernateBeanSerializerFactory(SerializerFactoryConfig config) {
		super(config);
	}

	@Override
	public SerializerFactory withConfig(SerializerFactoryConfig config) {
		return new HibernateBeanSerializerFactory(config);
	}

	@Override
	public JsonSerializer<Object> createSerializer(SerializerProvider prov, JavaType origType)
			throws JsonMappingException {
		Class<?> clazz = origType.getRawClass();
		
		// nécessaire sinon les accesseurs de propriétés sont générés sur le type javassist
		if (HibernateProxy.class.isAssignableFrom(clazz)) {
			origType = SimpleType.construct(clazz.getSuperclass());
		}
		
		// Well, then it is not a Hibernate proxy
		return super.createSerializer(prov, origType);
	}

}
