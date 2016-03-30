package fr.openwide.core.test.business.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.jpa.business.generic.service.GenericEntityServiceImpl;
import fr.openwide.core.test.business.project.dao.IProjectDao;
import fr.openwide.core.test.business.project.model.Project;

@Service("projectService")
public class ProjectServiceImpl extends GenericEntityServiceImpl<Long, Project> implements IProjectService {

	@Autowired
	public ProjectServiceImpl(IProjectDao projectDao) {
		super(projectDao);
	}
}
