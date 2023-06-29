package org.iglooproject.test.business.company.dao;

import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.iglooproject.test.business.company.model.Company;
import org.springframework.stereotype.Repository;

@Repository("companyDao")
public class CompanyDaoImpl extends GenericEntityDaoImpl<Long, Company> implements ICompanyDao {

	public CompanyDaoImpl() {
		super();
	}
}
