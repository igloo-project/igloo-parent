package igloo.bootstrap.js.statement;

import igloo.bootstrap.js.util.ImmutableStyle;
import java.io.Serializable;
import org.immutables.value.Value;

@Value.Immutable
@ImmutableStyle
public interface IJsLiteral extends IJsStatement, Serializable {

  @Value.Parameter
  String value();

  @Override
  default String render() {
    return value();
  }
}
