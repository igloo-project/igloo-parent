package test.core.service;

import org.iglooproject.basicapp.core.business.notification.service.EmptyNotificationUrlBuilderServiceImpl;
import org.springframework.stereotype.Service;

/** Implémentation bouche-trou, uniquement pour combler la dépendance. */
@Service("coreTestNotificationUrlBuilderService")
public class CoreTestNotificationUrlBuilderServiceImpl
    extends EmptyNotificationUrlBuilderServiceImpl {}
