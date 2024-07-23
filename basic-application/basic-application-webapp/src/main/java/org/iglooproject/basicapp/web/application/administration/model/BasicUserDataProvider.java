package org.iglooproject.basicapp.web.application.administration.model;

import org.iglooproject.basicapp.core.business.user.model.BasicUser;
import org.iglooproject.basicapp.core.business.user.search.IBasicUserSearchQuery;

public class BasicUserDataProvider extends AbstractUserDataProvider<BasicUser> {

  private static final long serialVersionUID = -8540890431031886412L;

  public BasicUserDataProvider() {
    super(BasicUser.class);
  }

  @Override
  protected IBasicUserSearchQuery createSearchQuery() {
    return createSearchQuery(IBasicUserSearchQuery.class);
  }
}
