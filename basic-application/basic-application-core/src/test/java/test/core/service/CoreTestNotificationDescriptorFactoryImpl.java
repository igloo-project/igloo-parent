package test.core.service;

import org.iglooproject.basicapp.core.business.notification.service.EmptyNotificationContentDescriptorFactoryImpl;
import org.springframework.stereotype.Service;

/** Implémentation bouche-trou, uniquement pour combler la dépendance. */
@Service("CoreTestNotificationDescriptorFactory")
public class CoreTestNotificationDescriptorFactoryImpl
    extends EmptyNotificationContentDescriptorFactoryImpl {}
