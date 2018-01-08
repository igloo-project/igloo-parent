package org.iglooproject.basicapp.web.application.administration.component;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.wiquery.core.events.MouseEvent;

import org.iglooproject.basicapp.core.business.authority.BasicApplicationAuthorityUtils;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.administration.form.UserGroupPopup;
import org.iglooproject.basicapp.web.application.administration.model.RoleDataProvider;
import org.iglooproject.jpa.security.business.authority.model.Authority;
import org.iglooproject.wicket.markup.html.panel.GenericPanel;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.html.image.BooleanIcon;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap3.modal.behavior.AjaxModalOpenBehavior;
import org.iglooproject.wicket.more.markup.repeater.sequence.SequenceView;
import org.iglooproject.wicket.more.model.BindingModel;

public class UserGroupDescriptionPanel extends GenericPanel<UserGroup> {

	private static final long serialVersionUID = 4372823586880908316L;
	
	@SpringBean
	private BasicApplicationAuthorityUtils authorityUtils;

	public UserGroupDescriptionPanel(String id, final IModel<UserGroup> userGroupModel) {
		super(id, userGroupModel);
		
		UserGroupPopup updatePopup = new UserGroupPopup("updatePopup", getModel());
		
		add(
				new WebMarkupContainer("lockedWarningContainer") {
					private static final long serialVersionUID = 1L;
					@Override
					protected void onConfigure() {
						super.onConfigure();
						setVisible(userGroupModel.getObject().isLocked());
					}
				},
				new MultiLineLabel("description", BindingModel.of(userGroupModel, Bindings.userGroup().description())),
				
				new SequenceView<Authority>("authorities", new RoleDataProvider()) {
					private static final long serialVersionUID = 1L;
					@Override
					protected void populateItem(final Item<Authority> item) {
						item.add(
								new Label("authorityName", item.getModel()),
								new BooleanIcon(
										"authorityCheck", 
										Condition.contains(
												BindingModel.of(userGroupModel, Bindings.userGroup().authorities()),
												item.getModel()
										)
								)
						);
					}
				},
				
				updatePopup,
				new BlankLink("updateButton")
						.add(new AjaxModalOpenBehavior(updatePopup, MouseEvent.CLICK))
		);
	}
}
