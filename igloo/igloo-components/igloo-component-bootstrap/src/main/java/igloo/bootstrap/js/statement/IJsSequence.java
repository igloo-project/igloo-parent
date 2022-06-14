package igloo.bootstrap.js.statement;

import java.util.List;
import java.util.stream.Collectors;

import org.immutables.value.Value;

import igloo.bootstrap.js.type.JsAnyType;
import igloo.bootstrap.js.type.JsSequenceType;
import igloo.bootstrap.js.util.ImmutableStyle;
import igloo.bootstrap.js.util.JsVisitor;

@Value.Immutable(builder = true)
@ImmutableStyle
public interface IJsSequence<V extends JsAnyType> extends IJsStatement<JsSequenceType<V>> {

	List<IJsStatement<V>> values();

	@Override
	default void accept(JsVisitor visitor) {
		IJsStatement.super.accept(visitor);
		for (IJsStatement<V> statement : values()) {
			statement.accept(visitor);
		}
	}

	@Override
	default CharSequence render() {
		List<IJsStatement<V>> values = values();
		return values == null ?
				"null" :
				values.stream()
					.map(IJsStatement::render)
					.collect(Collectors.joining(", ", "[", "]"));
	}

}
