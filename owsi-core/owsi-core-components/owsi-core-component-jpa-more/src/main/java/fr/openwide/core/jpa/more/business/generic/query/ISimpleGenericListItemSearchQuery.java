package fr.openwide.core.jpa.more.business.generic.query;

import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.jpa.more.business.sort.ISort;

public interface ISimpleGenericListItemSearchQuery
		<
		T extends GenericListItem<? super T>,
		S extends ISort<?>
		>
		extends IGenericListItemSearchQuery<T, S, ISimpleGenericListItemSearchQuery<T, S>> {

}
