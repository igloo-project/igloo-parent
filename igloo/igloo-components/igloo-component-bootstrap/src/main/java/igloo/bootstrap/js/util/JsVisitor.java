package igloo.bootstrap.js.util;

import igloo.bootstrap.js.statement.IJsStatement;

public interface JsVisitor {

	void visit(IJsStatement<?> statement);

}
