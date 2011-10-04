package fr.openwide.core.wicket.more.markup.html.feedback;

import org.apache.wicket.markup.html.panel.FeedbackPanel;

class ErrorLevelFeedbackPanel extends FeedbackPanel {
	
	private static final long serialVersionUID = -731818705093875451L;
	protected int level;

	public ErrorLevelFeedbackPanel(String id, int level) {
		super(id, new ErrorLevelFeedbackMessageFilter(level));
		this.level = level;
	}
	
	@Override
	public boolean isVisible() {
		return anyMessage(level);
	}
}
