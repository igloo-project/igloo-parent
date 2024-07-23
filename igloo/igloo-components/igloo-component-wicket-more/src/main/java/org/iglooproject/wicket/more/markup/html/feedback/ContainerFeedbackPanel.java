package org.iglooproject.wicket.more.markup.html.feedback;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

/**
 * Panneau de feedback qui contient l'ensemble des feedback relatifs à l'élément « container » passé
 * au constructeur, ou à ses enfants.
 *
 * <p>Si aucun message ne doit être affiché, le panel est caché.
 */
public class ContainerFeedbackPanel extends AbstractFeedbackPanel {
  private static final long serialVersionUID = 6100876122634391019L;

  public ContainerFeedbackPanel(String id, MarkupContainer component) {
    super(id, component);
  }

  @Override
  public FeedbackPanel getFeedbackPanel(String id, int level, MarkupContainer container) {
    return new ErrorLevelContainerFeedbackPanel(id, level, container);
  }
}
