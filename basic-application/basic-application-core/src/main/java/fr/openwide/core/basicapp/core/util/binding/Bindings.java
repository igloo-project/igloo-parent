package fr.openwide.core.basicapp.core.util.binding;

import fr.openwide.core.basicapp.core.business.history.model.HistoryDifferenceBinding;
import fr.openwide.core.basicapp.core.business.history.model.HistoryLogBinding;
import fr.openwide.core.basicapp.core.business.referential.model.CityBinding;
import fr.openwide.core.basicapp.core.business.user.model.UserBinding;
import fr.openwide.core.basicapp.core.business.user.model.UserGroupBinding;
import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.jpa.more.business.generic.model.GenericListItemBinding;

public final class Bindings {

	private static final GenericListItemBinding<GenericListItem<?>> GENERIC_LIST_ITEM = new GenericListItemBinding<>();
	
	private static final UserBinding USER = new UserBinding();

	private static final UserGroupBinding USER_GROUP = new UserGroupBinding();

	private static final HistoryLogBinding HISTORY_LOG = new HistoryLogBinding();
	
	private static final HistoryDifferenceBinding HISTORY_DIFFERENCE = new HistoryDifferenceBinding();
	
	private static final CityBinding CITY = new CityBinding();

	public static GenericListItemBinding<GenericListItem<?>> genericListItem() {
		return GENERIC_LIST_ITEM;
	}
	
	public static UserBinding user() {
		return USER;
	}

	public static UserGroupBinding userGroup() {
		return USER_GROUP;
	}

	public static HistoryLogBinding historyLog() {
		return HISTORY_LOG;
	}
	
	public static HistoryDifferenceBinding historyDifference() {
		return HISTORY_DIFFERENCE;
	}
	
	public static CityBinding city() {
		return CITY;
	}

	private Bindings() {
	}
}
