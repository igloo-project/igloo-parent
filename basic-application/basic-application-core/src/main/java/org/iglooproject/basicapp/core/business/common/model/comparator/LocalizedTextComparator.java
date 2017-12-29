package org.iglooproject.basicapp.core.business.common.model.comparator;

import java.util.Locale;

import org.iglooproject.basicapp.core.business.common.model.embeddable.LocalizedText;
import org.iglooproject.jpa.more.business.localization.util.AbstractLocalizedTextComparator;

public class LocalizedTextComparator extends AbstractLocalizedTextComparator<LocalizedText> {

	private static final long serialVersionUID = -1217040817920839219L;

	public LocalizedTextComparator(Locale locale) {
		super(locale);
		}

}
