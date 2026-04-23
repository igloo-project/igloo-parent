package basicapp.front.common.component;

import basicapp.back.business.announcement.model.Announcement;
import basicapp.front.announcement.renderer.AnnouncementRenderer;
import basicapp.front.common.commonmark.component.CommonMarkLabel;
import igloo.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

public class AnnouncementMessagePanel extends GenericPanel<Announcement> {

  private static final long serialVersionUID = 7392973418033689115L;

  public AnnouncementMessagePanel(String id, IModel<Announcement> announcementModel) {
    super(id, announcementModel);
    setOutputMarkupId(true);

    add(new CommonMarkLabel("content", AnnouncementRenderer.content().asModel(announcementModel)));
  }
}
