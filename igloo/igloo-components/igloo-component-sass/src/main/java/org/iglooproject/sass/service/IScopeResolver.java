package org.iglooproject.sass.service;

import org.iglooproject.sass.exceptions.UnknownScssScope;

public interface IScopeResolver {

  /**
   * Rewrite a <code>$(scope-NAME)/</code> URL. Left URL untouched
   *
   * @param scoped url (<code>$(scope-NAME)/path/file</code> value). If url is not scoped, original
   *     url is returned.
   * @return original url if not scoped, else scopedUrl with scope replaced by classpath resource
   *     path.
   * @throws UnknownScssScope if NAME scope if not known.
   */
  String resolveScope(String scopedUrl);
}
