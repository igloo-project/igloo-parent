package igloo.bootstrap.js.statement;

import java.io.Serializable;

import javax.annotation.Nullable;

import org.immutables.value.Value;

import igloo.bootstrap.js.util.ImmutableStyle;

@Value.Immutable
@ImmutableStyle
public interface IJsString extends IJsStatement, Serializable {

	@Value.Parameter
	@Nullable
	String value();

	@Override
	default String render() {
		String value = value();
		return value != null ? Util.escape(value) : "null";
	}
}
