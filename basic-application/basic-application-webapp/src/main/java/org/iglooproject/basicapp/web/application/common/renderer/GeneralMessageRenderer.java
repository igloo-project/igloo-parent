package org.iglooproject.basicapp.web.application.common.renderer;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.wicket.model.StringResourceModel;
import org.iglooproject.basicapp.core.business.message.model.GeneralMessage;
import org.iglooproject.basicapp.core.business.message.model.atomic.GeneralMessageType;
import org.iglooproject.basicapp.web.application.BasicApplicationSession;
import org.iglooproject.functional.Joiners;
import org.iglooproject.wicket.more.rendering.EnumRenderer;
import org.iglooproject.wicket.more.rendering.Renderer;
import org.iglooproject.wicket.more.util.DatePattern;

public abstract class GeneralMessageRenderer extends Renderer<GeneralMessage> {

	private static final long serialVersionUID = 5707691630314666729L;

	private static final Renderer<GeneralMessage> INSTANCE = new GeneralMessageRenderer() {
		private static final long serialVersionUID = 1L;
		@Override
		public String render(GeneralMessage value, Locale locale) {
			String title = value.getTitle().get(BasicApplicationSession.get().getLocale());
			String description = value.getDescription().get(BasicApplicationSession.get().getLocale());
			
			if (GeneralMessageType.SERVICE_INTERRUPTION.equals(value.getType())) {
				title = EnumRenderer.get().render(GeneralMessageType.SERVICE_INTERRUPTION, locale);
				description = getInterruptionDescription(value.getInterruption().getStartDateTime(), value.getInterruption().getEndDateTime());
			}
			
			return Joiners.onMiddotSpace().join(title, description);
		}
	}.nullsAsNull();

	public static Renderer<GeneralMessage> get() {
		return INSTANCE;
	}

	private GeneralMessageRenderer() {
	}

	protected String getInterruptionDescription(Date startDate, Date endDate) {
		Locale locale = BasicApplicationSession.get().getLocale();
		
		String interruptionStartDate = Renderer.fromDatePattern(DatePattern.SHORT_DATE).render(startDate, locale);
		String interruptionEndDate = Renderer.fromDatePattern(DatePattern.SHORT_DATE).render(endDate, locale);
		String interruptionStartTime = Renderer.fromDatePattern(DatePattern.TIME).render(startDate, locale);
		String interruptionEndTime = Renderer.fromDatePattern(DatePattern.TIME).render(endDate, locale);
		
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		
		if (startCal.get(Calendar.DAY_OF_WEEK) == endCal.get(Calendar.DAY_OF_WEEK)) {
			return new StringResourceModel("business.generalMessage.interruption.message.window.sameDay")
				.setParameters(interruptionStartDate, interruptionStartTime, interruptionEndTime)
				.getString();
		} else {
			return new StringResourceModel("business.generalMessage.interruption.message.window")
				.setParameters(interruptionStartDate, interruptionStartTime, interruptionEndDate, interruptionEndTime)
				.getString();
		}
	}

}
