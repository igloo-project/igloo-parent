package fr.openwide.core.showcase.core.util.spring;

import fr.openwide.core.spring.config.CoreConfigurer;

public class ShowcaseConfigurer extends CoreConfigurer {
	
	public int getPortfolioItemsPerPage() {
		return getPropertyAsInteger("portfolio.itemsPerPage", 15);
	}
	
	public int getPortfolioItemsPerPageUser() {
		return getPropertyAsInteger("portfolio.itemsPerPage.user", 5);
	}
	
	public String getShowcaseFileFileStoreRootDirectory() {
		return getPropertyAsString("showcaseFile.rootDirectory");
	}
}
