package org.iglooproject.basicapp.web.application.console.notification.demo.util;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;
import java.io.Serializable;
import java.util.Collection;
import org.apache.wicket.Localizer;
import org.apache.wicket.Session;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Classes;
import org.iglooproject.basicapp.core.business.notification.service.IBasicApplicationNotificationContentDescriptorFactory;
import org.iglooproject.basicapp.web.application.console.notification.demo.page.ConsoleNotificationDemoIndexPage;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.functional.SerializablePredicate2;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.service.IEntityService;
import org.iglooproject.spring.notification.model.INotificationContentDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class NotificationDemoEntry implements IModel<INotificationContentDescriptor> {

  private static final Logger LOGGER = LoggerFactory.getLogger(NotificationDemoEntry.class);

  private static final long serialVersionUID = -4349817230833254700L;

  @SpringBean protected IEntityService entityService;

  @SpringBean protected IBasicApplicationNotificationContentDescriptorFactory descriptorService;

  private final String messageKeySuffix;

  public NotificationDemoEntry(String messageKeySuffix) {
    super();
    this.messageKeySuffix = messageKeySuffix;
    Injector.get().inject(this);
  }

  public IModel<String> getLabelModel() {
    return new ResourceModel(
        "console.notifications." + messageKeySuffix, messageKeySuffix /* Default value */);
  }

  @Override
  public final INotificationContentDescriptor getObject() {
    return getDescriptor();
  }

  protected abstract INotificationContentDescriptor getDescriptor();

  protected final <E extends GenericEntity<Long, ?>> E getFirstInRange(
      Class<E> clazz, long minId, long maxId) {
    return getFirstInRange(clazz, Range.closed(minId, maxId));
  }

  protected final <E extends GenericEntity<Long, ?>> E getFirstInRange(
      Class<E> clazz, Range<Long> range) {
    return getFirstInRange(clazz, range, Predicates2.<E>alwaysTrue());
  }

  protected final <E extends GenericEntity<Long, ?>> E getFirstInRange(
      Class<E> clazz, Range<Long> range, SerializablePredicate2<E> predicate) {
    E entity =
        getFirstWithId(clazz, ContiguousSet.create(range, DiscreteDomain.longs()), predicate);

    if (entity != null) {
      return entity;
    } else {
      LOGGER.error("A demo object is missing for demo " + Classes.simpleName(getClass()));
      Session.get()
          .error(Localizer.get().getString("console.notifications.demo.noDataAvailable", null));

      throw ConsoleNotificationDemoIndexPage.linkDescriptor().newRestartResponseException();
    }
  }

  protected final <K extends Comparable<K> & Serializable, E extends GenericEntity<K, ?>>
      E getFirstWithId(Class<E> clazz, Collection<K> ids, SerializablePredicate2<E> predicate) {
    for (K id : ids) {
      E entity = entityService.getEntity(clazz, id);
      if (entity != null && predicate.test(entity)) {
        return entity;
      }
    }

    return null;
  }
}
