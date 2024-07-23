package org.iglooproject.test.business.label.dao;

import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.iglooproject.test.business.label.model.Label;
import org.springframework.stereotype.Repository;

@Repository("labelDao")
public class LabelDaoImpl extends GenericEntityDaoImpl<String, Label> implements ILabelDao {

  public LabelDaoImpl() {
    super();
  }
}
