package fr.openwide.hibernate.business.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.hibernate.business.generic.service.GenericEntityServiceImpl;
import fr.openwide.hibernate.business.project.dao.ProjectDao;
import fr.openwide.hibernate.business.project.model.Project;

@Service("projectService")
public class ProjectServiceImpl extends GenericEntityServiceImpl<Project> implements ProjectService {

	@Autowired
	public ProjectServiceImpl(ProjectDao projectDao) {
		super(projectDao);
	}
}
