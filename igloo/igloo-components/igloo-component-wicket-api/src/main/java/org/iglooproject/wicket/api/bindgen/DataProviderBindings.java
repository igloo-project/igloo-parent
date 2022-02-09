package org.iglooproject.wicket.api.bindgen;

import org.iglooproject.wicket.api.provider.IBindableDataProviderBinding;

public final class DataProviderBindings {

	private static final IBindableDataProviderBinding IBINDABLE_DATA_PROVIDER = new IBindableDataProviderBinding();

	public static IBindableDataProviderBinding iBindableDataProvider() {
		return IBINDABLE_DATA_PROVIDER;
	}

	private DataProviderBindings() {
	}

}
