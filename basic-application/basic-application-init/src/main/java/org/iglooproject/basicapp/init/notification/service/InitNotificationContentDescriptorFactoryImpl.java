package org.iglooproject.basicapp.init.notification.service;

import org.springframework.stereotype.Service;

import org.iglooproject.basicapp.core.business.notification.service.EmptyNotificationContentDescriptorFactoryImpl;

/**
 * Implémentation bouche-trou, uniquement pour combler la dépendance.
 */
@Service("InitNotificationPanelRendererServiceImpl")
public class InitNotificationContentDescriptorFactoryImpl extends EmptyNotificationContentDescriptorFactoryImpl {

}
