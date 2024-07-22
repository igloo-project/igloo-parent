package basicapp.back.business.referencedata.search;

import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;

public interface IAbstractBasicReferenceDataSearchQueryDataBindingInterface {

  public String getLabel();

  public void setLabel(String label);

  public EnabledFilter getEnabledFilter();

  public void setEnabledFilter(EnabledFilter enabledFilter);
}
