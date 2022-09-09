package igloo.bootstrap.js.statement;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.immutables.value.Value;

import igloo.bootstrap.js.type.JsAnyType;
import igloo.bootstrap.js.type.JsSequenceType;
import igloo.bootstrap.js.util.ImmutableStyle;
import igloo.bootstrap.woption.IWVisitor;

@Value.Immutable(builder = true)
@ImmutableStyle
public interface IJsSequence<V extends JsAnyType> extends IJsStatement<JsSequenceType<V>>, Serializable {

	@Nullable
	List<IJsStatement<V>> values();

	@Override
	default void accept(IWVisitor visitor) {
		for (IJsStatement<V> statement : values()) {
			statement.accept(visitor);
		}
	}

	@Override
	default String render() {
		List<IJsStatement<V>> values = values();
		return values == null ?
				"null" :
				values.stream()
					.map(IJsStatement::render)
					.collect(Collectors.joining(", ", "[", "]"));
	}

}
