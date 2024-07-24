package basicapp.back.business.referencedata.search;

import basicapp.back.business.referencedata.model.ReferenceData;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.jpa.more.search.query.ISearchQueryData;

public abstract class AbstractReferenceDataSearchQueryData<T extends ReferenceData<? super T>>
    implements ISearchQueryData<T>, IAbstractBasicReferenceDataSearchQueryDataBindingInterface {

  private String label;

  private EnabledFilter enabledFilter = EnabledFilter.ENABLED_ONLY;

  @Override
  public String getLabel() {
    return label;
  }

  @Override
  public void setLabel(String label) {
    this.label = label;
  }

  @Override
  public EnabledFilter getEnabledFilter() {
    return enabledFilter;
  }

  @Override
  public void setEnabledFilter(EnabledFilter enabledFilter) {
    this.enabledFilter = enabledFilter;
  }
}
