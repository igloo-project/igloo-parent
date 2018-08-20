package org.iglooproject.test.business.company.dao;

import org.springframework.stereotype.Repository;

import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.iglooproject.test.business.company.model.Company;

@Repository("companyDao")
public class CompanyDaoImpl extends GenericEntityDaoImpl<Long, Company> implements ICompanyDao {

	public CompanyDaoImpl() {
		super();
	}
}
