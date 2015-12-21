package fr.openwide.core.jpa.more.business.history.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

@Service
public class HistoryValueServiceImpl implements IHistoryValueService {
	
	@Autowired
	private IEntityService entityService;
	
	@Autowired
	private IRendererService rendererService;
	
	@Autowired
	private IPropertyService propertyService;

	@Override
	public <T> HistoryValue create(T object) {
		if (object == null) {
			return new HistoryValue();
		} else {
			@SuppressWarnings("unchecked")
			IRenderer<? super T> renderer = rendererService.findRenderer((Class<T>)object.getClass());
			return create(object, renderer);
		}
	}

	@Override
	public <T> HistoryValue create(T value, IRenderer<? super T> renderer) {
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
	
	private String serialize(Object value) {
		if (value instanceof Enum) {
			return ((Enum<?>) value).name();
		}
		return null;
	}
	
	@Override
	public GenericEntity<Long, ?> retrieve(HistoryValue value) {
		if (value == null) {
			return null;
		}
		
		HistoryEntityReference reference = value.getEntityReference();
		if (reference == null || reference.getEntityClass() == null || reference.getEntityId() == null) {
			return null;
		}
		
		return entityService.getEntity(reference);
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String render(HistoryValue value, IRenderer renderer) {
		if (value == null) {
			return null;
		}
		
		if (renderer != null) {
			GenericEntity<Long, ?> entity = retrieve(value);
			if (entity != null) {
				return renderer.render(entity, propertyService.get(SpringPropertyIds.DEFAULT_LOCALE));
			}
		}
		
		return value.getLabel();
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String render(HistoryValue value) {
		if (value == null) {
			return null;
		}

		GenericEntity<Long, ?> entity = retrieve(value);
		if (entity != null) {
			IRenderer renderer = rendererService.findRenderer(HibernateUtils.getClass(entity));
			return renderer.render(entity, propertyService.get(SpringPropertyIds.DEFAULT_LOCALE));
		}
		
		return value.getLabel();
	}

}
