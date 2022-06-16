package igloo.bootstrap.jsmodel;

import java.util.List;

import org.apache.wicket.model.IDetachable;

import com.google.common.collect.Lists;

import igloo.bootstrap.js.statement.IJsStatement;
import igloo.bootstrap.js.util.JsVisitor;

public class JsDetachablesVisitor implements JsVisitor {

	private final List<IDetachable> detachables = Lists.newArrayList();

	@Override
	public void visit(IJsStatement<?> statement) {
		if (statement instanceof IJsDetachable) {
			detachables.addAll(((IJsDetachable) statement).getDetachables());
		}
	}

	public void visitAndDetach(IJsStatement<?> statement) {
		visit(statement);
		detachables.forEach(IDetachable::detach);
	}

}
