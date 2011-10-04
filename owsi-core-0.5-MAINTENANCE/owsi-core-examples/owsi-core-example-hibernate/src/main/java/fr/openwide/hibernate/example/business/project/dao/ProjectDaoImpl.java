package fr.openwide.hibernate.example.business.project.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.openwide.core.hibernate.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.hibernate.example.business.project.model.Project;

@Repository("projectDao")
public class ProjectDaoImpl extends GenericEntityDaoImpl<Integer, Project> implements ProjectDao {

	@Autowired
	public ProjectDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
}
