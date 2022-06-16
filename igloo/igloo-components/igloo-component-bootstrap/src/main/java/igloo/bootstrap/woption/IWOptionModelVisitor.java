package igloo.bootstrap.woption;

import java.util.List;

import org.apache.wicket.Component;

import com.google.common.collect.Lists;

public class IWOptionModelVisitor implements IWOptionVisitor {

	private final List<IWOptionModel> optionModels = Lists.newArrayList();

	@Override
	public void visit(IWOption option) {
		if (option instanceof IWOptionModel) {
			optionModels.add((IWOptionModel) option);
		}
	}

	public void visitAndBind(Component component, IWOption option) {
		visit(option);
		optionModels.forEach(i -> i.wrapModels(component));
	}

}
