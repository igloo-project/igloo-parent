package fr.openwide.jpa.example.business.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.jpa.business.generic.service.GenericEntityServiceImpl;
import fr.openwide.jpa.example.business.project.dao.ProjectDao;
import fr.openwide.jpa.example.business.project.model.Project;

@Service("projectService")
public class ProjectServiceImpl extends GenericEntityServiceImpl<Long, Project> implements ProjectService {

	@Autowired
	public ProjectServiceImpl(ProjectDao projectDao) {
		super(projectDao);
	}
}
