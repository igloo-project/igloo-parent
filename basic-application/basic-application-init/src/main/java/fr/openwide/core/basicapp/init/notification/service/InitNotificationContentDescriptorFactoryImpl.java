package fr.openwide.core.basicapp.init.notification.service;

import org.springframework.stereotype.Service;

import fr.openwide.core.basicapp.core.business.notification.service.EmptyNotificationContentDescriptorFactoryImpl;

/**
 * Implémentation bouche-trou, uniquement pour combler la dépendance.
 */
@Service("InitNotificationPanelRendererServiceImpl")
public class InitNotificationContentDescriptorFactoryImpl extends EmptyNotificationContentDescriptorFactoryImpl {

}
