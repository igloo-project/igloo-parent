package fr.openwide.core.basicapp.web.application.navigation.util;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.model.UserGroup;

/**
 * Utilitaire de gestion des liens au sein de l'application.
 * 
 * De manière générale, on centralise ici :
 * - tous les noms de paramètres qui vont être utilisés
 * - toutes les méthodes permettant de construire des liens qui ont un sens métier et sont réutilisables dans l'application
 */
public final class LinkUtils {

	public static final String ID_PARAMETER = "id";

	public static PageParameters getUserPageParameters(User user) {
		PageParameters parameters = new PageParameters();
		parameters.add(ID_PARAMETER, user.getId());
		return parameters;
	}

	public static PageParameters getUserGroupPageParameters(UserGroup userGroup) {
		PageParameters parameters = new PageParameters();
		parameters.add(ID_PARAMETER, userGroup.getId());
		return parameters;
	}
	
	private LinkUtils() {
	}
}
