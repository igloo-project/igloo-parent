package igloo.bootstrap.js.statement;

import igloo.bootstrap.js.util.ImmutableStyle;
import java.io.Serializable;
import org.immutables.value.Value;

/** JsStatement to distinct variable from string js value */
@Value.Immutable
@ImmutableStyle
public interface IJsVariable extends IJsStatement, Serializable {

  @Value.Parameter
  String value();

  @Override
  default String render() {
    return value();
  }
}
