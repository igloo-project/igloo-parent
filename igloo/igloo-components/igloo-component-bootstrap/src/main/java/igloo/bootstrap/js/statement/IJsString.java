package igloo.bootstrap.js.statement;

import org.immutables.value.Value;

import igloo.bootstrap.js.type.JsStringType;
import igloo.bootstrap.js.util.ImmutableStyle;

@Value.Immutable
@ImmutableStyle
public interface IJsString extends IJsStatement<JsStringType> {

	@Value.Parameter
	String value();

	@Override
	default CharSequence render() {
		String value = value();
		return value != null ? escape(value) : null;
	}
}
