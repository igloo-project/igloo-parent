package fr.openwide.core.basicapp.web.application.util.binding;

import fr.openwide.core.wicket.more.model.IBindableDataProviderBinding;


public final class WebappBindings {
	
	private static final IBindableDataProviderBinding IBINDABLE_DATA_PROVIDER = new IBindableDataProviderBinding();
	
	public static IBindableDataProviderBinding iBindableDataProvider() {
		return IBINDABLE_DATA_PROVIDER;
	}
	
	private WebappBindings() {
	}
	
}