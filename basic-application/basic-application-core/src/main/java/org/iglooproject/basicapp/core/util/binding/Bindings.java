package org.iglooproject.basicapp.core.util.binding;

import org.iglooproject.basicapp.core.business.announcement.model.AnnouncementBinding;
import org.iglooproject.basicapp.core.business.history.model.HistoryDifferenceBinding;
import org.iglooproject.basicapp.core.business.history.model.HistoryLogBinding;
import org.iglooproject.basicapp.core.business.history.search.HistoryLogSearchQueryDataBinding;
import org.iglooproject.basicapp.core.business.referencedata.model.CityBinding;
import org.iglooproject.basicapp.core.business.referencedata.model.IReferenceDataBindingInterfaceBinding;
import org.iglooproject.basicapp.core.business.referencedata.search.CitySearchQueryDataBinding;
import org.iglooproject.basicapp.core.business.referencedata.search.IBasicReferenceDataSearchQueryDataBindingInterfaceBinding;
import org.iglooproject.basicapp.core.business.user.model.UserBinding;
import org.iglooproject.basicapp.core.business.user.model.UserGroupBinding;
import org.iglooproject.basicapp.core.business.user.search.BasicUserSearchQueryDataBinding;
import org.iglooproject.basicapp.core.business.user.search.TechnicalUserSearchQueryDataBinding;
import org.iglooproject.basicapp.core.business.user.search.UserGroupSearchQueryDataBinding;
import org.iglooproject.basicapp.core.business.user.search.UserSearchQueryDataBinding;

public final class Bindings {

	private static final UserBinding USER = new UserBinding();
	private static final UserSearchQueryDataBinding USER_SEARCH_QUERY_DATA = new UserSearchQueryDataBinding();
	private static final BasicUserSearchQueryDataBinding BASIC_USER_SEARCH_QUERY_DATA = new BasicUserSearchQueryDataBinding();
	private static final TechnicalUserSearchQueryDataBinding TECHNICAL_USER_SEARCH_QUERY_DATA = new TechnicalUserSearchQueryDataBinding();

	private static final UserGroupBinding USER_GROUP = new UserGroupBinding();
	private static final UserGroupSearchQueryDataBinding USER_GROUP_SEARCH_QUERY_DATA = new UserGroupSearchQueryDataBinding();

	private static final HistoryLogBinding HISTORY_LOG = new HistoryLogBinding();
	private static final HistoryLogSearchQueryDataBinding HISTORY_LOG_SEARCH_QUERY_DATA = new HistoryLogSearchQueryDataBinding();
	private static final HistoryDifferenceBinding HISTORY_DIFFERENCE = new HistoryDifferenceBinding();

	private static final IReferenceDataBindingInterfaceBinding REFERENCE_DATA = new IReferenceDataBindingInterfaceBinding();
	private static final IBasicReferenceDataSearchQueryDataBindingInterfaceBinding BASIC_REFERENCE_DATA_SEARCH_QUERY_DATA = new IBasicReferenceDataSearchQueryDataBindingInterfaceBinding();
	private static final CityBinding CITY = new CityBinding();
	private static final CitySearchQueryDataBinding CITY_SEARCH_QUERY_DATA = new CitySearchQueryDataBinding();

	private static final AnnouncementBinding ANNOUNCEMENT = new AnnouncementBinding();

	public static UserBinding user() {
		return USER;
	}

	public static UserSearchQueryDataBinding userSearchQueryData() {
		return USER_SEARCH_QUERY_DATA;
	}

	public static BasicUserSearchQueryDataBinding basicUserDtoSearch() {
		return BASIC_USER_SEARCH_QUERY_DATA;
	}

	public static TechnicalUserSearchQueryDataBinding technicalUserDtoSearch() {
		return TECHNICAL_USER_SEARCH_QUERY_DATA;
	}

	public static UserGroupBinding userGroup() {
		return USER_GROUP;
	}

	public static UserGroupSearchQueryDataBinding userGroupSearchQueryData() {
		return USER_GROUP_SEARCH_QUERY_DATA;
	}

	public static HistoryLogBinding historyLog() {
		return HISTORY_LOG;
	}

	public static HistoryLogSearchQueryDataBinding historyLogSearchQueryData() {
		return HISTORY_LOG_SEARCH_QUERY_DATA;
	}

	public static HistoryDifferenceBinding historyDifference() {
		return HISTORY_DIFFERENCE;
	}

	public static IReferenceDataBindingInterfaceBinding referenceData() {
		return REFERENCE_DATA;
	}

	public static IBasicReferenceDataSearchQueryDataBindingInterfaceBinding basicReferenceDataSearchQueryData() {
		return BASIC_REFERENCE_DATA_SEARCH_QUERY_DATA;
	}

	public static CityBinding city() {
		return CITY;
	}

	public static CitySearchQueryDataBinding citySearchQueryData() {
		return CITY_SEARCH_QUERY_DATA;
	}

	public static AnnouncementBinding announcement() {
		return ANNOUNCEMENT;
	}

	private Bindings() {
	}

}
