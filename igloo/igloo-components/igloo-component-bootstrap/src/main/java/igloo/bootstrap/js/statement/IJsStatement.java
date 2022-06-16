package igloo.bootstrap.js.statement;

import java.io.Serializable;

import igloo.bootstrap.js.type.JsAnyType;
import igloo.bootstrap.js.type.JsType;
import igloo.bootstrap.js.util.JsVisitor;

public interface IJsStatement<T extends JsType> extends Serializable {

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
