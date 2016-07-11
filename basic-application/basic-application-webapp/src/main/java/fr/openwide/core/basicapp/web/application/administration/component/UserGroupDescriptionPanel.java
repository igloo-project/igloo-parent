package fr.openwide.core.basicapp.web.application.administration.component;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.wiquery.core.events.MouseEvent;

import fr.openwide.core.basicapp.core.business.authority.BasicApplicationAuthorityUtils;
import fr.openwide.core.basicapp.core.business.user.model.UserGroup;
import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.basicapp.web.application.administration.form.UserGroupPopup;
import fr.openwide.core.basicapp.web.application.administration.model.RoleDataProvider;
import fr.openwide.core.jpa.security.business.authority.model.Authority;
import fr.openwide.core.wicket.markup.html.panel.GenericPanel;
import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.markup.html.image.BooleanIcon;
import fr.openwide.core.wicket.more.markup.html.link.BlankLink;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.behavior.AjaxModalOpenBehavior;
import fr.openwide.core.wicket.more.markup.repeater.sequence.SequenceView;
import fr.openwide.core.wicket.more.model.BindingModel;

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
