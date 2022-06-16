package igloo.bootstrap.jsmodel;

import java.util.List;

import org.apache.wicket.Component;

import com.google.common.collect.Lists;

import igloo.bootstrap.js.statement.IJsStatement;
import igloo.bootstrap.js.util.JsVisitor;

public class JsModelVisitor implements JsVisitor {

	private final List<IJsModel> jsModels = Lists.newArrayList();

	@Override
	public void visit(IJsStatement<?> statement) {
		if (statement instanceof IJsModel) {
			jsModels.add((IJsModel) statement);
		}
	}

	public void visitAndBind(Component component, IJsStatement<?> statement) {
		visit(statement);
		jsModels.forEach(i -> i.wrapModels(component));
	}

}
