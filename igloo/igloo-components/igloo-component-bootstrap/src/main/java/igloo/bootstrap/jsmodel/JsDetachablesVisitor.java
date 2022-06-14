package igloo.bootstrap.jsmodel;

import java.util.List;

import org.apache.wicket.model.IDetachable;

import com.google.common.collect.Lists;

import igloo.bootstrap.js.statement.IJsStatement;
import igloo.bootstrap.js.util.JsVisitor;

public class JsDetachablesVisitor implements JsVisitor {

	private List<IDetachable> detachables = Lists.newArrayList();

	@Override
	public void visit(IJsStatement<?> statement) {
		if (statement instanceof JsDetachable) {
			detachables.addAll(((JsDetachable) statement).getDetachables());
		}
	}

}
