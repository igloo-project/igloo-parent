package fr.openwide.hibernate.example.business.util.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.hibernate.example.business.company.model.Company;
import fr.openwide.hibernate.example.business.company.service.CompanyService;
import fr.openwide.hibernate.exception.SecurityServiceException;
import fr.openwide.hibernate.exception.ServiceException;


/**
 * Classe utilitaire permettant de générer des exceptions et de vérifier
 * leur bonne prise en compte par le transaction manager
 * 
 * @author Open Wide
 */
@Service
public class ServiceExceptionServiceImpl {
	
	@Autowired
	private CompanyService companyService;

	public void dontThrow() throws ServiceException, SecurityServiceException {
		Company company = new Company("Company Test");
		companyService.create(company);
	}

	public void throwServiceException() throws ServiceException, SecurityServiceException {
		Company company = new Company("Company Test");
		companyService.create(company);
		throw new ServiceException();
	}

	public void throwServiceInheritedException() throws ServiceException, SecurityServiceException {
		Company company = new Company("Company Test");
		companyService.create(company);
		throw new MyException() ;
	}

	public void throwUncheckedException() throws ServiceException, SecurityServiceException {
		Company company = new Company("Company Test");
		companyService.create(company);
		throw new IllegalStateException();
	}

	public long size() {
		return companyService.count();
	}
	
	private class MyException extends ServiceException {
		private static final long serialVersionUID = -6408089837070550022L;
	}
}
