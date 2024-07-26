package org.iglooproject.basicapp.core.business.announcement.model.embeddable;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.bindgen.Bindable;
import org.iglooproject.commons.util.CloneUtils;

@Embeddable
@Bindable
public class AnnouncementDate implements Serializable {

  private static final long serialVersionUID = -6711976886958374843L;

  @Basic
  @Temporal(TemporalType.TIMESTAMP)
  private Date startDateTime;

  @Basic
  @Temporal(TemporalType.TIMESTAMP)
  private Date endDateTime;

  public Date getStartDateTime() {
    return CloneUtils.clone(startDateTime);
  }

  public void setStartDateTime(Date startDateTime) {
    this.startDateTime = CloneUtils.clone(startDateTime);
  }

  public Date getEndDateTime() {
    return CloneUtils.clone(endDateTime);
  }

  public void setEndDateTime(Date endDateTime) {
    this.endDateTime = CloneUtils.clone(endDateTime);
  }
}
