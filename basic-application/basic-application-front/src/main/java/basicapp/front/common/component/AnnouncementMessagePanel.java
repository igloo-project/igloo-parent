package basicapp.front.common.component;

import basicapp.back.business.announcement.model.Announcement;
import basicapp.back.business.announcement.model.atomic.AnnouncementType;
import basicapp.back.business.announcement.predicate.AnnouncementPredicates;
import basicapp.front.announcement.renderer.AnnouncementRenderer;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

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
