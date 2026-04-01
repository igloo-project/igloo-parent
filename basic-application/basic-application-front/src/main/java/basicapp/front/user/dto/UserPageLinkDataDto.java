package basicapp.front.user.dto;

import basicapp.back.business.user.model.atomic.UserType;
import basicapp.front.pagelink.dto.IPageLinkDataDto;

public class UserPageLinkDataDto implements IPageLinkDataDto {

  private static final long serialVersionUID = 1L;

  private Long id;

  private UserType type;

  private String username;

  public UserPageLinkDataDto() {}

  public UserPageLinkDataDto(Long id, UserType type, String username) {
    this.id = id;
    this.type = type;
    this.username = username;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UserType getType() {
    return type;
  }

  public void setType(UserType type) {
    this.type = type;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
