package igloo.bootstrap.js.statement;

import com.github.openjson.JSONArray;
import igloo.bootstrap.js.util.ImmutableStyle;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.immutables.value.Value;

@Value.Immutable
@ImmutableStyle
public interface IJsDateList extends IJsStatement, Serializable {

  @Value.Parameter
  @Nullable
  List<Date> values();

  @Override
  default String render() {
    return CollectionUtils.isEmpty(values())
        ? "[]"
        : new JSONArray(values().stream().map(Date::getTime).toList()).toString();
  }
}
