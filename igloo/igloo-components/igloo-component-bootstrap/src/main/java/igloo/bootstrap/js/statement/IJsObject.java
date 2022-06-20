package igloo.bootstrap.js.statement;

import java.io.Serializable;
import java.util.Map;
import java.util.stream.Collectors;

import igloo.bootstrap.js.type.JsObjectType;
import igloo.bootstrap.woption.IWOptionVisitor;

public interface IJsObject extends IJsStatement<JsObjectType>, Serializable {

	Map<String, IJsStatement<?>> values();

	@Override
	default void accept(IWOptionVisitor visitor) {
		IJsStatement.super.accept(visitor);
		for (IJsStatement<?> statement : values().values()) {
			statement.accept(visitor);
		}
	}

	@Override
	default String render() {
		Map<String, IJsStatement<?>> values = values();
		return values == null ?
				"null" :
				values.entrySet().stream()
					.<String>map(e -> Util.escapeIdentifier(e.getKey()) + ": " + e.getValue().render())
					.collect(Collectors.joining(", ", "{", "}"));
	}
}
