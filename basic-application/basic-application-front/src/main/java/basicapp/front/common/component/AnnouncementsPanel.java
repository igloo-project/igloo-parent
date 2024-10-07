package basicapp.front.common.component;

import basicapp.back.business.announcement.model.Announcement;
import basicapp.back.business.announcement.service.controller.IAnnouncementControllerService;
import basicapp.back.business.user.model.User;
import basicapp.back.business.user.service.controller.IUserControllerService;
import basicapp.front.BasicApplicationSession;
import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.model.Detachables;
import java.util.List;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.ajax.AjaxListeners;
import org.iglooproject.wicket.more.markup.repeater.collection.CollectionView;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnouncementsPanel extends Panel {

  private static final long serialVersionUID = 1L;

  private static final Logger LOGGER = LoggerFactory.getLogger(AnnouncementsPanel.class);

  @SpringBean private IAnnouncementControllerService announcementControllerService;

  @SpringBean private IUserControllerService userControllerService;

  private final IModel<List<Announcement>> announcementsModel =
      LoadableDetachableModel.of(() -> announcementControllerService.listEnabled());

  private final IModel<Boolean> openModel = Model.of(Boolean.TRUE);

  public AnnouncementsPanel(String id) {
    super(id);
    setOutputMarkupId(true);

    Condition openCondition = Condition.isTrue(openModel);

    add(
        new EnclosureContainer("container")
            .condition(openCondition)
            .add(
                new CollectionView<>(
                    "announcements", announcementsModel, GenericEntityModel.factory()) {
                  private static final long serialVersionUID = 1L;

                  @Override
                  protected void populateItem(Item<Announcement> item) {
                    item.add(new AnnouncementMessagePanel("message", item.getModel()));
                  }
                },
                new AjaxLink<Void>("close") {
                  private static final long serialVersionUID = 1L;

                  @Override
                  public void onClick(AjaxRequestTarget target) {
                    try {
                      openModel.setObject(Boolean.FALSE);

                      User user = BasicApplicationSession.get().getUser();
                      userControllerService.closeAnnouncement(user);

                      AjaxListeners.add(target, AjaxListeners.refresh(AnnouncementsPanel.this));
                    } catch (Exception e) {
                      LOGGER.error("Error on update user announcement information.", e);
                      Session.get().error(getString("common.error.unexpected"));
                    }
                  }
                }),
        new AjaxLink<Void>("open") {
          private static final long serialVersionUID = 1L;

          @Override
          public void onClick(AjaxRequestTarget target) {
            try {
              openModel.setObject(Boolean.TRUE);

              User user = BasicApplicationSession.get().getUser();
              userControllerService.openAnnouncement(user);

              AjaxListeners.add(target, AjaxListeners.refresh(AnnouncementsPanel.this));
            } catch (Exception e) {
              LOGGER.error("Error on update user announcement information.", e);
              Session.get().error(getString("common.error.unexpected"));
            }
          }
        }.add(openCondition.thenHide()));

    add(
        new ClassAttributeAppender(
            Condition.isFalse(openModel).then("header-alert-section-dismissed").otherwise("")));
  }

  @Override
  protected void onConfigure() {
    super.onConfigure();

    if (announcementsModel.getObject().isEmpty()) {
      setVisible(false);
      openModel.setObject(Boolean.FALSE);
      return;
    }

    setVisible(true);

    openModel.setObject(announcementControllerService.isOpen());
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(announcementsModel, openModel);
  }
}
