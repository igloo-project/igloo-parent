package fr.openwide.core.wicket.more.security.authorization;

import org.apache.wicket.Component;
import org.apache.wicket.authorization.IUnauthorizedComponentInstantiationListener;
import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;

import fr.openwide.core.wicket.more.AbstractCoreSession;
import fr.openwide.core.wicket.more.application.CoreWicketAuthenticatedApplication;
import fr.openwide.core.wicket.more.request.cycle.RequestCycleUtils;

/**
 * Dans le cas d'un accès non identifié non autorisé, on envoie sur la
 * page d'accueil avec affichage d'un message invitant l'utilisateur
 * à s'identifier.
 */
public class StandardUnauthorizedComponentInstantiationListener implements IUnauthorizedComponentInstantiationListener {

	@Override
	public void onUnauthorizedInstantiation(Component component) {
		if (!AuthenticatedWebSession.exists() || !AuthenticatedWebSession.get().isSignedIn()) {
			AuthenticatedWebSession.get().getFeedbackMessages().clear();
			AuthenticatedWebSession.get().error(component.getString("access.denied"));
			String currentUrl = RequestCycleUtils.getCurrentRequestUrl();
			if (currentUrl != null && !currentUrl.contains("")) {
				AbstractCoreSession.get().registerRedirectUrl(currentUrl);
			}
			throw CoreWicketAuthenticatedApplication.get().getSignInPageLinkDescriptor()
					.newRestartResponseAtInterceptPageException();
		} else {
			throw new UnauthorizedInstantiationException(component.getClass());
		}
	}
	
	protected void onUnauthorizedComponentInstantiation(Component component) {
		throw new UnauthorizedInstantiationException(component.getClass());
	}

}
