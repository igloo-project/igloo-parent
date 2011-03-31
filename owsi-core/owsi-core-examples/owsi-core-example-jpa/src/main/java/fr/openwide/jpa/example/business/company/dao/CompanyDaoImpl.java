package fr.openwide.jpa.example.business.company.dao;

import org.springframework.stereotype.Repository;

import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.jpa.example.business.company.model.Company;

@Repository("companyDao")
public class CompanyDaoImpl extends GenericEntityDaoImpl<Integer, Company> implements CompanyDao {

	public CompanyDaoImpl() {
		super();
	}
}
