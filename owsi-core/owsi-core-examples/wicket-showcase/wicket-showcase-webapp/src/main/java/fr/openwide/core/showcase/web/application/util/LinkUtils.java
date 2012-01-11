package fr.openwide.core.showcase.web.application.util;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.showcase.core.business.user.model.User;

public final class LinkUtils {

	public static final String ITEM_ID_PARAMETER = "id";

	public static PageParameters getUserDescriptionPageParameters(User user) {
		PageParameters parameters = new PageParameters();
		
		if (user != null) {
			parameters.add(ITEM_ID_PARAMETER, user.getId());
		}
		return parameters;
	}
}
