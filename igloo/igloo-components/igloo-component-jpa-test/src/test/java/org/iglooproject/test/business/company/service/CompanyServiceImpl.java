package org.iglooproject.test.business.company.service;

import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.test.business.company.dao.ICompanyDao;
import org.iglooproject.test.business.company.model.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("companyService")
public class CompanyServiceImpl extends GenericEntityServiceImpl<Long, Company>
    implements ICompanyService {

  @Autowired
  public CompanyServiceImpl(ICompanyDao companyDao) {
    super(companyDao);
  }

  //	@Override
  //	public void addEmployee(Company company, Person employee) throws ServiceException,
  // SecurityServiceException {
  //		company.addEmployee(employee);
  //		update(company);
  //	}
  //
  //	@Override
  //	public void addProject(Company company, Project project) throws ServiceException,
  // SecurityServiceException {
  //		company.addProject(project);
  //		update(company);
  //	}

}
