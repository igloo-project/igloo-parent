package org.iglooproject.bootstrap.api;

import org.apache.wicket.ResourceBundles;
import org.iglooproject.sass.service.IScssService;

public interface IBootstrapProvider {

	void registerImportScopes(IScssService scssService);

	void updateResourceBundles(ResourceBundles resourceBundles);

}
