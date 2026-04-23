package basicapp.front.announcement.renderer;

import basicapp.back.business.announcement.model.Announcement;
import igloo.wicket.renderer.Renderer;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;
import org.apache.wicket.Application;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.lang.Objects;
import org.iglooproject.wicket.more.rendering.EnumRenderer;

public abstract class AnnouncementRenderer extends Renderer<Announcement> {

  private static final long serialVersionUID = 1L;

  private static final Renderer<Announcement> CONTENT =
      new AnnouncementRenderer() {
        private static final long serialVersionUID = 1L;

        @Override
        public String render(Announcement value, Locale locale) {
          if (value.getType() == null) {
            return null;
          }

          switch (value.getType()) {
            case NOTIFICATION:
              return value.getContent().get(locale);
            case UNAVAILABILITY:
              return getUnavailabilityContent(value, locale);
          }

          return null;
        }
      }.nullsAsNull();

  public static Renderer<Announcement> content() {
    return CONTENT;
  }

  private AnnouncementRenderer() {}

  protected String getUnavailabilityContent(Announcement value, Locale locale) {
    IConverter<LocalDate> localDateConverter =
        Application.get().getConverterLocator().getConverter(LocalDate.class);
    IConverter<LocalTime> localTimeConverter =
        Application.get().getConverterLocator().getConverter(LocalTime.class);

    if (Objects.equal(
        value.getUnavailability().getStartDateTime().toLocalDate(),
        value.getUnavailability().getEndDateTime().toLocalDate())) {
      return new StringResourceModel("business.announcement.unavailability.content.sameDay")
          .setParameters(
              EnumRenderer.get().render(value.getType(), locale),
              localDateConverter.convertToString(
                  value.getUnavailability().getStartDateTime().toLocalDate(), locale),
              localTimeConverter.convertToString(
                  value.getUnavailability().getStartDateTime().toLocalTime(), locale),
              localTimeConverter.convertToString(
                  value.getUnavailability().getEndDateTime().toLocalTime(), locale))
          .getString();
    } else {
      return new StringResourceModel("business.announcement.unavailability.content")
          .setParameters(
              EnumRenderer.get().render(value.getType(), locale),
              localDateConverter.convertToString(
                  value.getUnavailability().getStartDateTime().toLocalDate(), locale),
              localTimeConverter.convertToString(
                  value.getUnavailability().getStartDateTime().toLocalTime(), locale),
              localDateConverter.convertToString(
                  value.getUnavailability().getEndDateTime().toLocalDate(), locale),
              localTimeConverter.convertToString(
                  value.getUnavailability().getEndDateTime().toLocalTime(), locale))
          .getString();
    }
  }
}
