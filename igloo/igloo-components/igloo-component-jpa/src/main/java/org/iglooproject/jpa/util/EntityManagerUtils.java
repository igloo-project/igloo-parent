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

package org.iglooproject.jpa.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Classe permettant de manipuler le EntityManager.
 *
 * <p>Utilisée dans les tests pour ouvrir la session au début d'un test et la fermer à la fin
 *
 * @author Open Wide
 */
public class EntityManagerUtils {

  @Autowired private EntityManagerFactory entityManagerFactory;

  @PersistenceContext private EntityManager entityManager;

  /** Mise en place du EntityManager */
  public EntityManager openEntityManager() {
    if (TransactionSynchronizationManager.hasResource(entityManagerFactory)) {
      return ((EntityManagerHolder)
              TransactionSynchronizationManager.getResource(entityManagerFactory))
          .getEntityManager();
    } else {
      EntityManager entityManager = entityManagerFactory.createEntityManager();
      TransactionSynchronizationManager.bindResource(
          entityManagerFactory, new EntityManagerHolder(entityManager));
      return entityManager;
    }
  }

  /** Suppression du EntityManager. */
  public void closeEntityManager() {
    if (TransactionSynchronizationManager.hasResource(entityManagerFactory)) {
      EntityManagerHolder entityManagerHolder =
          (EntityManagerHolder)
              TransactionSynchronizationManager.unbindResource(entityManagerFactory);
      EntityManagerFactoryUtils.closeEntityManager(entityManagerHolder.getEntityManager());
    }
  }

  /**
   * Retourne l'EntityManager courant.
   *
   * <p>Renvoie une exception si l'EntityManager n'est pas présent.
   */
  public EntityManager getCurrentEntityManager() {
    if (TransactionSynchronizationManager.hasResource(entityManagerFactory)) {
      return ((EntityManagerHolder)
              TransactionSynchronizationManager.getResource(entityManagerFactory))
          .getEntityManager();
    } else {
      throw new IllegalStateException("No EntityManager in the TransactionSynchronizationManager");
    }
  }

  /** Retourne l'EntityManager injecté. */
  public EntityManager getEntityManager() {
    return entityManager;
  }
}
