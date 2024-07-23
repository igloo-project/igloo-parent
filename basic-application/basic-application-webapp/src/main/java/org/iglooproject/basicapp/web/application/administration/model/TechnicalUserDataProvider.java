package org.iglooproject.basicapp.web.application.administration.model;

import org.iglooproject.basicapp.core.business.user.model.TechnicalUser;
import org.iglooproject.basicapp.core.business.user.search.ITechnicalUserSearchQuery;

public class TechnicalUserDataProvider extends AbstractUserDataProvider<TechnicalUser> {

  private static final long serialVersionUID = -8540890431031886412L;

  public TechnicalUserDataProvider() {
    super(TechnicalUser.class);
  }

  @Override
  protected ITechnicalUserSearchQuery createSearchQuery() {
    return createSearchQuery(ITechnicalUserSearchQuery.class);
  }
}
