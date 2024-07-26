package org.iglooproject.test.business.util.service;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.test.business.company.model.Company;
import org.iglooproject.test.business.company.service.ICompanyService;
import org.iglooproject.test.transaction.CheckedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Classe utilitaire permettant de générer des exceptions et de vérifier leur bonne prise en compte
 * par le transaction manager
 *
 * @author Open Wide
 */
@Service
public class ServiceExceptionServiceImpl implements ServiceExceptionService {

  @Autowired private ICompanyService companyService;

  @Override
  public void dontThrow() throws ServiceException, SecurityServiceException {
    Company company = new Company("Company Test");
    companyService.create(company);
  }

  @Override
  public void throwServiceException() throws ServiceException, SecurityServiceException {
    Company company = new Company("Company Test");
    companyService.create(company);
    throw new ServiceException();
  }

  @Override
  public void throwServiceInheritedException() throws ServiceException, SecurityServiceException {
    Company company = new Company("Company Test");
    companyService.create(company);
    throw new MyException();
  }

  @Override
  public void throwUncheckedException() throws ServiceException, SecurityServiceException {
    Company company = new Company("Company Test");
    companyService.create(company);
    throw new IllegalStateException();
  }

  @Override
  public void throwCheckedException()
      throws ServiceException, SecurityServiceException, CheckedException {
    Company company = new Company("Company Test");
    companyService.create(company);
    throw new CheckedException();
  }

  @Override
  public long size() {
    return companyService.count();
  }

  private static class MyException extends ServiceException {
    private static final long serialVersionUID = -6408089837070550022L;
  }
}
