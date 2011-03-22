package fr.openwide.core.wicket.more.markup.html.feedback;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

public class GlobalFeedbackPanel extends Panel {
	private static final long serialVersionUID = 8440891357292721078L;
	private static final int[] ERROR_MESSAGE_LEVELS = {
		FeedbackMessage.FATAL
		, FeedbackMessage.ERROR
		, FeedbackMessage.WARNING
		, FeedbackMessage.INFO
		, FeedbackMessage.DEBUG
		, FeedbackMessage.UNDEFINED
	};
	
	private static final String[] ERROR_MESSAGE_LEVEL_NAMES = {
		"FATAL"
		, "ERROR"
		, "WARNING"
		, "INFO"
		, "DEBUG"
		, "UNDEFINED"
	};
	
	private List<FeedbackPanel> feedbackPanels = new ArrayList<FeedbackPanel>();

	public GlobalFeedbackPanel(String id) {
		super(id);
		
		int i = 0;
		for(int level: ERROR_MESSAGE_LEVELS) {
			FeedbackPanel f = new ErrorLevelFeedbackComponent(
					ERROR_MESSAGE_LEVEL_NAMES[i] + "feedbackPanel"
					, level
			);
			feedbackPanels.add(f);
			add(f);
			i++;
		}
	}
	
	@Override
	public boolean isVisible() {
		for(FeedbackPanel f:feedbackPanels) {
			if(f.isVisible()) {
				return true;
			}
		}
		return false;
	}
	
	protected class ErrorLevelFeedbackComponent extends FeedbackPanel {
		private static final long serialVersionUID = -731818705093875451L;
		protected int level;

		public ErrorLevelFeedbackComponent(String id, int level) {
			super(id, new ExactErrorLevelFeedbackMessageFilter(level));
			this.level = level;
		}
		
		@Override
		public boolean isVisible() {
			return anyMessage(level);
		}
		
	}
	
	protected static class ExactErrorLevelFeedbackMessageFilter implements IFeedbackMessageFilter {
		private static final long serialVersionUID = 8546167520348581368L;
		private final int errorLevel;

		public ExactErrorLevelFeedbackMessageFilter(int errorLevel) {
			this.errorLevel = errorLevel;
		}

		public boolean accept(FeedbackMessage message) {
			return message.getLevel() == errorLevel;
		}
	}
}
