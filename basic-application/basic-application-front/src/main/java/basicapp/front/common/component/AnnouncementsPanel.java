package basicapp.front.common.component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

import basicapp.back.business.announcement.model.Announcement;
import basicapp.back.business.announcement.service.IAnnouncementService;
import basicapp.back.business.user.model.User;
import basicapp.back.business.user.service.IUserService;
import basicapp.front.BasicApplicationSession;
import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.model.Detachables;

public class AnnouncementsPanel extends Panel {

	private static final long serialVersionUID = 9097065669590214330L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AnnouncementsPanel.class);

	@SpringBean
	private IAnnouncementService announcementService;

	@SpringBean
	private IUserService userService;

	private final IModel<List<Announcement>> announcementsModel = new LoadableDetachableModel<List<Announcement>>() {
		private static final long serialVersionUID = 1L;
		@Override
		protected List<Announcement> load() {
			return announcementService.listEnabled();
		}
	};

	private final IModel<Boolean> closeModel = Model.of(Boolean.FALSE);

	public AnnouncementsPanel(String id) {
		super(id);
		setOutputMarkupId(true);
		
		Condition openCondition = Condition.isFalse(closeModel);
		
		add(
			new EnclosureContainer("container")
				.condition(openCondition)
				.add(
					new CollectionView<Announcement>( "announcements", announcementsModel, GenericEntityModel.factory()) {
						private static final long serialVersionUID = 1L;
						@Override
						protected void populateItem(Item<Announcement> item) {
							item.add(
								new AnnouncementMessagePanel("message", item.getModel())
							);
						}
					},
					new AjaxLink<Void>("close") {
						private static final long serialVersionUID = 1L;
						@Override
						public void onClick(AjaxRequestTarget target) {
							try {
								closeModel.setObject(Boolean.TRUE);
								
								User user = BasicApplicationSession.get().getUser();
								user.getAnnouncementInformation().setLastActionDate(Instant.now());
								user.getAnnouncementInformation().setOpen(!closeModel.getObject());
								userService.update(user);
								
								AjaxListeners.add(target, AjaxListeners.refresh(AnnouncementsPanel.this));
							} catch (Exception e) {
								LOGGER.error("Error on update user announcement information.", e);
								Session.get().error(getString("common.error.unexpected"));
							}
						}
					}
				),
			new AjaxLink<Void>("open") {
				private static final long serialVersionUID = 1L;
				@Override
				public void onClick(AjaxRequestTarget target) {
					try {
						closeModel.setObject(Boolean.FALSE);
						
						User user = BasicApplicationSession.get().getUser();
						user.getAnnouncementInformation().setLastActionDate(Instant.now());
						user.getAnnouncementInformation().setOpen(!closeModel.getObject());
						userService.update(user);
						
						AjaxListeners.add(target, AjaxListeners.refresh(AnnouncementsPanel.this));
					} catch (Exception e) {
						LOGGER.error("Error on update user announcement information.", e);
						Session.get().error(getString("common.error.unexpected"));
					}
				}
			}
				.add(openCondition.thenHide())
		);
		
		add(
			new ClassAttributeAppender(Condition.isTrue(closeModel).then("header-alert-section-dismissed").otherwise(""))
		);
	}

	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		if (announcementsModel.getObject().isEmpty()) {
			setVisible(false);
			closeModel.setObject(Boolean.TRUE);
			return;
		}
		
		setVisible(true);
		
		Instant lastActionDate = BasicApplicationSession.get().getUser().getAnnouncementInformation().getLastActionDate();
		LocalDateTime mostRecentPublicationStartDate = announcementService.getMostRecentPublicationStartDate();
		
		if (lastActionDate == null || LocalDateTime.ofInstant(lastActionDate, ZoneId.systemDefault()).isBefore(mostRecentPublicationStartDate)) {
			closeModel.setObject(Boolean.FALSE);
		} else {
			closeModel.setObject(!BasicApplicationSession.get().getUser().getAnnouncementInformation().isOpen());
		}
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(
			announcementsModel,
			closeModel
		);
	}

}