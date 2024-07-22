package org.iglooproject.jpa.more.business.localization.model;

import java.io.Serializable;
import java.util.Locale;

public interface ILocalizedText extends Serializable, Cloneable {

  String get(Locale locale);
}
