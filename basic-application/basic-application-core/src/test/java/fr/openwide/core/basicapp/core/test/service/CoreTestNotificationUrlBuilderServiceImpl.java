package fr.openwide.core.basicapp.core.test.service;

import org.springframework.stereotype.Service;

import fr.openwide.core.basicapp.core.business.notification.service.EmptyNotificationUrlBuilderServiceImpl;

/**
 * Implémentation bouche-trou, uniquement pour combler la dépendance.
 */
@Service("coreTestNotificationUrlBuilderService")
public class CoreTestNotificationUrlBuilderServiceImpl extends EmptyNotificationUrlBuilderServiceImpl {

}
