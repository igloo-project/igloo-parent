package igloo.wicket.model;

public final class DataProviderBindings {

	private static final IBindableDataProviderBinding IBINDABLE_DATA_PROVIDER = new IBindableDataProviderBinding();

	public static IBindableDataProviderBinding iBindableDataProvider() {
		return IBINDABLE_DATA_PROVIDER;
	}

	private DataProviderBindings() {
	}

}
