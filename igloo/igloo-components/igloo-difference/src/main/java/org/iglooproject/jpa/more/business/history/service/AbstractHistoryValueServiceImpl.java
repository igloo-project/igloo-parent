package org.iglooproject.jpa.more.business.history.service;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Locale;
import org.iglooproject.commons.util.rendering.IRenderer;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.model.GenericEntityReference;
import org.iglooproject.jpa.business.generic.model.IReference;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryEventValue;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryValue;
import org.iglooproject.jpa.more.business.history.model.embeddable.IHistoryValue;
import org.iglooproject.jpa.more.business.history.model.embeddable.IHistoryValueProvider;
import org.iglooproject.jpa.more.rendering.service.IRendererService;
import org.iglooproject.jpa.util.HibernateUtils;
import org.iglooproject.spring.property.SpringPropertyIds;
import org.iglooproject.spring.property.service.IPropertyService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractHistoryValueServiceImpl implements IHistoryValueService {

  @PersistenceContext private EntityManager entityManager;

  @Autowired protected IRendererService rendererService;

  @Autowired protected IPropertyService propertyService;

  @Override
  public final <T> HistoryValue createHistoryValue(T object) {
    return create(object, HistoryValue::build);
  }

  @Override
  public final <T> HistoryEventValue createHistoryEventValue(T object) {
    return create(object, HistoryEventValue::build);
  }

  @Override
  public final <T> HistoryValue createHistoryValue(T object, IRenderer<? super T> renderer) {
    return create(object, renderer, HistoryValue::build);
  }

  @Override
  public final <T> HistoryEventValue createHistoryEventValue(
      T object, IRenderer<? super T> renderer) {
    return create(object, renderer, HistoryEventValue::build);
  }

  /*
   * Even if object.getClass() is a subtype of T (say T2), this will work.
   *
   * The returned renderer will actually be of type IRenderer<? super T2>.
   * This is enough to allow calling renderer.render(object) at runtime, even though at
   * compile time, it's a bit dodgy. Maybe we could write something like object.getClass().cast(object)
   * just to get the right type, but it would seem a little dumb...
   */
  @SuppressWarnings("unchecked")
  public final <T, H extends IHistoryValue> H create(T object, IHistoryValueProvider<H> supplier) {
    if (object == null) {
      return supplier.build(null, null, null);
    } else {
      @SuppressWarnings("rawtypes")
      IRenderer renderer = rendererService.findRenderer(object.getClass());
      return (H) this.<T, H>create(object, renderer, supplier);
    }
  }

  public final <T, H extends IHistoryValue> H create(
      T value, IRenderer<? super T> renderer, IHistoryValueProvider<H> supplier) {
    String label = renderer.render(value, propertyService.get(SpringPropertyIds.DEFAULT_LOCALE));

    if (value instanceof GenericEntity) {
      GenericEntity<?, ?> entity = (GenericEntity<?, ?>) value;
      if (entity.getId() instanceof Long) {
        @SuppressWarnings("unchecked")
        GenericEntityReference<Long, ?> reference =
            GenericEntityReference.of((GenericEntity<Long, ?>) entity);
        return supplier.build(label, null, reference);
      }
    }

    String serialized = serialize(value);
    return supplier.build(label, serialized, null);
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
  public final Object retrieve(IHistoryValue value) {
    if (value == null) {
      return null;
    }

    GenericEntityReference<Long, GenericEntity<Long, ?>> reference = value.getReference();
    if (reference != null && reference.getType() != null && reference.getId() != null) {
      return getEntity(reference);
    }

    return deserialize(value.getSerialized());
  }

  @VisibleForTesting
  public <E extends GenericEntity<?, ?>> E getEntity(IReference<E> reference) {
    return entityManager.find(reference.getType(), reference.getId());
  }

  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  public final String render(IHistoryValue value, IRenderer renderer, Locale locale) {
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
  @SuppressWarnings({"unchecked", "rawtypes"})
  public final String render(IHistoryValue value, Locale locale) {
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
  public Optional<Boolean> matches(IHistoryValue historyValue, Object value) {
    if (historyValue == null) {
      // We never return null from create(...). Thus a null HistoryValue does not match anything.
      return Optional.of(false);
    }

    GenericEntityReference<Long, ?> referenceReference = historyValue.getReference();
    if (referenceReference != null) {
      if (!(value instanceof GenericEntity)) {
        return Optional.of(false);
      }
      GenericEntityReference<?, ?> candidateReference =
          GenericEntityReference.of((GenericEntity<?, ?>) value);
      return Optional.of(referenceReference.equals(candidateReference));
    }

    // Don't know
    return Optional.absent();
  }
}
