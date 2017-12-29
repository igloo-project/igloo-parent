package org.iglooproject.wicket.more.security.page;

import org.apache.wicket.Page;

import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.wicket.more.AbstractCoreSession;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.CoreWebPage;
import org.iglooproject.wicket.more.request.cycle.RequestCycleUtils;

public class LoginSuccessPage extends CoreWebPage {
	
	private static final long serialVersionUID = -875304387617628398L;
	
	public static final String WICKET_BEHAVIOR_LISTENER_URL_FRAGMENT = "IBehaviorListener";
	
	public static final IPageLinkDescriptor linkDescriptor() {
		return LinkDescriptorBuilder.start().page(LoginSuccessPage.class);
	}
	
	public LoginSuccessPage() {
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		redirectToSavedPage();
	}
	
	protected void redirectToSavedPage() {
		AbstractCoreSession<?> session = AbstractCoreSession.get();

		@SuppressWarnings("deprecation")
		IPageLinkDescriptor pageLinkDescriptor = session.getRedirectPageLinkDescriptor();
		if (pageLinkDescriptor != null) {
			throw pageLinkDescriptor.newRestartResponseException();
		}
		
		@SuppressWarnings("deprecation")
		String redirectUrl = session.consumeRedirectUrl();
		if (!StringUtils.hasText(redirectUrl)) {
			redirectUrl = RequestCycleUtils.getSpringSecuritySavedRequest();
		}
		if (isUrlValid(redirectUrl)) {
			redirect(redirectUrl);
		} else {
			redirect(getDefaultRedirectPage());
		}
	}
	
	protected Class<? extends Page> getDefaultRedirectPage() {
		return getApplication().getHomePage();
	}
	
	protected boolean isUrlValid(String url) {
		return StringUtils.hasText(url);
		//  && !StringUtils.contains(url, WICKET_BEHAVIOR_LISTENER_URL_FRAGMENT) : Wicket a l'air de s'en sortir et on a vite des problèmes sur Sitra si on vire ces éléments
	}

}