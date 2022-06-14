package igloo.bootstrap.js.statement;

import org.immutables.value.Value;

import igloo.bootstrap.js.type.JsAnyType;
import igloo.bootstrap.js.util.ImmutableStyle;

@Value.Immutable
@ImmutableStyle
public interface IJsLiteral<V extends JsAnyType> extends IJsStatement<V> {

	@Value.Parameter
	String value();

	@Override
	default CharSequence render() {
		return value();
	}
}
