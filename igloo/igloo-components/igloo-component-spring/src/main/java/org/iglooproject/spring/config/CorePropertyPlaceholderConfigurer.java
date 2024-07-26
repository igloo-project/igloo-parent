/*
 * Copyright (C) 2009-2010 Open Wide
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

package org.iglooproject.spring.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurablePropertyResolver;
import org.springframework.core.env.PropertyResolver;

/**
 * Gère la récupération des propriétés de configuration.
 *
 * <p>Ajout de fonctionnalités supplémentaires par rapport au configurer Spring permettant : - la
 * résolution via l'appel aux méthodes getPropertyAsXXXX(propertyName) (avec substitution des
 * placeholders)
 *
 * @author Open Wide
 */
public class CorePropertyPlaceholderConfigurer extends PropertySourcesPlaceholderConfigurer {

  /** Propriétés de configuration. */
  private PropertyResolver propertyResolver;

  /**
   * Retourne une propriété spécifique à partir de sa clé La propriété retournée n'est pas castée.
   *
   * @param key la clé
   * @return l'objet propriété
   */
  public String getProperty(String key) {
    String rawValue = propertyResolver.getProperty(key);

    if (rawValue != null) {
      return propertyResolver.resolvePlaceholders(rawValue);
    } else {
      return null;
    }
  }

  @Override
  protected void processProperties(
      ConfigurableListableBeanFactory beanFactoryToProcess,
      ConfigurablePropertyResolver propertyResolver)
      throws BeansException {
    this.propertyResolver = propertyResolver;

    // on garde les traitements dans la classe parente
    super.processProperties(beanFactoryToProcess, propertyResolver);
  }
}
