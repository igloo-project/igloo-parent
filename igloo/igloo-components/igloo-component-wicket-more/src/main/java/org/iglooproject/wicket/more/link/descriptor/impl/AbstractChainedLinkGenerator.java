package org.iglooproject.wicket.more.link.descriptor.impl;

import org.apache.wicket.Component;
import org.apache.wicket.request.cycle.RequestCycle;
import org.iglooproject.wicket.model.Detachables;
import org.iglooproject.wicket.more.link.descriptor.AbstractDynamicBookmarkableLink;
import org.iglooproject.wicket.more.link.descriptor.LinkInvalidTargetRuntimeException;
import org.iglooproject.wicket.more.link.descriptor.generator.ILinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.parameter.injector.LinkParameterInjectionRuntimeException;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationRuntimeException;

abstract class AbstractChainedLinkGenerator<L extends ILinkGenerator> implements ILinkGenerator {
	
	private static final long serialVersionUID = 4316276456091245259L;
	
	private final Iterable<? extends L> chain;
	
	public AbstractChainedLinkGenerator(Iterable<? extends L> chain) {
		super();
		this.chain = chain;
	}
	
	protected final Iterable<? extends L> getChain() {
		return chain;
	}

	@Override
	public abstract ILinkGenerator chain(ILinkGenerator other);
	
	@Override
	public abstract ILinkGenerator wrap(Component component);

	protected L delegate() {
		L first = null;
		for (L generator : getChain()) {
			if (first == null) {
				first = generator;
			}
			if (generator.isAccessible()) {
				return generator;
			}
		}
		if (first == null) {
			throw new IllegalStateException("Empty delegate chain");
		}
		return first;
	}

	@Override
	public void detach() {
		Detachables.detach(getChain());
	}

	@Override
	public AbstractDynamicBookmarkableLink link(String wicketId) {
		return delegate().link(wicketId);
	}

	@Override
	public String url() throws LinkInvalidTargetRuntimeException, LinkParameterInjectionRuntimeException,
			LinkParameterValidationRuntimeException {
		return delegate().url();
	}

	@Override
	public String url(RequestCycle requestCycle) throws LinkInvalidTargetRuntimeException,
			LinkParameterInjectionRuntimeException, LinkParameterValidationRuntimeException {
		return delegate().url(requestCycle);
	}

	@Override
	public String fullUrl() throws LinkInvalidTargetRuntimeException, LinkParameterInjectionRuntimeException,
			LinkParameterValidationRuntimeException {
		return delegate().fullUrl();
	}

	@Override
	public String fullUrl(RequestCycle requestCycle) throws LinkInvalidTargetRuntimeException,
			LinkParameterInjectionRuntimeException, LinkParameterValidationRuntimeException {
		return delegate().fullUrl(requestCycle);
	}

	@Override
	public boolean isAccessible() {
		return delegate().isAccessible();
	}

}
