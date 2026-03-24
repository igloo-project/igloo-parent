package basicapp.back.business.user.difference.service;

import basicapp.back.business.user.model.User;
import igloo.difference.AbstractConfiguredDifferenceServiceImpl;
import igloo.difference.model.DifferenceFields;

public class UserDifferenceServiceImpl extends AbstractConfiguredDifferenceServiceImpl<User>
    implements IUserDifferenceService {

  public UserDifferenceServiceImpl(DifferenceFields fields) {
    super(fields);
  }
}
