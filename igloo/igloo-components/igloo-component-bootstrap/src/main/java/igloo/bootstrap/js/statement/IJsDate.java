package igloo.bootstrap.js.statement;

import igloo.bootstrap.js.util.ImmutableStyle;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.Nullable;
import org.immutables.value.Value;

@Value.Immutable
@ImmutableStyle
public interface IJsDate extends IJsStatement, Serializable {

  @Value.Parameter
  @Nullable
  Date value();

  @Override
  default String render() {
    Date value = value();
    return value != null ? String.valueOf(value.getTime()) : "null";
  }
}
