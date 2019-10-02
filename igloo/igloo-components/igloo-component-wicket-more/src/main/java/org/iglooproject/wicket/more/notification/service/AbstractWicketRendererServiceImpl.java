package org.iglooproject.wicket.more.notification.service;

import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.jpa.security.service.IRunAsSystemService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractWicketRendererServiceImpl extends AbstractOfflinePanelRendererServiceImpl {
	
	@Autowired
	private IRunAsSystemService securityService;

	public AbstractWicketRendererServiceImpl(IWicketContextProvider wicketContextProvider) {
		super(wicketContextProvider);
	}
	
	@Override
	protected String renderComponent(final SerializableSupplier2<Component> componentSupplier, final Locale locale, final String variation) {
		return securityService.runAsSystem(
			() -> AbstractWicketRendererServiceImpl.super.renderComponent(componentSupplier, locale, variation)
		);
	}
	
	@Override
	protected String renderPage(final SerializableSupplier2<? extends Page> pageSupplier, final Locale locale, final String variation) {
		return securityService.runAsSystem(
			() -> AbstractWicketRendererServiceImpl.super.renderPage(pageSupplier, locale, variation)
		);
	}
}
