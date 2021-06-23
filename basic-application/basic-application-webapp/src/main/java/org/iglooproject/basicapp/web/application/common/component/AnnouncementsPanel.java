package org.iglooproject.basicapp.web.application.common.component;

import java.util.Date;
import java.util.List;

import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.core.business.announcement.model.Announcement;
import org.iglooproject.basicapp.core.business.announcement.service.IAnnouncementService;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.service.IUserService;
import org.iglooproject.basicapp.web.application.BasicApplicationSession;
import org.iglooproject.wicket.behavior.ClassAttributeAppender;
import org.iglooproject.wicket.more.ajax.AjaxListeners;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.html.basic.EnclosureContainer;
import org.iglooproject.wicket.more.markup.repeater.collection.CollectionView;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.iglooproject.wicket.more.util.model.Detachables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		
		add(
			new AjaxLink<Void>("main") {
				private static final long serialVersionUID = 1L;
				@Override
				public void onClick(AjaxRequestTarget target) {
					try {
						closeModel.setObject(!closeModel.getObject());
						
						User user = BasicApplicationSession.get().getUser();
						user.getAnnouncementInformation().setLastActionDate(new Date());
						user.getAnnouncementInformation().setOpen(!closeModel.getObject());
						userService.update(user);
					} catch (Exception e) {
						LOGGER.error("Error occured while saving user", e);
						Session.get().error(getString("common.error.unexpected"));
					}
					AjaxListeners.add(target, AjaxListeners.refresh(AnnouncementsPanel.this));
				}
			}
				.add(
					new EnclosureContainer("container")
						.condition(Condition.isFalse(closeModel))
						.add(
							new CollectionView<Announcement>( "announcements", announcementsModel, GenericEntityModel.factory()) {
								private static final long serialVersionUID = 1L;
								@Override
								protected void populateItem(Item<Announcement> item) {
									item.add(
										new AnnouncementMessagePanel("message", item.getModel()),
										new WebMarkupContainer("separator").add(Condition.isEqual(Model.of(item.getIndex()), Model.of(0)).thenHide())
									);
								}
							}
						)
				)
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
		
		Date userLastReadDate = BasicApplicationSession.get().getUser().getAnnouncementInformation().getLastActionDate();
		Date mostRecentPublicationStartDate = announcementService.getMostRecentPublicationStartDate();
		
		if (userLastReadDate == null || userLastReadDate.before(mostRecentPublicationStartDate)) {
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