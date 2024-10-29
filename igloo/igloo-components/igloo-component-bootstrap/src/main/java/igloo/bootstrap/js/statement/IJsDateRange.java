package igloo.bootstrap.js.statement;

import igloo.bootstrap.js.util.ImmutableStyle;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.Nullable;
import org.immutables.value.Value;
import org.javatuples.Pair;

@Value.Immutable
@ImmutableStyle
public interface IJsDateRange extends IJsStatement, Serializable {

  @Value.Parameter
  @Nullable
  Pair<Date, Date> values();

  @Override
  default String render() {
    if (values() == null || (values().getValue0() == null && values().getValue1() == null)) {
      return "[]";
    }
    String date1 = values().getValue0() == null ? "" : values().getValue0().getTime() + ", ";
    String date2 =
        values().getValue1() == null ? "" : String.valueOf(values().getValue1().getTime());
    return "[" + date1 + date2 + "]";
  }
}
