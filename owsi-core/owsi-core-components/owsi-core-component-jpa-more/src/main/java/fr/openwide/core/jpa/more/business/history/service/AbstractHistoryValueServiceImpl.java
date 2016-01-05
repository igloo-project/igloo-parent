package fr.openwide.core.jpa.more.business.history.service;

import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.commons.util.rendering.IRenderer;
import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.model.GenericEntityReference;
import fr.openwide.core.jpa.business.generic.service.IEntityService;
import fr.openwide.core.jpa.more.business.history.model.embeddable.HistoryEntityReference;
import fr.openwide.core.jpa.more.business.history.model.embeddable.HistoryValue;
import fr.openwide.core.jpa.more.rendering.service.IRendererService;
import fr.openwide.core.jpa.util.HibernateUtils;
import fr.openwide.core.spring.property.SpringPropertyIds;
import fr.openwide.core.spring.property.service.IPropertyService;

public class AbstractHistoryValueServiceImpl implements IHistoryValueService {
	
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
		
		HistoryEntityReference reference = value.getEntityReference();
		if (reference != null && reference.getEntityClass() != null && reference.getEntityId() != null) {
			return entityService.getEntity(reference);
		}
		
		return deserialize(value.getSerialized());
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public final String render(HistoryValue value, IRenderer renderer) {
		if (value == null) {
			return null;
		}
		
		if (renderer != null) {
			Object retrieved = retrieve(value);
			if (retrieved != null) {
				return renderer.render(retrieved, propertyService.get(SpringPropertyIds.DEFAULT_LOCALE));
			}
		}
		
		return value.getLabel();
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public final String render(HistoryValue value) {
		if (value == null) {
			return null;
		}

		Object retrieved = retrieve(value);
		if (retrieved != null) {
			IRenderer renderer = rendererService.findRenderer(HibernateUtils.getClass(retrieved));
			return renderer.render(retrieved, propertyService.get(SpringPropertyIds.DEFAULT_LOCALE));
		}
		
		return value.getLabel();
	}

}
