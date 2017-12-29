package org.iglooproject.jpa.more.business.generic.query;

import org.iglooproject.jpa.more.business.generic.model.GenericListItem;
import org.iglooproject.jpa.more.business.sort.ISort;

public interface ISimpleGenericListItemSearchQuery
		<
		T extends GenericListItem<? super T>,
		S extends ISort<?>
		>
		extends IGenericListItemSearchQuery<T, S, ISimpleGenericListItemSearchQuery<T, S>> {

}
