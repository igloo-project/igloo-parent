package igloo.bootstrap.js.statement;

import com.github.openjson.JSONArray;
import igloo.bootstrap.js.util.ImmutableStyle;
import jakarta.annotation.Nullable;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.wicket.model.IModel;
import org.immutables.value.Value;
import org.javatuples.Pair;

@Value.Immutable
@ImmutableStyle
public interface IJsLocalDateTimePairModel extends IJsStatement, Serializable {

  @Value.Parameter
  @Nullable
  IModel<Pair<LocalDateTime, LocalDateTime>> values();

  @Override
  default String render() {
    Pair<LocalDateTime, LocalDateTime> values = values() != null ? values().getObject() : null;
    return values != null
        ? new JSONArray(
                Stream.of(values.getValue0(), values.getValue1())
                    .filter(Objects::nonNull)
                    .map(date -> date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                    .toList())
            .toString()
        : "[]";
  }
}
