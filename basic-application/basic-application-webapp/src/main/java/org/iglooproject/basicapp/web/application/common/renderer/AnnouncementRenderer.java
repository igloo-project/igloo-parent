package org.iglooproject.basicapp.web.application.common.renderer;

import igloo.wicket.renderer.Renderer;
import igloo.wicket.util.DatePattern;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.apache.wicket.model.StringResourceModel;
import org.iglooproject.basicapp.core.business.announcement.model.Announcement;
import org.iglooproject.basicapp.web.application.BasicApplicationSession;
import org.iglooproject.wicket.more.rendering.EnumRenderer;

public abstract class AnnouncementRenderer extends Renderer<Announcement> {

  private static final long serialVersionUID = 5707691630314666729L;

  private static final Renderer<Announcement> TITLE =
      new AnnouncementRenderer() {
        private static final long serialVersionUID = 1L;

        @Override
        public String render(Announcement value, Locale locale) {
          if (value.getType() == null) {
            return null;
          }

          switch (value.getType()) {
            case SERVICE_INTERRUPTION:
              return EnumRenderer.get().render(value.getType(), locale);
            case OTHER:
              return value.getTitle().get(locale);
          }

          return null;
        }
      }.nullsAsNull();

  private static final Renderer<Announcement> DESCRIPTION =
      new AnnouncementRenderer() {
        private static final long serialVersionUID = 1L;

        @Override
        public String render(Announcement value, Locale locale) {
          if (value.getType() == null) {
            return null;
          }

          switch (value.getType()) {
            case SERVICE_INTERRUPTION:
              return getInterruptionDescription(
                  value.getInterruption().getStartDateTime(),
                  value.getInterruption().getEndDateTime());
            case OTHER:
              return value.getDescription().get(locale);
          }

          return null;
        }
      }.nullsAsNull();

  public static Renderer<Announcement> title() {
    return TITLE;
  }

  public static Renderer<Announcement> description() {
    return DESCRIPTION;
  }

  private AnnouncementRenderer() {}

  protected String getInterruptionDescription(Date startDate, Date endDate) {
    Locale locale = BasicApplicationSession.get().getLocale();

    String interruptionStartDate =
        Renderer.fromDatePattern(DatePattern.SHORT_DATE).render(startDate, locale);
    String interruptionEndDate =
        Renderer.fromDatePattern(DatePattern.SHORT_DATE).render(endDate, locale);
    String interruptionStartTime =
        Renderer.fromDatePattern(DatePattern.TIME).render(startDate, locale);
    String interruptionEndTime = Renderer.fromDatePattern(DatePattern.TIME).render(endDate, locale);

    Calendar startCal = Calendar.getInstance();
    startCal.setTime(startDate);
    Calendar endCal = Calendar.getInstance();
    endCal.setTime(endDate);

    if (startCal.get(Calendar.DAY_OF_WEEK) == endCal.get(Calendar.DAY_OF_WEEK)) {
      return new StringResourceModel("business.announcement.interruption.message.window.sameDay")
          .setParameters(interruptionStartDate, interruptionStartTime, interruptionEndTime)
          .getString();
    } else {
      return new StringResourceModel("business.announcement.interruption.message.window")
          .setParameters(
              interruptionStartDate,
              interruptionStartTime,
              interruptionEndDate,
              interruptionEndTime)
          .getString();
    }
  }
}
