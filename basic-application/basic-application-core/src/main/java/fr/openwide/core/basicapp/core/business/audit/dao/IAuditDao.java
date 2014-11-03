package fr.openwide.core.basicapp.core.business.audit.dao;

import java.util.List;

import fr.openwide.core.basicapp.core.business.audit.model.Audit;
import fr.openwide.core.basicapp.core.business.audit.model.search.AuditSearchParametersBean;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.audit.dao.IAbstractAuditDao;

public interface IAuditDao extends IAbstractAuditDao<Audit> {

	List<Audit> search(AuditSearchParametersBean searchParams, Long limit, Long offset) throws ServiceException,
			SecurityServiceException;

	long count(AuditSearchParametersBean searchParams) throws ServiceException, SecurityServiceException;

}
