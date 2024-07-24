package basicapp.back.business.user.search;

import basicapp.back.business.user.model.BasicUser;
import basicapp.back.business.user.model.TechnicalUser;
import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.UserGroup;
import org.bindgen.Bindable;
import org.iglooproject.jpa.more.search.query.ISearchQueryData;

@Bindable
public class UserGroupSearchQueryData implements ISearchQueryData<UserGroup> {

  private String name;

  private User user;

  private BasicUser basicUser;

  private TechnicalUser technicalUser;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public BasicUser getBasicUser() {
    return basicUser;
  }

  public void setBasicUser(BasicUser basicUser) {
    this.basicUser = basicUser;
  }

  public TechnicalUser getTechnicalUser() {
    return technicalUser;
  }

  public void setTechnicalUser(TechnicalUser technicalUser) {
    this.technicalUser = technicalUser;
  }
}
