package basicapp.back.business.announcement.model.embeddable;

import jakarta.persistence.Basic;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;
import org.bindgen.Bindable;

@Embeddable
@Bindable
public class AnnouncementDate implements Serializable {

  private static final long serialVersionUID = -6711976886958374843L;

  @Basic private LocalDateTime startDateTime;

  @Basic private LocalDateTime endDateTime;

  public LocalDateTime getStartDateTime() {
    return startDateTime;
  }

  public void setStartDateTime(LocalDateTime startDateTime) {
    this.startDateTime = startDateTime;
  }

  public LocalDateTime getEndDateTime() {
    return endDateTime;
  }

  public void setEndDateTime(LocalDateTime endDateTime) {
    this.endDateTime = endDateTime;
  }
}
