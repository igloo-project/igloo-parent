package igloo.bootstrap.js.statement;

import java.util.Map;
import java.util.stream.Collectors;

import org.immutables.value.Value;

import igloo.bootstrap.js.type.JsAnyType;
import igloo.bootstrap.js.type.JsMappingType;
import igloo.bootstrap.js.util.ImmutableStyle;
import igloo.bootstrap.js.util.JsVisitor;

@Value.Immutable(builder = true)
@ImmutableStyle
public interface IJsMapping<V extends JsAnyType> extends IJsStatement<JsMappingType<V>> {

	Map<String, IJsStatement<V>> values();

	@Override
	default void accept(JsVisitor visitor) {
		IJsStatement.super.accept(visitor);
		for (IJsStatement<V> statement : values().values()) {
			statement.accept(visitor);
		}
	}

	@Override
	default CharSequence render() {
		Map<String, IJsStatement<V>> values = values();
		return values == null ?
				"null" :
				values.entrySet().stream()
					.<String>map(e -> escape(e.getKey()) + ": " + e.getValue().render())
					.collect(Collectors.joining(", ", "{", "}"));
	}
}
