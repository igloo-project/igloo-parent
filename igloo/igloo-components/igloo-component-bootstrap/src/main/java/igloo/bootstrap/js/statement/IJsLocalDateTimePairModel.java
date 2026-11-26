package igloo.bootstrap.js.statement;

import igloo.bootstrap.js.util.ImmutableStyle;
import jakarta.annotation.Nullable;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;
import org.apache.wicket.model.IModel;
import org.immutables.value.Value;
import org.javatuples.Pair;

@Value.Immutable
@ImmutableStyle
public interface IJsLocalDateTimePairModel extends IJsStatement, Serializable {

  @Value.Parameter
  @Nullable
  IModel<Pair<LocalDateTime, LocalDateTime>> values();

  /**
   * Both dates in an array, or an empty array if one is missing: the datepicker cannot display a
   * range with a single date, it would take an end date for a start date.
   */
  @Override
  default String render() {
    return Optional.ofNullable(values())
        .map(IModel::getObject)
        .filter(range -> range.getValue0() != null && range.getValue1() != null)
        .map(
            range ->
                "[%d,%d]"
                    .formatted(
                        Util.epochMilli(range.getValue0()), Util.epochMilli(range.getValue1())))
        .orElse("[]");
  }
}
