package basicapp.back.business.referencedata.search;

import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;

public interface IAbstractBasicReferenceDataSearchQueryDataBindingInterface {

  String getLabel();

  void setLabel(String label);

  EnabledFilter getEnabledFilter();

  void setEnabledFilter(EnabledFilter enabledFilter);
}
