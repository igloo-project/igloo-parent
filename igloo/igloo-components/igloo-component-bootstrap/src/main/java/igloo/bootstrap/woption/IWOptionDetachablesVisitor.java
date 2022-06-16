package igloo.bootstrap.woption;

import java.util.List;

import org.apache.wicket.model.IDetachable;

import com.google.common.collect.Lists;

public class IWOptionDetachablesVisitor implements IWOptionVisitor {

	private final List<IDetachable> detachables = Lists.newArrayList();

	@Override
	public void visit(IWOption<?> option) {
		if (option instanceof IWOptionDetachable) {
			detachables.addAll(((IWOptionDetachable) option).getDetachables());
		}
	}

	public void visitAndDetach(IWOption<?> option) {
		visit(option);
		detachables.forEach(IDetachable::detach);
	}

}
