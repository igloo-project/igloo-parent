package fr.openwide.core.wicket.more.markup.html.feedback;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;


public final class FeedbackUtils {

	public static void refreshFeedback(AjaxRequestTarget target, Page page) {
		target.addChildren(page, AnimatedGlobalFeedbackPanel.class);
	}
	
	private FeedbackUtils() {
	}

}
