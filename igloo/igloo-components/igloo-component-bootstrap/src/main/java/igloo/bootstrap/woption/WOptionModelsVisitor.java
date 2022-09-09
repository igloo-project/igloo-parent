package igloo.bootstrap.woption;

import java.util.List;

import org.apache.wicket.Component;

import com.google.common.collect.Lists;

public class WOptionModelsVisitor implements IWVisitor {

	private final List<IWOptionModel> optionModels = Lists.newArrayList();

	@Override
	public void visit(IWVisitable option) {
		option.accept(this);
		if (option instanceof IWOptionModel) {
			optionModels.add((IWOptionModel) option);
		}
	}

	public void visitAndBind(Component component, IWVisitable option) {
		visit(option);
		optionModels.forEach(i -> i.wrapModels(component));
	}

}
