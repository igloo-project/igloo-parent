package org.iglooproject.rest.jersey2.util.jackson.serializer;

import java.io.IOException;

import org.hibernate.proxy.HibernateProxy;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;

import org.iglooproject.jpa.util.HibernateUtils;

/**
 * BeanSerializerModifier that correctly handles Hibernate proxies.
 */
public class HibernateBeanSerializerModifier extends BeanSerializerModifier {

	@Override
	public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc,
			JsonSerializer<?> serializer) {
		if (serializer instanceof BeanSerializer) {
			return new BeanSerializerWrapper((BeanSerializer) serializer);
		} else {
			return serializer;
		}
	}

	public static class BeanSerializerWrapper extends BeanSerializer {

		private static final long serialVersionUID = 1L;
		
		private final BeanSerializer serializer;

		protected BeanSerializerWrapper(BeanSerializer src) {
			super(src);
			this.serializer = src;
		}

		protected BeanSerializerWrapper(BeanSerializerWrapper src, ObjectIdWriter objectIdWriter) {
			super(src.serializer, objectIdWriter);
			this.serializer = src;
		}

		@Override
		public BeanSerializerWrapper withObjectIdWriter(ObjectIdWriter objectIdWriter) {
			return new BeanSerializerWrapper(this, objectIdWriter);
		}

		private <T> T unwrapBean(T bean) {
			// Pour serializeFields* :
			// C'est une étape non nécessaire car le serializerFactory utilisé en amont a modifié le type
			// d'introspection. La liste des propriétés sera donc la bonne.
			// On réalise quand même l'unwrapping pour plus de cohérence.
			// Pour serializeWithType : absolument nécessaire, car la sérialisation du type ne prend pas du tout
			// en compte ce qu'on a fait dans serializerFactory.
			// pour les IObjetTouristiqueLinkedReference, on n'unwrappe pas le bean car on le type statiquement et on
			// se contente de l'identifiant.
			if (bean instanceof HibernateProxy) {
				bean = HibernateUtils.unwrap(bean);
			}
			return bean;
		}
		
		@Override
		protected void serializeFieldsFiltered(Object bean, JsonGenerator jgen, SerializerProvider provider)
				throws IOException, JsonGenerationException {
			if (bean == null) {
				return;
			} else {
				Object object = unwrapBean(bean);
				super.serializeFieldsFiltered(object, jgen, provider);
			}
		}
		
		@Override
		protected void serializeFields(Object bean, JsonGenerator jgen, SerializerProvider provider)
				throws IOException, JsonGenerationException {
			if (bean == null) {
				return;
			} else {
				Object object = unwrapBean(bean);
				super.serializeFields(object, jgen, provider);
			}
		}
		
		@Override
		public void serializeWithType(Object bean, JsonGenerator jgen, SerializerProvider provider,
				TypeSerializer typeSer) throws IOException, JsonGenerationException {
			if (bean == null) {
				return;
			} else {
				Object object = unwrapBean(bean);
				super.serializeWithType(object, jgen, provider, typeSer);
			}
		}
		
	}

}
