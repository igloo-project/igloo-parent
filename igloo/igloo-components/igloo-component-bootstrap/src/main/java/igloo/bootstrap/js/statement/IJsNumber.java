package igloo.bootstrap.js.statement;

import igloo.bootstrap.js.util.ImmutableStyle;
import jakarta.annotation.Nullable;
import java.io.Serializable;
import org.immutables.value.Value;

@Value.Immutable
@ImmutableStyle
public interface IJsNumber extends IJsStatement, Serializable {

  @Value.Parameter
  @Nullable
  Number value();

  @Override
  default String render() {
    Number value = value();
    return value != null ? value.toString() : "null";
  }
}
