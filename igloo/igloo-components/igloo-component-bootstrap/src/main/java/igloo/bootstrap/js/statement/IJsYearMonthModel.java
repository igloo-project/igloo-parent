package igloo.bootstrap.js.statement;

import igloo.bootstrap.js.util.ImmutableStyle;
import jakarta.annotation.Nullable;
import java.io.Serializable;
import java.time.YearMonth;
import java.util.Optional;
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
    return Optional.ofNullable(value())
        .map(IModel::getObject)
        .map(value -> String.valueOf(Util.epochMilli(value.atDay(1))))
        .orElse("null");
  }
}
