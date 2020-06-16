package org.iglooproject.basicapp.core.business.referencedata.model.comparator;

import java.util.Objects;

import org.hibernate.Hibernate;
import org.iglooproject.basicapp.core.business.common.model.comparator.LocalizedTextComparator;
import org.iglooproject.basicapp.core.business.common.util.BasicApplicationLocale;
import org.iglooproject.basicapp.core.business.referencedata.model.ReferenceData;
import org.iglooproject.jpa.business.generic.util.AbstractGenericEntityComparator;

import com.google.common.collect.ComparisonChain;

public class ReferenceDataComparator extends AbstractGenericEntityComparator<Long, ReferenceData<?>>  {

	private static final long serialVersionUID = 1L;

	private static final ReferenceDataComparator INSTANCE = new ReferenceDataComparator();

	@Override
	protected int compareNotNullObjects(ReferenceData<?> left, ReferenceData<?> right) {
		ComparisonChain comparaisonChain = ComparisonChain.start();
		
		if (!Objects.equals(Hibernate.getClass(left), Hibernate.getClass(right))) {
			comparaisonChain = comparaisonChain
				.compare(
					Hibernate.getClass(left).getSimpleName(),
					Hibernate.getClass(right).getSimpleName(),
					BasicApplicationLocale.comparator()
				);
		}
		
		int order = comparaisonChain
			.compare(left.getPosition(), right.getPosition())
			.compare(left.getLabel(), right.getLabel(), LocalizedTextComparator.get())
			.result();
		
		if (order == 0) {
			order = super.compareNotNullObjects(left, right);
		}
		
		return order;
	}

	public static final ReferenceDataComparator get() {
		return INSTANCE;
	}

}
