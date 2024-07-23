/*
 * Copyright (C) 2009-2011 Open Wide
 * Contact: contact@openwide.fr
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.iglooproject.wicket.more.rendering;

import igloo.wicket.renderer.Renderer;
import java.util.Locale;
import org.apache.wicket.Localizer;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.lang.Classes;
import org.iglooproject.spring.util.StringUtils;

public class EnumRenderer extends Renderer<Enum<?>> {

  private static final long serialVersionUID = -6934415690685574154L;

  private static final EnumRenderer INSTANCE = new EnumRenderer();

  public static EnumRenderer get() {
    return INSTANCE;
  }

  public static EnumRenderer withPrefix(String prefix) {
    return with(prefix, null);
  }

  public static EnumRenderer withSuffix(String suffix) {
    return with(null, suffix);
  }

  public static EnumRenderer with(String prefix, String suffix) {
    return new EnumRenderer(prefix, suffix);
  }

  private String prefix = null;

  private String suffix = null;

  private EnumRenderer() {
    super();
  }

  private EnumRenderer(String prefix, String suffix) {
    this();
    this.prefix = prefix;
    this.suffix = suffix;
  }

  @Override
  public String render(Enum<?> value, Locale locale) {
    if (value == null) {
      return null;
    }

    StringBuilder key = new StringBuilder();

    if (StringUtils.hasText(prefix)) {
      key.append(prefix).append(".");
    }

    key.append(resourceKey(value));

    if (StringUtils.hasText(suffix)) {
      key.append(".").append(suffix);
    }

    return Localizer.get().getString(key.toString(), null, Model.of(value));
  }

  /**
   * @deprecated Do not override this.
   */
  @Deprecated
  protected String resourceKey(Enum<?> value) {
    return Classes.simpleName(value.getDeclaringClass()) + '.' + value.name();
  }
}
