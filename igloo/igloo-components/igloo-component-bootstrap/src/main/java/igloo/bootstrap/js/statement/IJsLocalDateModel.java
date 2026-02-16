package igloo.bootstrap.js.statement;

import igloo.bootstrap.js.util.ImmutableStyle;
import jakarta.annotation.Nullable;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import org.apache.wicket.model.IModel;
import org.immutables.value.Value;

@Value.Immutable
@ImmutableStyle
public interface IJsLocalDateModel extends IJsStatement, Serializable {

  @Value.Parameter
  @Nullable
  IModel<LocalDate> value();

  @Override
  default String render() {
    LocalDate value = value() != null ? value().getObject() : null;
    return value != null
        ? String.valueOf(value.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli())
        : "null";
  }
}
