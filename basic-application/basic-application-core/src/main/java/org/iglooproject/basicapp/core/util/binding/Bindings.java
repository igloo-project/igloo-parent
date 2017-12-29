package org.iglooproject.basicapp.core.util.binding;

import org.iglooproject.basicapp.core.business.history.model.HistoryDifferenceBinding;
import org.iglooproject.basicapp.core.business.history.model.HistoryLogBinding;
import org.iglooproject.basicapp.core.business.referencedata.model.CityBinding;
import org.iglooproject.basicapp.core.business.user.model.UserBinding;
import org.iglooproject.basicapp.core.business.user.model.UserGroupBinding;
import org.iglooproject.jpa.more.business.generic.model.IGenericListItemBindingInterfaceBinding;

public final class Bindings {

	private static final UserBinding USER = new UserBinding();

	private static final UserGroupBinding USER_GROUP = new UserGroupBinding();

	private static final HistoryLogBinding HISTORY_LOG = new HistoryLogBinding();
	
	private static final HistoryDifferenceBinding HISTORY_DIFFERENCE = new HistoryDifferenceBinding();
	
	private static final CityBinding CITY = new CityBinding();
	
	private static final IGenericListItemBindingInterfaceBinding GENERIC_LIST_ITEM =
			new IGenericListItemBindingInterfaceBinding();

	public static IGenericListItemBindingInterfaceBinding genericListItem() {
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
