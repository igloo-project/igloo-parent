package igloo.bootstrap.js.statement;

import java.io.Serializable;

import igloo.bootstrap.js.type.JsAnyType;
import igloo.bootstrap.js.type.JsType;
import igloo.bootstrap.woption.IWVisitable;

public interface IJsStatement<T extends JsType> extends IWVisitable, Serializable {

	String render();

	@SuppressWarnings("unchecked")
	default IJsStatement<JsAnyType> anyType() {
		return (IJsStatement<JsAnyType>) this;
	}

}
