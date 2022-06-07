package org.iglooproject.basicapp.web.application.administration.component;

import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.basicapp.core.business.user.model.BasicUser;
import org.iglooproject.basicapp.web.application.administration.form.UserAjaxDropDownSingleChoice;
import org.iglooproject.basicapp.web.application.administration.form.UserGroupDropDownSingleChoice;
import org.iglooproject.basicapp.web.application.administration.model.AbstractUserDataProvider;
import org.iglooproject.basicapp.web.application.administration.page.AdministrationBasicUserDetailPage;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.wicket.markup.html.form.PageableSearchForm;
import org.iglooproject.wicket.model.Detachables;
import org.iglooproject.wicket.more.ajax.SerializableListener;
import org.iglooproject.wicket.more.common.behavior.UpdateOnChangeAjaxEventBehavior;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.model.ComponentPageModel;
import org.iglooproject.wicket.more.markup.html.form.EnumDropDownSingleChoice;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.iglooproject.wicket.more.model.GenericEntityModel;

public class BasicUserListSearchPanel extends Panel {

	private static final long serialVersionUID = -4624527265796845060L;

	private final IModel<BasicUser> quickAccessModel = new GenericEntityModel<>();

	public BasicUserListSearchPanel(String id, IPageable pageable, AbstractUserDataProvider<? extends BasicUser> dataProvider) {
		super(id);
		
		add(
			new PageableSearchForm<>("form", pageable)
				.add(
					new TextField<String>("name", dataProvider.getNameModel())
						.setLabel(new ResourceModel("business.user.name"))
						.add(new LabelPlaceholderBehavior()),
					new UserGroupDropDownSingleChoice("userGroup", dataProvider.getGroupModel())
						.setLabel(new ResourceModel("business.user.group"))
						.add(new LabelPlaceholderBehavior()),
					new EnumDropDownSingleChoice<>("enabledFilter", dataProvider.getEnabledFilterModel(), EnabledFilter.class)
						.setLabel(new ResourceModel("business.user.enabled.state"))
						.add(new LabelPlaceholderBehavior()),
					new UserAjaxDropDownSingleChoice<>("quickAccess", quickAccessModel, BasicUser.class)
						.setLabel(new ResourceModel("common.quickAccess"))
						.add(new LabelPlaceholderBehavior())
						.add(
							new UpdateOnChangeAjaxEventBehavior()
								.onChange(new SerializableListener() {
									private static final long serialVersionUID = 1L;
									@Override
									public void onBeforeRespond(Map<String, Component> map, AjaxRequestTarget target) {
										IPageLinkDescriptor linkDescriptor = AdministrationBasicUserDetailPage.MAPPER
											.setParameter2(new ComponentPageModel(BasicUserListSearchPanel.this))
											.map(new GenericEntityModel<>(quickAccessModel.getObject()));
										
										quickAccessModel.setObject(null);
										quickAccessModel.detach();
										
										if (linkDescriptor.isAccessible()) {
											throw linkDescriptor.newRestartResponseException();
										}
									}
								})
						)
				)
		);
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(quickAccessModel);
	}

}
