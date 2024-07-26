package org.iglooproject.basicapp.core.business.history.service;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.service.IUserService;
import org.iglooproject.jpa.more.business.history.service.AbstractHistoryEventSummaryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoryEventSummaryServiceImpl extends AbstractHistoryEventSummaryServiceImpl<User>
    implements IHistoryEventSummaryService {

  @Autowired private IUserService userService;

  @Override
  protected User getDefaultSubject() {
    return userService.getAuthenticatedUser();
  }
}
