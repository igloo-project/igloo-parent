package fr.openwide.core.basicapp.web.application.administration.component;

import org.apache.wicket.markup.repeater.data.IDataProvider;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.wicket.more.markup.html.list.PageablePortfolioPanel;

public abstract class AbstractUserPortfolioPanel<U extends User> extends PageablePortfolioPanel<U> {

	private static final long serialVersionUID = 3233717910646663737L;

	public AbstractUserPortfolioPanel(String wicketId, IDataProvider<U> dataProvider, int itemsPerPage,
			String countRessourceKey) {
		super(wicketId, dataProvider, itemsPerPage, countRessourceKey);
	}

}
