package igloo.bootstrap.js.statement;

import igloo.bootstrap.js.util.ImmutableStyle;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import javax.annotation.Nullable;
import org.apache.wicket.model.IModel;
import org.immutables.value.Value;

@Value.Immutable
@ImmutableStyle
public interface IJsLocalDateTimeModel extends IJsStatement, Serializable {

  @Value.Parameter
  @Nullable
  IModel<LocalDateTime> value();

  @Override
  default String render() {
    LocalDateTime value = value() != null ? value().getObject() : null;
    return value != null
        ? String.valueOf(value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
        : "null";
  }
}
