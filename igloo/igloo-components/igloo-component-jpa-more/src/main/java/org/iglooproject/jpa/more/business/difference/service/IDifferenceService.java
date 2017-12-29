package org.iglooproject.jpa.more.business.difference.service;

import org.iglooproject.jpa.more.business.difference.util.IDifferenceFromReferenceGenerator;
import org.iglooproject.jpa.more.business.difference.util.IHistoryDifferenceGenerator;

public interface IDifferenceService<T> extends IHistoryDifferenceGenerator<T> {
	
	/**
	 * @return A difference generator to be used in most cases.
	 */
	IDifferenceFromReferenceGenerator<T> getMainDifferenceGenerator();

	/**
	 * @return A difference generator to use when the diff must be made on part of the properties (for instance for
	 * the creation of an object).
	 */
	IDifferenceFromReferenceGenerator<T> getMinimalDifferenceGenerator();

}
