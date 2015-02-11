package fr.openwide.core.basicapp.core.business.audit.service;

import java.util.Date;
import java.util.List;

import fr.openwide.core.basicapp.core.business.audit.model.Audit;
import fr.openwide.core.basicapp.core.business.audit.model.atomic.AuditAction;
import fr.openwide.core.basicapp.core.business.audit.model.search.AuditSearchParametersBean;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.audit.service.IAbstractAuditService;

public interface IAuditService extends IAbstractAuditService<Audit> {

	Audit audit(String service, String method, GenericEntity<Long, ?> object, AuditAction action)
			throws ServiceException, SecurityServiceException;

	Audit audit(Date date, String service, String method, GenericEntity<Long, ?> object, AuditAction action)
			throws ServiceException, SecurityServiceException;

	Audit audit(String service, String method, User subject, GenericEntity<Long, ?> object, AuditAction action)
			throws ServiceException, SecurityServiceException;

	Audit audit(Date date, String service, String method, User subject, GenericEntity<Long, ?> object,
			AuditAction action) throws ServiceException, SecurityServiceException;

	Audit audit(Date date, String service, String method, GenericEntity<Long, ?> context, User subject,
			GenericEntity<Long, ?> object, GenericEntity<Long, ?> secondaryObject, AuditAction action, String message)
			throws ServiceException, SecurityServiceException;

	List<Audit> search(AuditSearchParametersBean searchParams, Long limit, Long offset) throws ServiceException,
			SecurityServiceException;

	long count(AuditSearchParametersBean searchParams) throws ServiceException, SecurityServiceException;

}
