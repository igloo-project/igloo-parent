package igloo.bootstrap.js.statement;

import java.io.Serializable;

import org.immutables.value.Value;

import igloo.bootstrap.js.util.ImmutableStyle;

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
