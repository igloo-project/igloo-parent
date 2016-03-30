package fr.openwide.core.test.business.project.dao;

import org.springframework.stereotype.Repository;

import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.test.business.project.model.Project;

@Repository("projectDao")
public class ProjectDaoImpl extends GenericEntityDaoImpl<Long, Project> implements IProjectDao {

	public ProjectDaoImpl() {
		super();
	}
}
