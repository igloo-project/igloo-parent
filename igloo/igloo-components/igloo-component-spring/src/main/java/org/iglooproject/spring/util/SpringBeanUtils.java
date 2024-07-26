/*
 * Copyright (C) 2009-2010 Open Wide
 * Contact: contact@openwide.fr
 *
 * Large portions Copyright Apache Software Foundation
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

package org.iglooproject.spring.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.context.ApplicationContext;

/**
 * Ensemble de méthodes utilitaires permettant de cabler certaines manipulations Spring sur les
 * beans.
 */
public final class SpringBeanUtils {

  /**
   * Retourne un wrapper permettant de manipuler programmatiquement les propriétés du bean en
   * utilisant les getters et les setters.
   *
   * @param bean un bean quelconque
   * @return un wrapper autour du bean
   */
  public static BeanWrapper getBeanWrapper(Object bean) {
    return PropertyAccessorFactory.forBeanPropertyAccess(bean);
  }

  /**
   * Permet de faire réaliser l'injection de dépendances par Spring sur un bean.
   *
   * @param applicationContext le contexte d'application Spring
   * @param bean un bean Spring
   */
  public static void autowireBean(ApplicationContext applicationContext, Object bean) {
    applicationContext.getAutowireCapableBeanFactory().autowireBean(bean);
  }

  private SpringBeanUtils() {}
}
