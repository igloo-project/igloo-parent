package fr.openwide.core.showcase.core.util.spring;

import fr.openwide.core.spring.config.CoreConfigurer;

public class ShowcaseConfigurer extends CoreConfigurer {
	
	public int getPortfolioItemsPerPage() {
		return getPropertyAsInteger("portfolio.itemsPerPage", 3);
	}
}
