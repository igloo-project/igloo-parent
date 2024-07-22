package org.iglooproject.wicket.more.util.binding;

import igloo.wicket.model.IBindableDataProviderBinding;
import org.bindgen.java.util.ListBinding;
import org.iglooproject.commons.util.mime.MediaTypeBinding;
import org.iglooproject.jpa.security.business.user.model.GenericUserBinding;

public final class CoreWicketMoreBindings {

  @SuppressWarnings("rawtypes")
  public static final GenericUserBinding GENERIC_USER = new GenericUserBinding<>();

  private static final IBindableDataProviderBinding IBINDABLE_DATA_PROVIDER =
      new IBindableDataProviderBinding();

  private static final ListBinding<?> LIST = new ListBinding<>();

  private static final MediaTypeBinding MEDIA_TYPE = new MediaTypeBinding();

  public static IBindableDataProviderBinding iBindableDataProvider() {
    return IBINDABLE_DATA_PROVIDER;
  }

  public static ListBinding<?> list() {
    return LIST;
  }

  public static MediaTypeBinding mediaType() {
    return MEDIA_TYPE;
  }

  private CoreWicketMoreBindings() {}
}
