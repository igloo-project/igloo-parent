package igloo.bootstrap.js.statement;

import igloo.bootstrap.js.util.ImmutableStyle;
import jakarta.annotation.Nullable;
import java.io.Serializable;
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
