package org.iglooproject.basicapp.web.application.administration.component;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.core.business.authority.BasicApplicationAuthorityUtils;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.administration.form.UserGroupPopup;
import org.iglooproject.basicapp.web.application.administration.model.RoleDataProvider;
import org.iglooproject.jpa.security.business.authority.model.Authority;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.markup.html.panel.GenericPanel;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.html.basic.EnclosureContainer;
import org.iglooproject.wicket.more.markup.html.image.BooleanIcon;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.behavior.AjaxModalOpenBehavior;
import org.iglooproject.wicket.more.markup.repeater.sequence.SequenceView;
import org.iglooproject.wicket.more.model.BindingModel;
import org.wicketstuff.wiquery.core.events.MouseEvent;

public class UserGroupDescriptionPanel extends GenericPanel<UserGroup> {

	private static final long serialVersionUID = 4372823586880908316L;
	
	@SpringBean
	private BasicApplicationAuthorityUtils authorityUtils;

	public UserGroupDescriptionPanel(String id, final IModel<UserGroup> userGroupModel) {
		super(id, userGroupModel);
		
		UserGroupPopup updatePopup = new UserGroupPopup("updatePopup", getModel());
		
		add(
				new EnclosureContainer("lockedWarningContainer")
						.condition(Condition.isTrue(BindingModel.of(getModel(), Bindings.userGroup().locked()))),
				
				new CoreLabel("description", BindingModel.of(userGroupModel, Bindings.userGroup().description()))
						.multiline()
						.showPlaceholder(),
				
				new SequenceView<Authority>("authorities", new RoleDataProvider()) {
					private static final long serialVersionUID = 1L;
					@Override
					protected void populateItem(final Item<Authority> item) {
						item.add(
								new CoreLabel("authorityName", item.getModel()),
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
