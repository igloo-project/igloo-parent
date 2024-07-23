package org.iglooproject.basicapp.web.application.common.component;

import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.iglooproject.basicapp.core.business.announcement.model.Announcement;
import org.iglooproject.basicapp.core.business.announcement.model.atomic.AnnouncementType;
import org.iglooproject.basicapp.core.business.announcement.predicate.AnnouncementPredicates;
import org.iglooproject.basicapp.web.application.common.renderer.AnnouncementRenderer;

public class AnnouncementMessagePanel extends GenericPanel<Announcement> {

  private static final long serialVersionUID = 7392973418033689115L;

  public AnnouncementMessagePanel(String id, IModel<Announcement> announcementModel) {
    super(id, announcementModel);
    setOutputMarkupId(true);

    add(
        new EnclosureContainer("serviceInterruptionContainer")
            .condition(
                Condition.predicate(
                    announcementModel,
                    AnnouncementPredicates.type(AnnouncementType.SERVICE_INTERRUPTION)))
            .add(
                new CoreLabel("title", AnnouncementRenderer.title().asModel(announcementModel)),
                new CoreLabel(
                        "description",
                        AnnouncementRenderer.description().asModel(announcementModel))
                    .hideIfEmpty()),
        new EnclosureContainer("otherContainer")
            .condition(
                Condition.predicate(
                        announcementModel,
                        AnnouncementPredicates.type(AnnouncementType.SERVICE_INTERRUPTION))
                    .negate())
            .add(
                new CoreLabel("title", AnnouncementRenderer.title().asModel(announcementModel)),
                new CoreLabel(
                        "description",
                        AnnouncementRenderer.description().asModel(announcementModel))
                    .multiline()
                    .hideIfEmpty()));
  }
}
