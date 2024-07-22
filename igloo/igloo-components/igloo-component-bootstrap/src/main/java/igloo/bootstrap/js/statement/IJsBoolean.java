package igloo.bootstrap.js.statement;

import igloo.bootstrap.js.util.ImmutableStyle;
import java.io.Serializable;
import javax.annotation.Nullable;
import org.immutables.value.Value;

@Value.Immutable
@ImmutableStyle
public interface IJsBoolean extends IJsStatement, Serializable {

  @Value.Parameter
  @Nullable
  Boolean value();

  @Override
  default String render() {
    Boolean value = value();
    return value != null ? value.toString() : "null";
  }
}
