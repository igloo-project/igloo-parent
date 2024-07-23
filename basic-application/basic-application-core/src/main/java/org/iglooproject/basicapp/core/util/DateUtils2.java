package org.iglooproject.basicapp.core.util;

import java.util.Calendar;
import java.util.Date;
import org.iglooproject.commons.util.CloneUtils;

public final class DateUtils2 {

  public static Date addTime(final Date date, final Date time) {
    if (date == null || time == null) {
      return null;
    }

    Calendar calTime = Calendar.getInstance();
    calTime.setTime(time);

    Calendar calDate = Calendar.getInstance();
    calDate.setTime(date);

    calDate.set(Calendar.HOUR_OF_DAY, calTime.get(Calendar.HOUR_OF_DAY));
    calDate.set(Calendar.MINUTE, calTime.get(Calendar.MINUTE));

    return CloneUtils.clone(calDate.getTime());
  }

  private DateUtils2() {}
}
