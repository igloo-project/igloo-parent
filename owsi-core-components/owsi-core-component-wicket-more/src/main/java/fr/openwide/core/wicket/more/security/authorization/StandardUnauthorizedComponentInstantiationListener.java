package fr.openwide.core.wicket.more.security.authorization;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.authorization.UnauthorizedInstantiationException;

import fr.openwide.core.wicket.more.AbstractCoreSession;
import fr.openwide.core.wicket.more.application.CoreWicketAuthenticatedApplication;

/**
 * Dans le cas d'un accès non identifié non autorisé, on envoie sur la
 * page d'accueil avec affichage d'un message invitant l'utilisateur
 * à s'identifier.
 */
public class StandardUnauthorizedComponentInstantiationListener extends
		AbstractUnauthorizedComponentInstantiationListener {

	@Override
	public void onUnauthorizedInstantiation(Component component) {
		if (component instanceof Page) {
			Page page = (Page) component;
			if (!AuthenticatedWebSession.get().isSignedIn()) {
				page.getSession().error(page.getLocalizer().getString("access.denied", page));
				String currentUrl = getCurrentPageUrl(page);
				if (currentUrl != null) {
					AbstractCoreSession.get().registerRedirectUrl(currentUrl);
				}
				throw new RestartResponseAtInterceptPageException(
						CoreWicketAuthenticatedApplication.get().getSignInPageClass());
			} else {
				throw new UnauthorizedInstantiationException(page.getClass());
			}
		} else {
			onUnauthorizedComponentInstantiation(component);
		}
	}

}
