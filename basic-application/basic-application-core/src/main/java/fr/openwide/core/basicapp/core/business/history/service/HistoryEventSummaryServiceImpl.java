package fr.openwide.core.basicapp.core.business.history.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.service.IUserService;
import fr.openwide.core.jpa.more.business.history.service.AbstractHistoryEventSummaryServiceImpl;

@Service
public class HistoryEventSummaryServiceImpl extends AbstractHistoryEventSummaryServiceImpl<User>
		implements IHistoryEventSummaryService {
	
	@Autowired
	private IUserService userService;

	@Override
	protected User getDefaultSubject() {
		return userService.getAuthenticatedUser();
	}

}
