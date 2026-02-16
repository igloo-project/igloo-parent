package igloo.bootstrap.js.statement;

import igloo.bootstrap.js.util.ImmutableStyle;
import igloo.bootstrap.woption.IWVisitor;
import jakarta.annotation.Nullable;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import org.immutables.value.Value;

@Value.Immutable(builder = true)
@ImmutableStyle
public interface IJsSequence extends IJsStatement, Serializable {

  @Nullable
  List<IJsStatement> values();

  @Override
  default void accept(IWVisitor visitor) {
    for (IJsStatement statement : values()) {
      statement.accept(visitor);
    }
  }

  @Override
  default String render() {
    List<IJsStatement> values = values();
    return values == null
        ? "null"
        : values.stream().map(IJsStatement::render).collect(Collectors.joining(", ", "[", "]"));
  }
}
