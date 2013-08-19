package fr.openwide.core.basicapp.core.config.application;

import fr.openwide.core.spring.config.CoreConfigurer;

public class BasicApplicationConfigurer extends CoreConfigurer {

	private static final Integer PORTFOLIO_ITEMS_PER_PAGE_DEFAULT_VALUE = 20;

	public int getPortfolioItemsPerPage() {
		return getPropertyAsInteger("portfolio.itemsPerPage", PORTFOLIO_ITEMS_PER_PAGE_DEFAULT_VALUE);
	}
}
