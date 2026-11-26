package igloo.bootstrap.js.statement;

import igloo.bootstrap.js.util.ImmutableStyle;
import jakarta.annotation.Nullable;
import java.io.Serializable;
import java.time.Year;
import org.apache.wicket.model.IModel;
import org.immutables.value.Value;

@Value.Immutable
@ImmutableStyle
public interface IJsYearModel extends IJsStatement, Serializable {

  @Value.Parameter
  @Nullable
  IModel<Year> value();

  @Override
  default String render() {
    Year value = value() != null ? value().getObject() : null;
    return value != null ? String.valueOf(Util.epochMilli(value.atDay(1))) : "null";
  }
}
