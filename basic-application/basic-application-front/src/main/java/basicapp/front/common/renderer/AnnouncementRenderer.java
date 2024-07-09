package basicapp.front.common.renderer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;

import org.apache.wicket.Application;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.lang.Objects;
import org.iglooproject.wicket.more.rendering.EnumRenderer;

import basicapp.back.business.announcement.model.Announcement;
import igloo.wicket.renderer.Renderer;

public abstract class AnnouncementRenderer extends Renderer<Announcement> {

	private static final long serialVersionUID = 5707691630314666729L;

	private static final Renderer<Announcement> TITLE = new AnnouncementRenderer() {
		private static final long serialVersionUID = 1L;
		@Override
		public String render(Announcement value, Locale locale) {
			if (value.getType() == null) {
				return null;
			}
			
			switch(value.getType()) {
			case SERVICE_INTERRUPTION:
				return EnumRenderer.get().render(value.getType(), locale);
			case OTHER:
				return value.getTitle().get(locale);
			}
			
			return null;
		}
	}.nullsAsNull();

	private static final Renderer<Announcement> DESCRIPTION = new AnnouncementRenderer() {
		private static final long serialVersionUID = 1L;
		@Override
		public String render(Announcement value, Locale locale) {
			if (value.getType() == null) {
				return null;
			}
			
			switch(value.getType()) {
			case SERVICE_INTERRUPTION:
				return getInterruptionDescription(value.getInterruption().getStartDateTime(), value.getInterruption().getEndDateTime(), locale);
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

	private AnnouncementRenderer() {
	}

	protected String getInterruptionDescription(LocalDateTime startDate, LocalDateTime endDate, Locale locale) {
		IConverter<LocalDate> localDateConverter = Application.get().getConverterLocator().getConverter(LocalDate.class);
		IConverter<LocalTime> localTimeConverter = Application.get().getConverterLocator().getConverter(LocalTime.class);
		
		if (Objects.equal(startDate.toLocalDate(), endDate.toLocalDate())) {
			return new StringResourceModel("business.announcement.interruption.message.window.sameDay")
				.setParameters(
					localDateConverter.convertToString(startDate.toLocalDate(), locale),
					localTimeConverter.convertToString(startDate.toLocalTime(), locale),
					localTimeConverter.convertToString(endDate.toLocalTime(), locale)
				)
				.getString();
		} else {
			return new StringResourceModel("business.announcement.interruption.message.window")
				.setParameters(
					localDateConverter.convertToString(startDate.toLocalDate(), locale),
					localTimeConverter.convertToString(startDate.toLocalTime(), locale),
					localDateConverter.convertToString(endDate.toLocalDate(), locale),
					localTimeConverter.convertToString(endDate.toLocalTime(), locale)
				)
				.getString();
		}
	}

}
