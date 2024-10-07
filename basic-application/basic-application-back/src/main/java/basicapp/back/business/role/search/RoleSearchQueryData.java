package basicapp.back.business.role.search;

import basicapp.back.business.role.model.Role;
import basicapp.back.business.user.model.User;
import org.bindgen.Bindable;
import org.iglooproject.jpa.more.search.query.ISearchQueryData;

@Bindable
public class RoleSearchQueryData implements ISearchQueryData<Role> {

  private User user;

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
