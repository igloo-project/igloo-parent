package org.iglooproject.jpa.more.business.localization.util;

import java.io.Serializable;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import org.iglooproject.commons.util.ordering.AbstractNullSafeComparator;
import org.iglooproject.commons.util.ordering.SerializableCollator;
import org.iglooproject.jpa.more.business.localization.model.AbstractLocalizedText;

public abstract class AbstractLocalizedTextComparator<T extends AbstractLocalizedText> extends AbstractNullSafeComparator<T>
		implements Serializable {
	
	private static final long serialVersionUID = -6565287329417568609L;
	
	private final Locale locale;
	private final Comparator<String> textComparator;
	
	public AbstractLocalizedTextComparator(Locale locale) {
		if (locale == null) {
			throw new IllegalStateException("Locale missing.");
		}
		this.locale = locale;
		SerializableCollator collator = new SerializableCollator(locale);
		collator.setStrength(Collator.PRIMARY);
		this.textComparator = collator.nullsLast();
	}
	
	@Override
	protected int compareNotNullObjects(T left, T right) {
		return textComparator.compare(left.get(locale), right.get(locale));
	}
}
