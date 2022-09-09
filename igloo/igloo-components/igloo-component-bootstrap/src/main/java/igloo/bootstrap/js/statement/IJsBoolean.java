package igloo.bootstrap.js.statement;

import java.io.Serializable;

import javax.annotation.Nullable;

import org.immutables.value.Value;

import igloo.bootstrap.js.util.ImmutableStyle;

@Value.Immutable
@ImmutableStyle
public interface IJsBoolean extends IJsStatement, Serializable {

	@Value.Parameter
	@Nullable
	Boolean value();

	@Override
	default String render() {
		Boolean value = value();
		return value != null ? value.toString() : "null";
	}
}
