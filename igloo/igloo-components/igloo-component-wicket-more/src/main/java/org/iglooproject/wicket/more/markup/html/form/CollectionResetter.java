/*
 * Copyright (C) 2009-2015 Open Wide
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

package org.iglooproject.wicket.more.markup.html.form;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializableSupplier2;

public class CollectionResetter<C> implements Resetter {

  private static final long serialVersionUID = 8258908044311296337L;

  private final SerializableSupplier2<C> newCollectionSupplier;

  public CollectionResetter(SerializableSupplier2<C> newCollectionSupplier) {
    this.newCollectionSupplier = newCollectionSupplier;
  }

  @Override
  public void reset(Component component) {
    @SuppressWarnings("unchecked")
    IModel<C> model = (IModel<C>) component.getDefaultModel();

    if (model != null && model.getObject() != null) {
      model.setObject(newCollectionSupplier.get());
    }
  }
}
