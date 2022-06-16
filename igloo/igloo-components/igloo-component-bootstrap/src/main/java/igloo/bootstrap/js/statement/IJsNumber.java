package igloo.bootstrap.js.statement;

import java.io.Serializable;

import javax.annotation.Nullable;

import org.immutables.value.Value;

import igloo.bootstrap.js.type.JsNumberType;
import igloo.bootstrap.js.util.ImmutableStyle;

@Value.Immutable
@ImmutableStyle
public interface IJsNumber extends IJsStatement<JsNumberType>, Serializable {

	@Value.Parameter
	@Nullable
	Number value();

	@Override
	default String render() {
		Number value = value();
		return value != null ? value.toString() : "null";
	}
}
