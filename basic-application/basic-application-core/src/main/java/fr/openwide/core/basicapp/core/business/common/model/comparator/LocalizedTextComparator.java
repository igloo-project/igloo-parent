package fr.openwide.core.basicapp.core.business.common.model.comparator;

import java.util.Locale;

import fr.openwide.core.basicapp.core.business.common.model.embeddable.LocalizedText;
import fr.openwide.core.jpa.more.business.localization.util.AbstractLocalizedTextComparator;

public class LocalizedTextComparator extends AbstractLocalizedTextComparator<LocalizedText> {

	private static final long serialVersionUID = -1217040817920839219L;

	public LocalizedTextComparator(Locale locale) {
		super(locale);
		}

}
