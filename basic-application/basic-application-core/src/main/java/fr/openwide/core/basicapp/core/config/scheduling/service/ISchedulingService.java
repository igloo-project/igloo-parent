package fr.openwide.core.basicapp.core.config.scheduling.service;

import fr.openwide.core.jpa.business.generic.service.ITransactionalAspectAwareService;

public interface ISchedulingService extends ITransactionalAspectAwareService {

	void temporaryFilesCleaning();
}
