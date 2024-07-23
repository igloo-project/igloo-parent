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

package org.iglooproject.jpa.business.generic.service;

import java.io.Serializable;
import java.util.List;
import org.iglooproject.commons.util.security.PermissionObject;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.model.IReference;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;

/**
 * Service racine pour la gestion des entités.
 *
 * @author Open Wide
 * @param <T> type d'entité
 */
public interface IGenericEntityService<
        K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
    extends ITransactionalAspectAwareService {

  /**
   * Met à jour l'entité dans la base de données.
   *
   * @param entity entité
   */
  void update(@PermissionObject E entity) throws ServiceException, SecurityServiceException;

  /**
   * Crée l'entité dans la base de données.
   *
   * @param entity entité
   */
  void create(@PermissionObject E entity) throws ServiceException, SecurityServiceException;

  /**
   * Supprime l'entité de la base de données
   *
   * @param entity entité
   */
  void delete(@PermissionObject E entity) throws ServiceException, SecurityServiceException;

  /**
   * Rafraîchit l'entité depuis la base de données
   *
   * @param entity entité
   */
  E refresh(@PermissionObject E entity);

  /**
   * Retourne une entité à partir de son id.
   *
   * @param id identifiant
   * @return entité
   */
  E getById(@PermissionObject K id);

  /**
   * Retourne une entité à partir de sa classe (dérivée de {@link E}) et de son id.
   *
   * @param id identifiant
   * @return entité
   */
  <T extends E> T getById(Class<T> clazz, K id);

  /**
   * Retourne une entité à partir d'une référence.
   *
   * @param reference
   * @return entité
   */
  <T extends E> T getById(IReference<T> reference);

  /**
   * Renvoie la liste de l'ensemble des entités de ce type.
   *
   * @return liste d'entités
   */
  List<E> list();

  /**
   * Compte le nombre d'entités de ce type présentes dans la base.
   *
   * @return nombre d'entités
   */
  Long count();

  /** Flushe la session. */
  void flush();

  void clear();
}
