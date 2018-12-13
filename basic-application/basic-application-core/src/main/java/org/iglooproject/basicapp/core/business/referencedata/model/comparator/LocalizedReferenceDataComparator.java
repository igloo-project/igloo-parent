package org.iglooproject.basicapp.core.business.referencedata.model.comparator;

import org.hibernate.Hibernate;
import org.iglooproject.basicapp.core.business.common.model.comparator.LocalizedTextComparator;
import org.iglooproject.basicapp.core.business.common.util.BasicApplicationLocale;
import org.iglooproject.basicapp.core.business.referencedata.model.LocalizedReferenceData;
import org.iglooproject.jpa.business.generic.util.AbstractGenericEntityComparator;

import com.google.common.collect.ComparisonChain;

public class LocalizedReferenceDataComparator extends AbstractGenericEntityComparator<Long, LocalizedReferenceData<?>>  {

	private static final long serialVersionUID = 1L;

	private static final LocalizedReferenceDataComparator INSTANCE = new LocalizedReferenceDataComparator();

	@Override
	protected int compareNotNullObjects(LocalizedReferenceData<?> left, LocalizedReferenceData<?> right) {
		ComparisonChain comparaisonChain = ComparisonChain.start();
		
		if (!Hibernate.getClass(left).equals(Hibernate.getClass(right))) {
			comparaisonChain
				.compare(
					Hibernate.getClass(left).getSimpleName(),
					Hibernate.getClass(right).getSimpleName(),
					BasicApplicationLocale.comparator()
				);
		}
		
		comparaisonChain
			.compare(left.getPosition(), right.getPosition())
			.compare(left.getLabel(), right.getLabel(), LocalizedTextComparator.get());
		
		int order = comparaisonChain.result();
		
		if (order == 0) {
			order = super.compareNotNullObjects(left, right);
		}
		
		return order;
	}

	public static final LocalizedReferenceDataComparator get() {
		return INSTANCE;
	}

}
