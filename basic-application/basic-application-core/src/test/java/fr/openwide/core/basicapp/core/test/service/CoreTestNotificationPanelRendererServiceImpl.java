package fr.openwide.core.basicapp.core.test.service;

import org.springframework.stereotype.Service;

import fr.openwide.core.basicapp.core.business.notification.service.EmptyNotificationPanelRendererServiceImpl;

/**
 * Implémentation bouche-trou, uniquement pour combler la dépendance.
 */
@Service("coreTestNotificationPanelRendererService")
public class CoreTestNotificationPanelRendererServiceImpl extends EmptyNotificationPanelRendererServiceImpl {

}
