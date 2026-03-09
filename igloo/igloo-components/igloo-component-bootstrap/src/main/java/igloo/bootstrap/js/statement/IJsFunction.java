package igloo.bootstrap.js.statement;

import igloo.bootstrap.js.util.ImmutableStyle;
import igloo.bootstrap.woption.IWVisitor;
import jakarta.annotation.Nullable;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.immutables.value.Value;

@Value.Immutable(builder = true)
@ImmutableStyle
public interface IJsFunction extends IJsStatement, Serializable {

  @Value.Parameter
  String functionName();

  @Nullable
  List<IJsStatement> arguments();

  @Override
  default void accept(IWVisitor visitor) {
    for (IJsStatement statement : arguments()) {
      statement.accept(visitor);
    }
  }

  @Override
  default String render() {
    return functionName()
        + "("
        + Optional.ofNullable(arguments())
            .map(args -> args.stream().map(IJsStatement::render).collect(Collectors.joining(", ")))
            .orElse("")
        + ")";
  }
}
