package igloo.bootstrap.woption;

import java.util.List;

import org.apache.wicket.model.IDetachable;

import com.google.common.collect.Lists;

public class WDetachablesVisitor implements IWVisitor {

	private final List<IDetachable> detachables = Lists.newArrayList();

	@Override
	public void visit(IWVisitable visitable) {
		WVisitables.accept(this, visitable);
		if (visitable instanceof IWOptionDetachable) {
			detachables.addAll(((IWOptionDetachable) visitable).getDetachables());
		}
	}

	public void visitAndDetach(IWVisitable visitable) {
		visit(visitable);
		detachables.forEach(IDetachable::detach);
	}

}
