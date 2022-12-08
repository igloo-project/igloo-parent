package org.iglooproject.basicapp.web.application.administration.component;

import static org.iglooproject.jpa.security.model.CorePermissionConstants.WRITE;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.core.business.authority.BasicApplicationAuthorityUtils;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.administration.form.UserGroupPopup;
import org.iglooproject.basicapp.web.application.administration.model.RoleDataProvider;
import org.iglooproject.jpa.security.business.authority.model.Authority;
import org.iglooproject.wicket.more.markup.html.image.BooleanIcon;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.repeater.sequence.SequenceView;
import org.wicketstuff.wiquery.core.events.MouseEvent;

import igloo.bootstrap.modal.AjaxModalOpenBehavior;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.panel.GenericPanel;
import igloo.wicket.model.BindingModel;

public class UserGroupDetailDescriptionPanel extends GenericPanel<UserGroup> {

	private static final long serialVersionUID = 4372823586880908316L;

	@SpringBean
	private BasicApplicationAuthorityUtils authorityUtils;

	public UserGroupDetailDescriptionPanel(String id, final IModel<UserGroup> userGroupModel) {
		super(id, userGroupModel);
		
		UserGroupPopup editPopup = new UserGroupPopup("editPopup");
		add(editPopup);
		
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
			}
		);
		
		add(
			new EnclosureContainer("actionsContainer")
				.anyChildVisible()
				.add(
					new BlankLink("edit")
						.add(
							new AjaxModalOpenBehavior(editPopup, MouseEvent.CLICK) {
								private static final long serialVersionUID = 1L;
								@Override
								protected void onShow(AjaxRequestTarget target) {
									editPopup.setUpEdit(getModelObject());
								}
							}
						)
						.add(
							Condition.permission(userGroupModel, WRITE)
								.thenShow()
						)
				)
		);
	}

}
