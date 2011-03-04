package fr.openwide.core.wicket.more.markup.html.feedback;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Panneau de feedback qui contient l'ensemble des feedback relatifs à l'élément
 * « container » passé au constructeur, ou à ses enfants.
 * 
 * Si aucun message ne doit être affiché, le panel est caché.
 * 
 * Seuls les niveaux INFO et ERROR sont gérés.
 *
 */
public class ComponentFeedbackPanel extends Panel {
	private static final long serialVersionUID = 6100876122634391019L;
	
	public ComponentFeedbackPanel(String id, MarkupContainer container) {
		super(id);
		
		FeedbackPanel errorFeedbackPanel = new FeedbackPanel("errorFeedbackPanel", new LevelComponentFeedbackMessageFilter(container, FeedbackMessage.ERROR)) {
			private static final long serialVersionUID = 7521787479719533147L;

			public boolean isVisible() {
				return anyMessage();
			}
		};
		errorFeedbackPanel.setEnabled(true);
		errorFeedbackPanel.setOutputMarkupId(true);
		
		FeedbackPanel successFeedbackPanel = new FeedbackPanel("successFeedbackPanel", new LevelComponentFeedbackMessageFilter(container, FeedbackMessage.INFO)) {
			private static final long serialVersionUID = -3133860085898074341L;

			public boolean isVisible() {
				return anyMessage();
			}
		};
		successFeedbackPanel.setEnabled(true);
		successFeedbackPanel.setOutputMarkupId(true);
		add(errorFeedbackPanel, successFeedbackPanel);
	}

}
