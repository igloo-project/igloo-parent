package fr.openwide.core.basicapp.core.business.audit.service;

import java.util.Date;
import java.util.List;

import org.apache.http.util.Args;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.basicapp.core.business.audit.dao.IAuditDao;
import fr.openwide.core.basicapp.core.business.audit.model.Audit;
import fr.openwide.core.basicapp.core.business.audit.model.AuditActionType;
import fr.openwide.core.basicapp.core.business.audit.model.search.AuditSearchParametersBean;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.service.IUserService;
import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.audit.service.AbstractAuditServiceImpl;

@Service("auditService")
public class AuditServiceImpl extends AbstractAuditServiceImpl<Audit> implements IAuditService {

	private IAuditDao auditDao;

	@Autowired
	private IAuditActionService auditActionService;

	@Autowired
	private IUserService userService;

	@Autowired
	public AuditServiceImpl(IAuditDao auditDao) {
		super(auditDao);
		this.auditDao = auditDao;
	}

	private User getAuthenticatedUser() {
		return userService.getAuthenticatedUser();
	}

	@Override
	public Audit audit(String service, String method, GenericEntity<Long, ?> object,
			AuditActionType type) throws ServiceException,
			SecurityServiceException {
		
		return audit(new Date(), service, method, getAuthenticatedUser(), object, type);
	}

	@Override
	public Audit audit(Date date, String service, String method, GenericEntity<Long, ?> object,
			AuditActionType type) throws ServiceException,
			SecurityServiceException {
		
		return audit(date, service, method, getAuthenticatedUser(), object, type);
	}

	@Override
	public Audit audit(String service, String method, User subject,
			GenericEntity<Long, ?> object, AuditActionType type) throws ServiceException,
			SecurityServiceException {
		
		return audit(new Date(), service, method, null, subject, object, null, type, null);
	}
	
	@Override
	public Audit audit(Date date, String service, String method, User subject,
			GenericEntity<Long, ?> object, AuditActionType type) throws ServiceException,
			SecurityServiceException {
		
		return audit(date, service, method, null, subject, object, null, type, null);
	}
	
	@Override
	public Audit audit(Date date, String service, String method, GenericEntity<Long, ?> context, User subject,
			GenericEntity<Long, ?> object, GenericEntity<Long, ?> secondaryObject, AuditActionType type,
			String message) throws ServiceException, SecurityServiceException {
		Args.notNull(subject, "subject");
		if (context == null && object == null) {
			throw new IllegalArgumentException("Context and object audit are both null");
		}
		
		Audit audit = new Audit(date, service, method, context, subject, object, secondaryObject,
				auditActionService.getByType(type), message);
		
		create(audit);
		return audit;
	}

	@Override
	public List<Audit> search(AuditSearchParametersBean searchParams, Long limit, Long offset)
			throws ServiceException, SecurityServiceException {
		return auditDao.search(searchParams, limit, offset);
	}

	@Override
	public long count(AuditSearchParametersBean searchParams) throws ServiceException, SecurityServiceException {
		return auditDao.count(searchParams);
	}
}
