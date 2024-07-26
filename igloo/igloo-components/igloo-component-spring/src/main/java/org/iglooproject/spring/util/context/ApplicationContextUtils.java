/*
 * Copyright (C) 2009-2012 Open Wide
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

package org.iglooproject.spring.util.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Utilitaire permettant d'accéder de manière statique à l'ApplicationContext en utilisant
 * ApplicationContextUtils.getContext();
 *
 * <p>-> A n'utiliser que quand on ne peut pas faire autrement... privilégier l'injection de
 * l'ApplicationContext quand c'est possible.
 *
 * <p>L'applicationContext doit être préalablement renseigné par Spring en Java Config de la manière
 * suivante : @Bean public ApplicationContextUtils applicationContextUtils() { return
 * ApplicationContextUtils.getInstance(); } ou en xml de la manière suivante : <bean
 * class="org.iglooproject.spring.util.context.ApplicationContextUtils" factory-method="getInstance"
 * />
 */
public final class ApplicationContextUtils {

  private static final ApplicationContextUtils INSTANCE = new ApplicationContextUtils();

  @Autowired private ApplicationContext applicationContext;

  public static ApplicationContext getContext() {
    return INSTANCE.applicationContext;
  }
  ;

  public static ApplicationContextUtils getInstance() {
    return INSTANCE;
  }

  private ApplicationContextUtils() {}
}
