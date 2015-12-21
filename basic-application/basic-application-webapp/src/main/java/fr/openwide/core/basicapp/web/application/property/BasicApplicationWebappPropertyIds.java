package fr.openwide.core.basicapp.web.application.property;

import fr.openwide.core.spring.property.model.ImmutablePropertyId;

public final class BasicApplicationWebappPropertyIds {

	/*
	 * Mutable Properties
	 */
	
	
	
	/*
	 * Immutable Properties
	 */
	
	public static final ImmutablePropertyId<Integer> PORTFOLIO_ITEMS_PER_PAGE = new ImmutablePropertyId<Integer>("portfolio.itemsPerPage");
	public static final ImmutablePropertyId<Integer> PORTFOLIO_ITEMS_PER_PAGE_DESCRIPTION = new ImmutablePropertyId<Integer>("portfolio.itemsPerPage.description");
	
	public static final ImmutablePropertyId<String> MAINTENANCE_URL = new ImmutablePropertyId<>("maintenance.url");

}
