package org.iglooproject.spring.notification.model;

import java.io.Serializable;

public interface INotificationContentBody extends Serializable {

	String getPlainText();

	String getHtmlText();

}
