package igloo.bootstrap.js.statement;

import java.io.Serializable;

import org.immutables.value.Value;

import igloo.bootstrap.js.type.JsNumberType;
import igloo.bootstrap.js.util.ImmutableStyle;

@Value.Immutable
@ImmutableStyle
public interface IJsNumber extends IJsStatement<JsNumberType>, Serializable {

	@Value.Parameter
	Number value();

	@Override
	default CharSequence render() {
		Number value = value();
		return value != null ? value.toString() : null;
	}
}
