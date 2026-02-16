package igloo.bootstrap.js.statement;

import igloo.bootstrap.js.util.ImmutableStyle;
import jakarta.annotation.Nullable;
import java.io.Serializable;
import java.time.LocalTime;
import org.apache.wicket.model.IModel;
import org.immutables.value.Value;

@Value.Immutable
@ImmutableStyle
public interface IJsLocalTimeModel extends IJsStatement, Serializable {

  @Value.Parameter
  @Nullable
  IModel<LocalTime> value();

  @Override
  default String render() {
    LocalTime value = value() != null ? value().getObject() : null;
    return value != null
        ? "{ hours: %s, minutes: %s, seconds: %s }"
            .formatted(value.getHour(), value.getMinute(), value.getSecond())
        : "null";
  }
}
