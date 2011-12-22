package fr.openwide.core.showcase.core.util.spring;

import fr.openwide.core.spring.config.CoreConfigurer;

public class ShowcaseConfigurer extends CoreConfigurer {

	private static final String PORTFOLIO_ITEMS_PER_PAGE = "portfolio.itemsPerPage";
	private static final int PORTFOLIO_ITEMS_PER_PAGE_DEFAULT_VALUE = 2;
	
	public int getPortfolioItemsPerPage() {
		return getPropertyAsInteger(PORTFOLIO_ITEMS_PER_PAGE, PORTFOLIO_ITEMS_PER_PAGE_DEFAULT_VALUE);
	}
}
