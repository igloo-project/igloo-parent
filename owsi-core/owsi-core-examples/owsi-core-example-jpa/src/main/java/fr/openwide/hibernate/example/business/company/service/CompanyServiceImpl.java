package fr.openwide.hibernate.example.business.company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.hibernate.business.generic.service.GenericEntityServiceImpl;
import fr.openwide.hibernate.example.business.company.dao.CompanyDao;
import fr.openwide.hibernate.example.business.company.model.Company;

@Service("companyService")
public class CompanyServiceImpl extends GenericEntityServiceImpl<Integer, Company> implements CompanyService {
	
	@Autowired
	public CompanyServiceImpl(CompanyDao companyDao) {
		super(companyDao);
	}

//	@Override
//	public void addEmployee(Company company, Person employee) throws ServiceException, SecurityServiceException {
//		company.addEmployee(employee);
//		update(company);
//	}
//
//	@Override
//	public void addProject(Company company, Project project) throws ServiceException, SecurityServiceException {
//		company.addProject(project);
//		update(company);
//	}

}
