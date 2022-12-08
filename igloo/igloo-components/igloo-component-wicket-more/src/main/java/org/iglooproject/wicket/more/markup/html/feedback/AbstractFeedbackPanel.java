package org.iglooproject.wicket.more.markup.html.feedback;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import igloo.wicket.feedback.IFeedbackPanel;

public abstract class AbstractFeedbackPanel extends Panel implements IFeedbackPanel {
	
	private static final long serialVersionUID = 8440891357292721078L;
	
	private static final int[] ERROR_MESSAGE_LEVELS = {
		FeedbackMessage.FATAL,
		FeedbackMessage.ERROR,
		FeedbackMessage.WARNING,
		FeedbackMessage.SUCCESS,
		FeedbackMessage.INFO,
		FeedbackMessage.DEBUG,
		FeedbackMessage.UNDEFINED
	};
	
	private static final String[] ERROR_MESSAGE_LEVEL_NAMES = {
		"FATAL",
		"ERROR",
		"WARNING",
		"SUCCESS",
		"INFO",
		"DEBUG",
		"UNDEFINED"
	};
	
	private List<FeedbackPanel> feedbackPanels = new ArrayList<>();

	public AbstractFeedbackPanel(String id, MarkupContainer container) {
		super(id);
		
		int i = 0;
		for (int level: ERROR_MESSAGE_LEVELS) {
			FeedbackPanel f = getFeedbackPanel(ERROR_MESSAGE_LEVEL_NAMES[i] + "feedbackPanel", level, container);
			feedbackPanels.add(f);
			add(f);
			i++;
		}
	}
	
	public abstract FeedbackPanel getFeedbackPanel(String id, int level, MarkupContainer container);
}
