package igloo.bootstrap.js.statement;

import java.io.Serializable;

import org.immutables.value.Value;

import igloo.bootstrap.js.type.JsBooleanType;
import igloo.bootstrap.js.util.ImmutableStyle;

@Value.Immutable
@ImmutableStyle
public interface IJsBoolean extends IJsStatement<JsBooleanType>, Serializable {

	@Value.Parameter
	Boolean value();

	@Override
	default CharSequence render() {
		Boolean value = value();
		return value != null ? value.toString() : null;
	}
}
