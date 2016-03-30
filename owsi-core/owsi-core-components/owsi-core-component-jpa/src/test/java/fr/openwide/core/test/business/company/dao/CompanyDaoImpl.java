package fr.openwide.core.test.business.company.dao;

import org.springframework.stereotype.Repository;

import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.test.business.company.model.Company;

@Repository("companyDao")
public class CompanyDaoImpl extends GenericEntityDaoImpl<Long, Company> implements ICompanyDao {

	public CompanyDaoImpl() {
		super();
	}
}
