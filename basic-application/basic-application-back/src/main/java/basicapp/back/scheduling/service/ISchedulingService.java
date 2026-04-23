package basicapp.back.scheduling.service;

import org.iglooproject.jpa.business.generic.service.ITransactionalAspectAwareService;

public interface ISchedulingService extends ITransactionalAspectAwareService {

  void temporaryFilesCleaning();
}
