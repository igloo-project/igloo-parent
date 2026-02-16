package igloo.bootstrap.js.statement;

import com.github.openjson.JSONArray;
import igloo.bootstrap.js.util.ImmutableStyle;
import jakarta.annotation.Nullable;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.wicket.model.IModel;
import org.immutables.value.Value;
import org.javatuples.Pair;

@Value.Immutable
@ImmutableStyle
public interface IJsLocalDatePairModel extends IJsStatement, Serializable {

  @Value.Parameter
  @Nullable
  IModel<Pair<LocalDate, LocalDate>> values();

  @Override
  default String render() {
    Pair<LocalDate, LocalDate> values = values() != null ? values().getObject() : null;
    return values != null
        ? new JSONArray(
                Stream.of(values.getValue0(), values.getValue1())
                    .filter(Objects::nonNull)
                    .map(
                        date ->
                            date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli())
                    .toList())
            .toString()
        : "[]";
  }
}
