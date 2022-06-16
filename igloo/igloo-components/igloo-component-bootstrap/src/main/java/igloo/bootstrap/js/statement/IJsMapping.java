package igloo.bootstrap.js.statement;

import java.io.Serializable;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.immutables.value.Value;

import igloo.bootstrap.js.type.JsAnyType;
import igloo.bootstrap.js.type.JsMappingType;
import igloo.bootstrap.js.util.ImmutableStyle;
import igloo.bootstrap.woption.IWOptionVisitor;

@Value.Immutable(builder = true)
@ImmutableStyle
public interface IJsMapping<V extends JsAnyType> extends IJsStatement<JsMappingType<V>>, Serializable {

	@Nullable
	Map<String, IJsStatement<V>> values();

	@Override
	default void accept(IWOptionVisitor visitor) {
		IJsStatement.super.accept(visitor);
		for (IJsStatement<V> statement : values().values()) {
			statement.accept(visitor);
		}
	}

	@Override
	default String render() {
		Map<String, IJsStatement<V>> values = values();
		return values == null ?
				"null" :
				values.entrySet().stream()
					.<String>map(e -> Util.escapeIdentifier(e.getKey()) + ": " + e.getValue().render())
					.collect(Collectors.joining(", ", "{", "}"));
	}
}
