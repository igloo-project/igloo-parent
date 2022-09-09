package igloo.bootstrap.woption;

import java.util.List;

import org.apache.wicket.Component;

import com.google.common.collect.Lists;

public class WOptionModelsVisitor implements IWVisitor {

	private final List<IWOptionModel> optionModels = Lists.newArrayList();

	@Override
	public void visit(IWVisitable visitable) {
		visitable.accept(this);
		if (visitable instanceof IWOptionModel) {
			optionModels.add((IWOptionModel) visitable);
		}
	}

	public void visitAndBind(Component component, IWVisitable visitable) {
		visit(visitable);
		optionModels.forEach(i -> i.wrapModels(component));
	}

}
