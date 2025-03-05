package org.iglooproject.wicket.more.util.binding;

import igloo.wicket.model.IBindableDataProviderBinding;
import org.iglooproject.commons.util.mime.MediaTypeBinding;
import org.iglooproject.jpa.security.business.user.model.IUserBinding;

public final class CoreWicketMoreBindings {

  public static final IUserBinding USER = new IUserBinding();

  private static final IBindableDataProviderBinding BINDABLE_DATA_PROVIDER =
      new IBindableDataProviderBinding();

  private static final MediaTypeBinding MEDIA_TYPE = new MediaTypeBinding();

  public static IUserBinding user() {
    return USER;
  }

  public static IBindableDataProviderBinding iBindableDataProvider() {
    return BINDABLE_DATA_PROVIDER;
  }

  public static MediaTypeBinding mediaType() {
    return MEDIA_TYPE;
  }

  private CoreWicketMoreBindings() {}
}
