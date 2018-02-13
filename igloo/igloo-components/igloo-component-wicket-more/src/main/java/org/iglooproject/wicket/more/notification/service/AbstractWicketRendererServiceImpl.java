package org.iglooproject.wicket.more.notification.service;

import java.util.Locale;
import java.util.concurrent.Callable;

import org.apache.wicket.Component;
import org.iglooproject.jpa.security.service.ISecurityService;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Supplier;

public abstract class AbstractWicketRendererServiceImpl extends AbstractOfflinePanelRendererServiceImpl {
	
	@Autowired
	private ISecurityService securityService;

	public AbstractWicketRendererServiceImpl(IWicketContextProvider wicketContextProvider) {
		super(wicketContextProvider);
	}
	
	@Override
	protected String renderComponent(final Supplier<Component> componentSupplier, final Locale locale, final String variation) {
		return securityService.runAsSystem(
			new Callable<String>() {
				@Override
				public String call() throws Exception {
					return AbstractWicketRendererServiceImpl.super.renderComponent(componentSupplier, locale, variation);
				}
			}
		);
	}
}
