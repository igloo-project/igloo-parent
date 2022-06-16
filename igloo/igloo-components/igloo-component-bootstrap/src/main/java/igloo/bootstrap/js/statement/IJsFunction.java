package igloo.bootstrap.js.statement;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.immutables.value.Value;
import org.springframework.lang.Nullable;

import igloo.bootstrap.js.type.JsAnyType;
import igloo.bootstrap.js.util.ImmutableStyle;
import igloo.bootstrap.woption.IWOptionVisitor;

@Value.Immutable(builder = true)
@ImmutableStyle
public interface IJsFunction<V extends JsAnyType> extends IJsStatement<V>, Serializable {

	@Value.Parameter
	String functionName();

	@Nullable
	List<IJsStatement<JsAnyType>> arguments();

	@Override
	default void accept(IWOptionVisitor visitor) {
		IJsStatement.super.accept(visitor);
		for (IJsStatement<JsAnyType> statement : arguments()) {
			statement.accept(visitor);
		}
	}

	@Override
	default String render() {
		return functionName() +
				"(" +
				Optional.ofNullable(arguments())
					.map(args -> args.stream().map(IJsStatement::render).collect(Collectors.joining(", ")))
					.orElse("")
				+ ")";
	}
}
