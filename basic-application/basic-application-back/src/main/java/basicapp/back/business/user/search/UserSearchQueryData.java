package basicapp.back.business.user.search;

import basicapp.back.business.role.model.Role;
import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.atomic.UserType;
import org.bindgen.Bindable;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.jpa.more.search.query.ISearchQueryData;

@Bindable
public class UserSearchQueryData implements ISearchQueryData<User> {

  private String term;

  private UserType type;

  private String lastName;

  private String firstName;

  private String username;

  private String email;

  private Role role;

  private EnabledFilter active = EnabledFilter.ENABLED_ONLY;

  public String getTerm() {
    return term;
  }

  public void setTerm(String term) {
    this.term = term;
  }

  public UserType getType() {
    return type;
  }

  public void setType(UserType type) {
    this.type = type;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public EnabledFilter getActive() {
    return active;
  }

  public void setActive(EnabledFilter active) {
    this.active = active;
  }
}
