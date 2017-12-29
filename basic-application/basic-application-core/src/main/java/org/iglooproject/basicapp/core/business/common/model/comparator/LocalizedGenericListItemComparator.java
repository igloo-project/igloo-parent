package org.iglooproject.basicapp.core.business.common.model.comparator;

import java.util.Comparator;
import java.util.Locale;

import com.google.common.collect.Ordering;

import org.iglooproject.basicapp.core.business.common.model.LocalizedGenericListItem;
import org.iglooproject.basicapp.core.business.common.model.embeddable.LocalizedText;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.util.AbstractGenericEntityComparator;

public class LocalizedGenericListItemComparator extends AbstractGenericEntityComparator<Long, LocalizedGenericListItem<?>> {

	private static final long serialVersionUID = -6024767658595096844L;
	
	private static final Ordering<Integer> DEFAULT_POSITION_COMPARATOR = Ordering.natural().nullsLast();
	
	private final Comparator<? super Integer> positionComparator;
	private final Comparator<? super String> codeComparator;
	private final Comparator<? super LocalizedText> labelComparator;

	public LocalizedGenericListItemComparator(Locale locale) {
		super();
		positionComparator = DEFAULT_POSITION_COMPARATOR;
		codeComparator = GenericEntity.DEFAULT_STRING_COLLATOR;
		labelComparator = new LocalizedTextComparator(locale);
	}
	
	@Override
	protected int compareNotNullObjects(LocalizedGenericListItem<?> left,LocalizedGenericListItem<?>  right) {
		// on trie en priorité sur la position, puis sur le libellé et enfin par l'identifiant
		int order = positionComparator.compare(left.getPosition(), right.getPosition());
		
		if (order == 0) {
			order = codeComparator.compare(left.getCode(), right.getCode());
		}
		
		if (order == 0) {
			order = labelComparator.compare(left.getLabel(), right.getLabel());
		}
		
		if (order == 0) {
			order = super.compareNotNullObjects(left, right);
		}
		
		return order;
	}
	
}
