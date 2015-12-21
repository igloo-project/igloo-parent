package fr.openwide.core.basicapp.core.business.history.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Supplier;

import fr.openwide.core.basicapp.core.business.history.dao.IHistoryLogDao;
import fr.openwide.core.basicapp.core.business.history.model.HistoryDifference;
import fr.openwide.core.basicapp.core.business.history.model.HistoryLog;
import fr.openwide.core.basicapp.core.business.history.model.atomic.HistoryEventType;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.service.IUserService;
import fr.openwide.core.jpa.more.business.history.model.bean.AbstractHistoryLogObjectsBean;
import fr.openwide.core.jpa.more.business.history.service.AbstractHistoryLogServiceImpl;

@Service
public class HistoryLogServiceImpl extends AbstractHistoryLogServiceImpl<HistoryLog, HistoryEventType, HistoryDifference>
		implements IHistoryLogService {

	@Autowired
	private IUserService userService;
	
	@Autowired
	public HistoryLogServiceImpl(IHistoryLogDao dao) {
		super(dao);
	}
	
	@Override
	protected HistoryLog newHistoryLog(Date date, HistoryEventType eventType, List<HistoryDifference> differences,
			AbstractHistoryLogObjectsBean<?> objects) {
		HistoryLog log = new HistoryLog(date, eventType, valueService.create(objects.getMainObject()));
		
		User subject = userService.getAuthenticatedUser();
		log.setSubject(valueService.create(subject));
		
		setSecondaryObjects(log, objects);
		
		return log;
	}

	@Override
	protected Supplier<HistoryDifference> newHistoryDifferenceSupplier() {
		return new Supplier<HistoryDifference>() {
			@Override
			public HistoryDifference get() {
				return new HistoryDifference();
			}
		};
	}

}
