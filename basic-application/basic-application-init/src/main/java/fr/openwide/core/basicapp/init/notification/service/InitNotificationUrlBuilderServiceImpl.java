package fr.openwide.core.basicapp.init.notification.service;

import org.springframework.stereotype.Service;

import fr.openwide.core.basicapp.core.business.notification.service.EmptyNotificationUrlBuilderServiceImpl;

/**
 * Implémentation bouche-trou, uniquement pour combler la dépendance.
 */
@Service("initNotificationUrlBuilderService")
public class InitNotificationUrlBuilderServiceImpl extends EmptyNotificationUrlBuilderServiceImpl {

}
