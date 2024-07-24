package org.iglooproject.wicket.more.css.scss.service;

import org.iglooproject.sass.model.ScssStylesheetInformation;
import org.iglooproject.sass.service.IScssService;

public interface ICachedScssService extends IScssService {

  ScssStylesheetInformation getCachedCompiledStylesheet(
      Class<?> scope, String path, boolean checkCacheEntryUpToDate);

  String getCacheKey(Class<?> scope, String path);
}
