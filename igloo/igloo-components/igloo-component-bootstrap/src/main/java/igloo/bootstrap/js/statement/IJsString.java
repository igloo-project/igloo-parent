package igloo.bootstrap.js.statement;

import igloo.bootstrap.js.util.ImmutableStyle;
import jakarta.annotation.Nullable;
import java.io.Serializable;
import org.immutables.value.Value;

@Value.Immutable
@ImmutableStyle
public interface IJsString extends IJsStatement, Serializable {

  @Value.Parameter
  @Nullable
  String value();

  @Override
  default String render() {
    String value = value();
    return value != null ? Util.escape(value) : "null";
  }
}
