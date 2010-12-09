package fr.openwide.hibernate.example.business.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.hibernate.business.generic.service.GenericEntityServiceImpl;
import fr.openwide.hibernate.example.business.project.dao.ProjectDao;
import fr.openwide.hibernate.example.business.project.model.Project;

@Service("projectService")
public class ProjectServiceImpl extends GenericEntityServiceImpl<Integer, Project> implements ProjectService {

	@Autowired
	public ProjectServiceImpl(ProjectDao projectDao) {
		super(projectDao);
	}
}
