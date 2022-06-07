package org.iglooproject.basicapp.web.application.common.template.theme.advanced;

import static org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants.ROLE_ADMIN;

import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.web.application.administration.form.UserAjaxDropDownSingleChoice;
import org.iglooproject.basicapp.web.application.administration.template.AdministrationUserDetailTemplate;
import org.iglooproject.wicket.component.EnclosureContainer;
import org.iglooproject.wicket.condition.Condition;
import org.iglooproject.wicket.model.Detachables;
import org.iglooproject.wicket.more.ajax.SerializableListener;
import org.iglooproject.wicket.more.common.behavior.UpdateOnChangeAjaxEventBehavior;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.model.ComponentPageModel;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.iglooproject.wicket.more.model.GenericEntityModel;

public class SidebarQuickSearchPanel extends Panel {

	private static final long serialVersionUID = -4710074067415473028L;

	private final IModel<User> searchUserModel = GenericEntityModel.of((User) null);

	public SidebarQuickSearchPanel(String id) {
		super(id);
		
		add(Condition.anyChildVisible(this).thenShow());
		
		add(
			new EnclosureContainer("quickSearchContainer")
				.anyChildVisible()
				.add(
					new UserAjaxDropDownSingleChoice<>("user", searchUserModel, User.class)
						.setRequired(true)
						.setLabel(new ResourceModel("sidebar.quickSearch.user"))
						.add(new LabelPlaceholderBehavior())
						.add(
							new UpdateOnChangeAjaxEventBehavior()
								.onChange(new SerializableListener() {
									private static final long serialVersionUID = 1L;
									@Override
									public void onBeforeRespond(Map<String, Component> map, AjaxRequestTarget target) {
										IPageLinkDescriptor linkDescriptor = AdministrationUserDetailTemplate.mapper()
											.setParameter2(new ComponentPageModel(SidebarQuickSearchPanel.this))
											.map(new GenericEntityModel<>(searchUserModel.getObject()));
										
										searchUserModel.setObject(null);
										searchUserModel.detach();
										
										if (linkDescriptor.isAccessible()) {
											throw linkDescriptor.newRestartResponseException();
										}
									}
								})
						)
						.add(Condition.role(ROLE_ADMIN).thenShow())
				)
		);
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(searchUserModel);
	}

}
