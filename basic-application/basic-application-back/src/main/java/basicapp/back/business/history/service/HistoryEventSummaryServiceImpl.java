package basicapp.back.business.history.service;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.service.business.IUserService;
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
