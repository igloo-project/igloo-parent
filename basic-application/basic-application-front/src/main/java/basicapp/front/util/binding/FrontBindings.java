package basicapp.front.util.binding;

import igloo.wicket.model.IBindableDataProviderBinding;

public final class FrontBindings {

  private static final IBindableDataProviderBinding IBINDABLE_DATA_PROVIDER =
      new IBindableDataProviderBinding();

  public static IBindableDataProviderBinding iBindableDataProvider() {
    return IBINDABLE_DATA_PROVIDER;
  }

  private FrontBindings() {}
}
