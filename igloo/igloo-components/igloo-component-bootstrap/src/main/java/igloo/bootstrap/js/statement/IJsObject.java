package igloo.bootstrap.js.statement;

import igloo.bootstrap.woption.IWVisitor;
import java.io.Serializable;
import java.util.Map;
import java.util.stream.Collectors;

public interface IJsObject extends IJsStatement, Serializable {

  Map<String, IJsStatement> values();

  @Override
  default void accept(IWVisitor visitor) {
    for (IJsStatement statement : values().values()) {
      statement.accept(visitor);
    }
  }

  @Override
  default String render() {
    Map<String, IJsStatement> values = values();
    return values == null
        ? "null"
        : values.entrySet().stream()
            .<String>map(e -> Util.escapeIdentifier(e.getKey()) + ": " + e.getValue().render())
            .collect(Collectors.joining(", ", "{", "}"));
  }
}
