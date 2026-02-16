package igloo.bootstrap.js.statement;

import igloo.bootstrap.js.util.ImmutableStyle;
import jakarta.annotation.Nullable;
import java.io.Serializable;
import java.time.YearMonth;
import java.time.ZoneId;
import org.apache.wicket.model.IModel;
import org.immutables.value.Value;

@Value.Immutable
@ImmutableStyle
public interface IJsYearMonthModel extends IJsStatement, Serializable {

  @Value.Parameter
  @Nullable
  IModel<YearMonth> value();

  @Override
  default String render() {
    YearMonth value = value() != null ? value().getObject() : null;
    return value != null
        ? String.valueOf(
            value.atDay(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli())
        : "null";
  }
}
