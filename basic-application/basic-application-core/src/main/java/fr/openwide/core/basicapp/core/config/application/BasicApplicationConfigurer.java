package fr.openwide.core.basicapp.core.config.application;

import fr.openwide.core.spring.config.CoreConfigurer;

public class BasicApplicationConfigurer extends CoreConfigurer {

	private static final String PORTFOLIO_ITEMS_PER_PAGE = "portfolio.itemsPerPage";

	public int getPortfolioItemsPerPage() {
		return getPropertyAsInteger(PORTFOLIO_ITEMS_PER_PAGE, 20);
	}
}
