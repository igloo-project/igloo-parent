package basicapp.front.notification.model;

import jakarta.annotation.Nonnull;
import java.time.Instant;
import java.util.Date;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class InstantToDateModel extends LoadableDetachableModel<Date> {

  private static final long serialVersionUID = 8339399181360049837L;

  @Nonnull private final IModel<Instant> fromModel;

  public InstantToDateModel(@Nonnull IModel<Instant> fromModel) {
    this.fromModel = fromModel;
  }

  @Override
  protected Date load() {
    if (fromModel.getObject() == null) {
      return null;
    } else {
      return Date.from(fromModel.getObject());
    }
  }

  @Override
  public void detach() {
    super.detach();
    fromModel.detach();
  }
}
