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

package igloo.wicket.component;

import igloo.wicket.renderer.Renderer;
import igloo.wicket.util.IDatePattern;
import java.util.Date;
import org.apache.wicket.model.IModel;

public class DateLabel extends AbstractCoreLabel<DateLabel> {

  private static final long serialVersionUID = 7214422620839758144L;

  public DateLabel(String id, IModel<Date> model, IDatePattern datePattern) {
    super(id, Renderer.fromDatePattern(datePattern).asModel(model));
  }

  @Override
  protected DateLabel thisAsT() {
    return this;
  }
}
