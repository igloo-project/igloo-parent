package igloo.bootstrap.js.statement;

import java.io.Serializable;

import igloo.bootstrap.js.type.JsAnyType;
import igloo.bootstrap.js.type.JsType;
import igloo.bootstrap.woption.IWOption;
import igloo.bootstrap.woption.IWOptionVisitor;

public interface IJsStatement<T extends JsType> extends IWOption<String>, Serializable {

	String render();

	default void accept(IWOptionVisitor visitor) {
		visitor.visit(this);
	}

	@SuppressWarnings("unchecked")
	default IJsStatement<JsAnyType> anyType() {
		return (IJsStatement<JsAnyType>) this;
	}

	@Override
	default String option() {
		return render();
	}

}
