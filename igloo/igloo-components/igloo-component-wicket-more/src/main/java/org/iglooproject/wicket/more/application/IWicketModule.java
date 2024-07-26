package org.iglooproject.wicket.more.application;

import org.apache.wicket.ResourceBundles;
import org.apache.wicket.settings.ResourceSettings;
import org.iglooproject.sass.service.IScssService;

public interface IWicketModule {

  void updateResourceBundles(ResourceBundles resourceBundles);

  void updateResourceSettings(ResourceSettings resourceSettings);

  void registerImportScopes(IScssService scssService);
}
