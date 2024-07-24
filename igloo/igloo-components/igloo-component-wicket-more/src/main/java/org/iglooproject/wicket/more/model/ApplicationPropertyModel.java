package org.iglooproject.wicket.more.model;

import com.google.common.base.Preconditions;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.spring.property.model.PropertyId;
import org.iglooproject.spring.property.service.IPropertyService;

public class ApplicationPropertyModel<T> extends LoadableDetachableModel<T> {

  private static final long serialVersionUID = 7221634823252925011L;

  @SpringBean private IPropertyService propertyService;

  private final PropertyId<T> propertyId;

  public static <T> ApplicationPropertyModel<T> of(PropertyId<T> propertyId) {
    Preconditions.checkNotNull(propertyId);
    return new ApplicationPropertyModel<>(propertyId);
  }

  private ApplicationPropertyModel(PropertyId<T> propertyId) {
    super();
    Injector.get().inject(this);
    Preconditions.checkNotNull(propertyId);
    this.propertyId = propertyId;
  }

  @Override
  protected T load() {
    return propertyService.get(propertyId);
  }

  public PropertyId<T> getPropertyId() {
    return propertyId;
  }
}
