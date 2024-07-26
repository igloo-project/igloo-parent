package org.iglooproject.basicapp.core.util.binding;

import org.iglooproject.basicapp.core.business.announcement.model.AnnouncementBinding;
import org.iglooproject.basicapp.core.business.history.model.HistoryDifferenceBinding;
import org.iglooproject.basicapp.core.business.history.model.HistoryLogBinding;
import org.iglooproject.basicapp.core.business.referencedata.model.CityBinding;
import org.iglooproject.basicapp.core.business.referencedata.model.IReferenceDataBindingInterfaceBinding;
import org.iglooproject.basicapp.core.business.user.model.UserBinding;
import org.iglooproject.basicapp.core.business.user.model.UserGroupBinding;

public final class Bindings {

  private static final UserBinding USER = new UserBinding();

  private static final UserGroupBinding USER_GROUP = new UserGroupBinding();

  private static final HistoryLogBinding HISTORY_LOG = new HistoryLogBinding();

  private static final HistoryDifferenceBinding HISTORY_DIFFERENCE = new HistoryDifferenceBinding();

  private static final IReferenceDataBindingInterfaceBinding REFERENCE_DATA =
      new IReferenceDataBindingInterfaceBinding();
  private static final CityBinding CITY = new CityBinding();

  private static final AnnouncementBinding ANNOUNCEMENT = new AnnouncementBinding();

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

  public static IReferenceDataBindingInterfaceBinding referenceData() {
    return REFERENCE_DATA;
  }

  public static CityBinding city() {
    return CITY;
  }

  public static AnnouncementBinding announcement() {
    return ANNOUNCEMENT;
  }

  private Bindings() {}
}
