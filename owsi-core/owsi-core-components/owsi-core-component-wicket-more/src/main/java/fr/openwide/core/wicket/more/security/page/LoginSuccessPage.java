package fr.openwide.core.wicket.more.security.page;

import org.springframework.security.web.savedrequest.SavedRequest;

import fr.openwide.core.spring.util.StringUtils;
import fr.openwide.core.wicket.more.AbstractCoreSession;
import fr.openwide.core.wicket.more.markup.html.CoreWebPage;
import fr.openwide.core.wicket.more.request.cycle.RequestCycleUtils;

public class LoginSuccessPage extends CoreWebPage {
	
	private static final long serialVersionUID = -875304387617628398L;
	
	private static final String SPRING_SECURITY_SAVED_REQUEST = "SPRING_SECURITY_SAVED_REQUEST";
	
	private static final String WICKET_BEHAVIOR_LISTENER_URL_FRAGMENT = "IBehaviorListener";
	
	public LoginSuccessPage() {
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		redirectToSavedPage();
	}
	
	protected void redirectToSavedPage() {
		AbstractCoreSession<?> session = AbstractCoreSession.get();
		
		String redirectUrl = null;
		if (StringUtils.hasText(session.getRedirectUrl())) {
			redirectUrl = session.getRedirectUrl();
		} else {
			Object savedRequest = RequestCycleUtils.getCurrentContainerRequest().getSession().getAttribute(SPRING_SECURITY_SAVED_REQUEST);
			if (savedRequest instanceof SavedRequest) {
				redirectUrl = ((SavedRequest) savedRequest).getRedirectUrl();
			}
		}
		if (isUrlValid(redirectUrl)) {
			redirect(redirectUrl);
		} else {
			redirect(this.getApplication().getHomePage());
		}
	}
	
	protected boolean isUrlValid(String url) {
		return StringUtils.hasText(url) && !StringUtils.contains(url, WICKET_BEHAVIOR_LISTENER_URL_FRAGMENT);
	}

}