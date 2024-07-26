package org.iglooproject.basicapp.core.business.user.search;

import org.iglooproject.basicapp.core.business.user.model.TechnicalUser;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class TechnicalUserSearchQueryImpl extends AbstractUserSearchQueryImpl<TechnicalUser>
    implements ITechnicalUserSearchQuery {

  protected TechnicalUserSearchQueryImpl() {
    super(TechnicalUser.class);
  }
}
