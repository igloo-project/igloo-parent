package org.iglooproject.jpa.more.business.history.service;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Optional;

import org.iglooproject.commons.util.rendering.IRenderer;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.model.GenericEntityReference;
import org.iglooproject.jpa.business.generic.service.IEntityService;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryEntityReference;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryValue;
import org.iglooproject.jpa.more.rendering.service.IRendererService;
import org.iglooproject.jpa.util.HibernateUtils;
import org.iglooproject.spring.property.SpringPropertyIds;
import org.iglooproject.spring.property.service.IPropertyService;

public abstract class AbstractHistoryValueServiceImpl implements IHistoryValueService {
	
	@Autowired
	protected IEntityService entityService;
	
	@Autowired
	protected IRendererService rendererService;
	
	@Autowired
	protected IPropertyService propertyService;

	/*
	 * Even if object.getClass() is a subtype of T (say T2), this will work.
	 * 
	 * The returned renderer will actually be of type IRenderer<? super T2>.
	 * This is enough to allow calling renderer.render(object) at runtime, even though at
	 * compile time, it's a bit dodgy. Maybe we could write something like object.getClass().cast(object)
	 * just to get the right type, but it would seem a little dumb...
	 */
	@SuppressWarnings("unchecked")
	@Override
	public final <T> HistoryValue create(T object) {
		if (object == null) {
			return new HistoryValue();
		} else {
			@SuppressWarnings("rawtypes")
			IRenderer renderer = rendererService.findRenderer(object.getClass());
			return create(object, renderer);
		}
	}

	@Override
	public final <T> HistoryValue create(T value, IRenderer<? super T> renderer) {
		String label = renderer.render(value, propertyService.get(SpringPropertyIds.DEFAULT_LOCALE));
		
		if (value instanceof GenericEntity) {
			GenericEntity<?, ?> entity = (GenericEntity<?, ?>) value;
			if (entity.getId() instanceof Long) {
				@SuppressWarnings("unchecked")
				GenericEntityReference<Long, ?> reference = GenericEntityReference.of((GenericEntity<Long, ?>)entity);
				return new HistoryValue(label, reference);
			}
		}
		
		String serialized = serialize(value);
		return new HistoryValue(label, serialized);
	}
	
	protected String serialize(Object value) {
		if (value instanceof Enum) {
			return ((Enum<?>) value).name();
		}
		return null;
	}
	
	protected Object deserialize(String value) {
		return null; // Can't do a thing with the default implementation of serialize(Object)
	}
	
	@Override
	public final Object retrieve(HistoryValue value) {
		if (value == null) {
			return null;
		}
		
		HistoryEntityReference reference = value.getReference();
		if (reference != null && reference.getType() != null && reference.getId() != null) {
			return entityService.getEntity(reference);
		}
		
		return deserialize(value.getSerialized());
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public final String render(HistoryValue value, IRenderer renderer, Locale locale) {
		if (value == null) {
			return null;
		}
		
		if (renderer != null) {
			Object retrieved = retrieve(value);
			if (retrieved != null) {
				return renderer.render(retrieved, locale);
			}
		}
		
		return value.getLabel();
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public final String render(HistoryValue value, Locale locale) {
		if (value == null) {
			return null;
		}

		Object retrieved = retrieve(value);
		if (retrieved != null) {
			IRenderer renderer = rendererService.findRenderer(HibernateUtils.getClass(retrieved));
			return renderer.render(retrieved, locale);
		}
		
		return value.getLabel();
	}
	
	@Override
	public Optional<Boolean> matches(HistoryValue historyValue, Object value) {
		if (historyValue == null) {
			// We never return null from create(...). Thus a null HistoryValue does not match anything.
			return Optional.of(false);
		}
		
		GenericEntityReference<Long, ?> referenceReference = historyValue.getReference();
		if (referenceReference != null) {
			if (!(value instanceof GenericEntity)) {
				return Optional.of(false);
			}
			GenericEntityReference<?, ?> candidateReference = GenericEntityReference.of((GenericEntity<?, ?>)value);
			return Optional.of(referenceReference.equals(candidateReference));
		}
		
		// Don't know
		return Optional.absent();
	}

}
