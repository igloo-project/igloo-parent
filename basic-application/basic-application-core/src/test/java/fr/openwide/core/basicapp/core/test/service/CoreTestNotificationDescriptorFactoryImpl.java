package fr.openwide.core.basicapp.core.test.service;

import org.springframework.stereotype.Service;

import fr.openwide.core.basicapp.core.business.notification.service.EmptyNotificationContentDescriptorFactoryImpl;

/**
 * Implémentation bouche-trou, uniquement pour combler la dépendance.
 */
@Service("CoreTestNotificationDescriptorFactory")
public class CoreTestNotificationDescriptorFactoryImpl extends EmptyNotificationContentDescriptorFactoryImpl {

}
