package org.iglooproject.sass.service;

import org.iglooproject.sass.model.ScssStylesheetInformation;

public interface IScssService {

  ScssStylesheetInformation getCompiledStylesheet(Class<?> scope, String path);

  void registerImportScope(String scopeName, Class<?> scope);
}
