package org.iglooproject.jpa.more.util.init.dao;

import java.io.Serializable;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

public interface IImportDataDao {

  <K extends Serializable & Comparable<K>, E extends GenericEntity<?, ?>> E getById(
      Class<E> clazz, K id);

  <E extends GenericEntity<?, ?>> void create(E entity);
}
