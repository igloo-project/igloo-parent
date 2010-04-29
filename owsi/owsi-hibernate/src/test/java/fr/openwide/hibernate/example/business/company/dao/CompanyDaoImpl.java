package fr.openwide.hibernate.example.business.company.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.openwide.hibernate.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.hibernate.example.business.company.model.Company;

@Repository("companyDao")
public class CompanyDaoImpl extends GenericEntityDaoImpl<Company> implements CompanyDao {

	@Autowired
	public CompanyDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
}
