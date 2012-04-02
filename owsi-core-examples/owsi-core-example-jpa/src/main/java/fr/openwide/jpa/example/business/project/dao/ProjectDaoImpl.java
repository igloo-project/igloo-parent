package fr.openwide.jpa.example.business.project.dao;

import org.springframework.stereotype.Repository;

import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.jpa.example.business.project.model.Project;

@Repository("projectDao")
public class ProjectDaoImpl extends GenericEntityDaoImpl<Long, Project> implements ProjectDao {

	public ProjectDaoImpl() {
		super();
	}
}
