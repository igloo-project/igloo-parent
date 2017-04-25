package fr.openwide.core.basicapp.core.business.upgrade.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.service.IUserService;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.upgrade.model.IDataUpgrade;

public class MigrationTest implements IDataUpgrade {

	private static final Logger LOGGER = LoggerFactory.getLogger(MigrationTest.class);

	@Autowired
	private IUserService userService;

	@Override
	public String getName() {
		return MigrationTest.class.getSimpleName();
	}

	@Override
	public void perform() throws ServiceException, SecurityServiceException {
		LOGGER.info("Performing MigrationTest upgrade");
		
		User admin = userService.getByUserName("admin");
		admin.setFirstName("fail");
		userService.update(admin);
		
		throw new ServiceException();
	}
}
