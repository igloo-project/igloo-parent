package org.iglooproject.basicapp.web.application.common.component;

import java.util.Date;
import java.util.List;

import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.core.business.message.model.GeneralMessage;
import org.iglooproject.basicapp.core.business.message.service.IGeneralMessageService;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.service.IUserService;
import org.iglooproject.basicapp.web.application.BasicApplicationSession;
import org.iglooproject.basicapp.web.application.common.renderer.GeneralMessageRenderer;
import org.iglooproject.wicket.behavior.ClassAttributeAppender;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.more.ajax.AjaxListeners;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.html.basic.EnclosureContainer;
import org.iglooproject.wicket.more.util.model.Detachables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeneralMessagePanel extends Panel {

	private static final long serialVersionUID = 9097065669590214330L;

	private static final Logger LOGGER = LoggerFactory.getLogger(GeneralMessagePanel.class);

	@SpringBean
	private IGeneralMessageService generalMessageService;

	@SpringBean
	private IUserService userService;

	private final IModel<Boolean> closeModel = Model.of(Boolean.FALSE);

	private final IModel<List<GeneralMessage>> generalMessageListModel;

	public GeneralMessagePanel(String id) {
		super(id);
		setOutputMarkupId(true);
		
		generalMessageListModel = new LoadableDetachableModel<List<GeneralMessage>>() {
			private static final long serialVersionUID = 1L;
			@Override
			protected List<GeneralMessage> load() {
				return generalMessageService.listActiveMessages();
			}
		};
		
		ListView<GeneralMessage> messages = new ListView<GeneralMessage>("messages", generalMessageListModel) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void populateItem(ListItem<GeneralMessage> item) {
				item.add(
						new CoreLabel("message", GeneralMessageRenderer.get().asModel(item.getModel())),
						new WebMarkupContainer("separator").add(Condition.isEqual(Model.of(item.getIndex()), Model.of(0)).thenHide())
				);
			}
		};
		
		add(
			new AjaxLink<Void>("main") {
				private static final long serialVersionUID = 1L;
				@Override
				public void onClick(AjaxRequestTarget target) {
					try {
						closeModel.setObject(!closeModel.getObject());
						
						User user = BasicApplicationSession.get().getUser();
						user.setLastGeneralMessageActionDate(new Date());
						user.setOpenGeneralMessage(!closeModel.getObject());
						userService.update(user);
					} catch (Exception e) {
						LOGGER.error("Error occured while saving user", e);
						Session.get().error(getString("common.error.unexpected"));
					}
					AjaxListeners.add(target, AjaxListeners.refresh(GeneralMessagePanel.this));
				}
			}
				.add(
					new EnclosureContainer("container")
						.condition(Condition.isFalse(closeModel))
						.add(messages)
				)
		);
		
		add(
				new ClassAttributeAppender(Condition.isTrue(closeModel).then("general-message-section-dismissed").otherwise(""))
		);
	}

	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		if (generalMessageService.newMessages()) {
			setVisible(true);
			
			Date userLastReadDate = BasicApplicationSession.get().getUser().getLastGeneralMessageActionDate();
			Date mostRecentPublicationStartDate = generalMessageService.getMostRecentPublicationStartDate();
			
			if (userLastReadDate == null || userLastReadDate.before(mostRecentPublicationStartDate)) {
				closeModel.setObject(Boolean.FALSE);
			} else {
				closeModel.setObject(!BasicApplicationSession.get().getUser().isOpenGeneralMessage());
			}
		} else {
			setVisible(false);
			closeModel.setObject(Boolean.TRUE);
		}
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(generalMessageListModel, closeModel);
	}

}