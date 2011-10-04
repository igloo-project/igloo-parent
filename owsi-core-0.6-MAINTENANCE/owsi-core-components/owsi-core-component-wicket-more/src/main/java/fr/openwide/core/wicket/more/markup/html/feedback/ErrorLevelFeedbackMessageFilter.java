package fr.openwide.core.wicket.more.markup.html.feedback;

import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;

class ErrorLevelFeedbackMessageFilter implements IFeedbackMessageFilter {

	private static final long serialVersionUID = 8546167520348581368L;
	private final int errorLevel;

	public ErrorLevelFeedbackMessageFilter(int errorLevel) {
		this.errorLevel = errorLevel;
	}

	public boolean accept(FeedbackMessage message) {
		return message.getLevel() == errorLevel;
	}

}
