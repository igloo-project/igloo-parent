package fr.openwide.core.basicapp.web.application.property;

import fr.openwide.core.spring.property.model.AbstractPropertyIds;
import fr.openwide.core.spring.property.model.ImmutablePropertyId;

public final class BasicApplicationWebappPropertyIds extends AbstractPropertyIds {

	/*
	 * Mutable Properties
	 */
	
	// None
	
	/*
	 * Immutable Properties
	 */
	
	public static final ImmutablePropertyId<Integer> PORTFOLIO_ITEMS_PER_PAGE = immutable("portfolio.itemsPerPage");
	public static final ImmutablePropertyId<Integer> PORTFOLIO_ITEMS_PER_PAGE_DESCRIPTION = immutable("portfolio.itemsPerPage.description");
	
	public static final ImmutablePropertyId<String> MAINTENANCE_URL = immutable("maintenance.url");

}
