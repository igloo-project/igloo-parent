package fr.openwide.core.wicket.more.security.page;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.wicket.more.AbstractCoreSession;
import fr.openwide.core.wicket.more.CoreDefaultExceptionMapper;
import fr.openwide.core.wicket.more.application.CoreWicketAuthenticatedApplication;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.markup.html.CoreWebPage;

/**
 * This page is used when Spring Security catches an AccessDeniedException, either because Spring Security
 * detected an unauthorized access, or because Wicket did and the {@link CoreDefaultExceptionMapper} threw
 * an AccessDeniedException.
 * <p>Due to how Spring Security is built, this generally can only happen for someone who already is authenticated,
 * but lacks the necessary authorizations.
 */
public class AccessDeniedPage extends CoreWebPage {

	private static final long serialVersionUID = 4583415457223655426L;
	
	private final IPageLinkDescriptor redirectLinkDescriptor;

	public AccessDeniedPage() {
		this(CoreWicketAuthenticatedApplication.get().getHomePageLinkDescriptor());
	}
	
	protected AccessDeniedPage(IPageLinkDescriptor redirectLinkDescriptor) {
		super(new PageParameters());
		this.redirectLinkDescriptor = redirectLinkDescriptor;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		AbstractCoreSession.get().getFeedbackMessages().clear();
		AbstractCoreSession.get().error(getString("access.denied"));
		
		throw redirectLinkDescriptor.newRestartResponseException();
	}
}
