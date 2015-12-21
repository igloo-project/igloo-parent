package fr.openwide.core.basicapp.core.util.binding;

import fr.openwide.core.basicapp.core.business.history.model.HistoryDifferenceBinding;
import fr.openwide.core.basicapp.core.business.history.model.HistoryLogBinding;
import fr.openwide.core.basicapp.core.business.user.model.UserBinding;
import fr.openwide.core.basicapp.core.business.user.model.UserGroupBinding;

public final class Bindings {

	private static final UserBinding USER = new UserBinding();

	private static final UserGroupBinding USER_GROUP = new UserGroupBinding();

	private static final HistoryLogBinding HISTORY_LOG = new HistoryLogBinding();
	
	private static final HistoryDifferenceBinding HISTORY_DIFFERENCE = new HistoryDifferenceBinding();

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

	private Bindings() {
	}
}
