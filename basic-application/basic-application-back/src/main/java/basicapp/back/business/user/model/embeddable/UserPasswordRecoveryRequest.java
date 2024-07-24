package basicapp.back.business.user.model.embeddable;

import basicapp.back.business.user.model.atomic.UserPasswordRecoveryRequestInitiator;
import basicapp.back.business.user.model.atomic.UserPasswordRecoveryRequestType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Transient;
import java.io.Serializable;
import java.time.Instant;
import org.bindgen.Bindable;

@Embeddable
@Bindable
public class UserPasswordRecoveryRequest implements Serializable {

  private static final long serialVersionUID = 5217823856674984551L;

  @Column private String token;

  @Column private Instant creationDate;

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

  public Instant getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Instant creationDate) {
    this.creationDate = creationDate;
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
