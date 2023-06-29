package org.iglooproject.test.business.project.service;

import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.test.business.project.dao.IProjectDao;
import org.iglooproject.test.business.project.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("projectService")
public class ProjectServiceImpl extends GenericEntityServiceImpl<Long, Project> implements IProjectService {

	@Autowired
	public ProjectServiceImpl(IProjectDao projectDao) {
		super(projectDao);
	}
}
