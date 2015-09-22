package fr.openwide.core.basicapp.core.util.monitoring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.basicapp.core.business.user.service.IUserService;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.util.IDatabaseConsistencyCheckService;

@Service("databaseConsistencyCheckService")
public class DatabaseConsistencyCheckServiceImpl implements IDatabaseConsistencyCheckService {

	@Autowired
	private IUserService userService;

	@Override
	public void checkDatabaseAccess() {
		userService.count();
	}

	@Override
	public void checkDatabaseConsistency() throws ServiceException, SecurityServiceException {
		// rien
	}

	@Override
	public void checkOnStartup(boolean allowCreateMissingElements) throws ServiceException, SecurityServiceException {
		// rien
	}
}
