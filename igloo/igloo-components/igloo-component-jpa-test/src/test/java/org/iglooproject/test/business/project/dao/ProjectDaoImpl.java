package org.iglooproject.test.business.project.dao;

import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.iglooproject.test.business.project.model.Project;
import org.springframework.stereotype.Repository;

@Repository("projectDao")
public class ProjectDaoImpl extends GenericEntityDaoImpl<Long, Project> implements IProjectDao {

  public ProjectDaoImpl() {
    super();
  }
}
