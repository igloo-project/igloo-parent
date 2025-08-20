package org.iglooproject.jpa.more.util.init.dao;

import java.io.Serializable;
import org.iglooproject.jpa.business.generic.dao.JpaDaoSupport;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.springframework.stereotype.Repository;

@Repository
public class ImportDataDaoImpl extends JpaDaoSupport implements IImportDataDao {

  public ImportDataDaoImpl() {}

  @Override
  public <K extends Serializable & Comparable<K>, E extends GenericEntity<?, ?>> E getById(
      Class<E> clazz, K id) {
    return super.getEntity(clazz, id);
  }

  @Override
  public <E extends GenericEntity<?, ?>> void create(E entity) {
    super.save(entity);
  }
}
