package igloo.bootstrap.js.statement;

import igloo.bootstrap.js.type.JsAnyType;
import igloo.bootstrap.js.type.JsType;
import igloo.bootstrap.js.util.JsVisitor;

public interface IJsStatement<T extends JsType> {

	CharSequence render();

	default void accept(JsVisitor visitor) {
		visitor.visit(this);
	}

	default String escape(String value) {
		return "\"" + value.replace("\"", "\\\"") +  "\"";
	}

	@SuppressWarnings("unchecked")
	default IJsStatement<JsAnyType> anyType() {
		return (IJsStatement<JsAnyType>) this;
	}
}
