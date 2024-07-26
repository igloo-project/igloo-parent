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

/**
 * DAO racine pour la gestion des entités.
 *
 * @author Open Wide
 * @param <T> type de l'entité à prendre en charge
 */
package org.iglooproject.jpa.business.generic.dao;

import java.io.Serializable;
import java.util.List;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.model.IReference;

/**
 * DAO racine pour la gestion des entités.
 *
 * @author Open Wide
 * @param <E> type de l'entité à prendre en charge
 */
public interface IGenericEntityDao<
    K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> {

  /**
   * Retourne une entité à partir de son id.
   *
   * @param id identifiant
   * @return entité
   */
  E getById(K id);

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
   * Retourne une entité à partir de son id naturelle (si elle a été déclarée avec @NaturalId)
   *
   * @param naturalId identifiant naturel (typiquement un login)
   */
  E getByNaturalId(Object naturalId);

  /**
   * Crée l'entité dans la base de données.
   *
   * @param entity entité
   */
  void save(E entity);

  /**
   * Met à jour l'entité dans la base de données.
   *
   * @param entity entité
   */
  void update(E entity);

  /**
   * Supprime l'entité de la base de données
   *
   * @param entity entité
   */
  void delete(E entity);

  /**
   * Rafraîchit l'entité depuis la base de données
   *
   * @param entity entité
   */
  E refresh(E entity);

  /**
   * Renvoie la liste de l'ensemble des entités de ce type.
   *
   * @return liste d'entités
   */
  List<E> list();

  /**
   * Renvoie la liste de l'ensemble des IDs d'entités de ce type.
   *
   * @return liste d'IDs
   */
  List<E> list(Long limit, Long offset);

  /**
   * Compte le nombre d'entités de ce type présentes dans la base.
   *
   * @return nombre d'entités
   */
  Long count();

  /** Flushe la session. */
  void flush();

  /** Clear la session. */
  void clear();
}
