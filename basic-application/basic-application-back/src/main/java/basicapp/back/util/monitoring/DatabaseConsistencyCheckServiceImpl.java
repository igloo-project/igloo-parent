package basicapp.back.util.monitoring;

import basicapp.back.business.user.service.business.IUserService;
import org.iglooproject.jpa.util.IDatabaseConsistencyCheckService;
import org.springframework.stereotype.Service;

@Service
public class DatabaseConsistencyCheckServiceImpl implements IDatabaseConsistencyCheckService {

  private final IUserService userService;

  public DatabaseConsistencyCheckServiceImpl(IUserService userService) {
    this.userService = userService;
  }

  @Override
  public void checkDatabaseAccess() {
    userService.count();
  }

  @Override
  public void checkDatabaseConsistency() {
    // rien
  }

  @Override
  public void checkOnStartup(boolean allowCreateMissingElements) {
    // rien
  }
}
