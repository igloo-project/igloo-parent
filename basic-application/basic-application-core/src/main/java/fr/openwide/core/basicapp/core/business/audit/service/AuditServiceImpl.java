package fr.openwide.core.basicapp.core.business.audit.service;

import java.util.Date;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.basicapp.core.business.audit.dao.IAuditDao;
import fr.openwide.core.basicapp.core.business.audit.model.Audit;
import fr.openwide.core.basicapp.core.business.audit.model.atomic.AuditAction;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.service.IUserService;
import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.audit.service.AbstractAuditServiceImpl;

@Service("auditService")
public class AuditServiceImpl extends AbstractAuditServiceImpl<Audit> implements IAuditService {

	@Autowired
	private IUserService userService;

	@Autowired
	public AuditServiceImpl(IAuditDao auditDao) {
		super(auditDao);
	}

	private User getAuthenticatedUser() {
		return userService.getAuthenticatedUser();
	}

	@Override
	public Audit audit(String service, String method, GenericEntity<Long, ?> object,
			AuditAction action) throws ServiceException,
			SecurityServiceException {
		
		return audit(new Date(), service, method, getAuthenticatedUser(), object, action);
	}

	@Override
	public Audit audit(Date date, String service, String method, GenericEntity<Long, ?> object,
			AuditAction action) throws ServiceException,
			SecurityServiceException {
		
		return audit(date, service, method, getAuthenticatedUser(), object, action);
	}

	@Override
	public Audit audit(String service, String method, User subject,
			GenericEntity<Long, ?> object, AuditAction action) throws ServiceException,
			SecurityServiceException {
		
		return audit(new Date(), service, method, null, subject, object, null, action, null);
	}
	
	@Override
	public Audit audit(Date date, String service, String method, User subject,
			GenericEntity<Long, ?> object, AuditAction action) throws ServiceException,
			SecurityServiceException {
		
		return audit(date, service, method, null, subject, object, null, action, null);
	}
	
	@Override
	public Audit audit(Date date, String service, String method, GenericEntity<Long, ?> context, User subject,
			GenericEntity<Long, ?> object, GenericEntity<Long, ?> secondaryObject, AuditAction action,
			String message) throws ServiceException, SecurityServiceException {
		Objects.requireNonNull(subject, "subject must not be null");
		if (context == null && object == null) {
			throw new IllegalArgumentException("Context and object audit are both null");
		}
		
		Audit audit = new Audit(date, service, method, context, subject, object, secondaryObject,
				action, message);
		
		create(audit);
		return audit;
	}

}
