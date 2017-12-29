package org.iglooproject.basicapp.core.test.service;

import org.springframework.stereotype.Service;

import org.iglooproject.basicapp.core.business.notification.service.EmptyNotificationUrlBuilderServiceImpl;

/**
 * Implémentation bouche-trou, uniquement pour combler la dépendance.
 */
@Service("coreTestNotificationUrlBuilderService")
public class CoreTestNotificationUrlBuilderServiceImpl extends EmptyNotificationUrlBuilderServiceImpl {

}
