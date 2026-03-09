package org.iglooproject.wicket.more.markup.html.model;

import jakarta.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.jpa.more.business.referencedata.model.GenericReferenceData;
import org.iglooproject.jpa.more.business.referencedata.service.IGenericReferenceDataService;

public class GenericReferenceDataModel<T extends GenericReferenceData<?, ?>>
    extends LoadableDetachableModel<List<T>> {

  private static final long serialVersionUID = -8014868217254919305L;

  private Class<T> clazz;
  private final Comparator<? super T> comparator;
  private final EnabledFilter enabledFilter;

  @SpringBean private IGenericReferenceDataService genericReferenceDataService;

  public GenericReferenceDataModel(Class<T> clazz, EnabledFilter enabledFilter) {
    this(clazz, null, enabledFilter);
  }

  public GenericReferenceDataModel(Class<T> clazz, @Nullable Comparator<? super T> comparator) {
    this(clazz, comparator, EnabledFilter.ENABLED_ONLY);
  }

  public GenericReferenceDataModel(
      Class<T> clazz, @Nullable Comparator<? super T> comparator, EnabledFilter enabledFilter) {
    super();
    Injector.get().inject(this);

    this.clazz = clazz;
    this.enabledFilter = enabledFilter;
    this.comparator = comparator;
  }

  @Override
  protected List<T> load() {
    return genericReferenceDataService.list(clazz, enabledFilter, comparator);
  }
}
