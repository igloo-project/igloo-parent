package fr.openwide.core.basicapp.core.config.application;

import java.util.List;

import fr.openwide.core.basicapp.core.config.util.Environment;
import fr.openwide.core.spring.config.CoreConfigurer;

public class BasicApplicationConfigurer extends CoreConfigurer {

	private static final Integer PORTFOLIO_ITEMS_PER_PAGE_DEFAULT_VALUE = 20;

	public int getPortfolioItemsPerPage() {
		return getPropertyAsInteger("portfolio.itemsPerPage", PORTFOLIO_ITEMS_PER_PAGE_DEFAULT_VALUE);
	}
	
	public int getPortfolioItemsPerPageDescription() {
		return getPropertyAsInteger("portfolio.itemsPerPage.description", PORTFOLIO_ITEMS_PER_PAGE_DEFAULT_VALUE);
	}

	public Environment getEnvironment() {
		return getPropertyAsEnum("environment", Environment.class, Environment.production);
	}

	public String getMaintenanceUrl() {
		return getPropertyAsString("maintenance.url");
	}

	public boolean isSecurityPasswordValidatorEnabled() {
		return getPropertyAsBoolean("security.password.validator.enabled");
	}

	public List<String> getSecurityPasswordUserForbiddenPasswords() {
		return getPropertyAsStringList("security.password.user.forbiddenPasswords");
	}

}
