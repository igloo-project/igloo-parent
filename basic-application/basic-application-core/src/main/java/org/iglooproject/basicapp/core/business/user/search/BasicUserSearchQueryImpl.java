package org.iglooproject.basicapp.core.business.user.search;

import org.iglooproject.basicapp.core.business.user.model.BasicUser;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class BasicUserSearchQueryImpl extends AbstractUserSearchQueryImpl<BasicUser>
    implements IBasicUserSearchQuery {

  protected BasicUserSearchQueryImpl() {
    super(BasicUser.class);
  }
}
