package org.iglooproject.basicapp.core.business.user.model.embeddable;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import org.bindgen.Bindable;
import org.iglooproject.basicapp.core.business.user.model.atomic.UserPasswordRecoveryRequestInitiator;
import org.iglooproject.basicapp.core.business.user.model.atomic.UserPasswordRecoveryRequestType;
import org.iglooproject.commons.util.CloneUtils;

@Embeddable
@Bindable
public class UserPasswordRecoveryRequest implements Serializable {

  private static final long serialVersionUID = 5217823856674984551L;

  @Column private String token;

  @Column private Date creationDate;

  @Column
  @Enumerated(EnumType.STRING)
  private UserPasswordRecoveryRequestType type;

  @Column
  @Enumerated(EnumType.STRING)
  private UserPasswordRecoveryRequestInitiator initiator;

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Date getCreationDate() {
    return CloneUtils.clone(creationDate);
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = CloneUtils.clone(creationDate);
  }

  public UserPasswordRecoveryRequestType getType() {
    return type;
  }

  public void setType(UserPasswordRecoveryRequestType type) {
    this.type = type;
  }

  public UserPasswordRecoveryRequestInitiator getInitiator() {
    return initiator;
  }

  public void setInitiator(UserPasswordRecoveryRequestInitiator initiator) {
    this.initiator = initiator;
  }

  @Transient
  public void reset() {
    setToken(null);
    setCreationDate(null);
    setType(null);
    setInitiator(null);
  }
}
