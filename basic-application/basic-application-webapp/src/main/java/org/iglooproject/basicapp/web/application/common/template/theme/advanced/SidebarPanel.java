package org.iglooproject.basicapp.web.application.common.template.theme.advanced;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.iglooproject.basicapp.core.business.user.model.BasicUser;
import org.iglooproject.basicapp.web.application.administration.form.UserAjaxDropDownSingleChoice;
import org.iglooproject.basicapp.web.application.administration.template.AdministrationUserDetailTemplate;
import org.iglooproject.spring.property.SpringPropertyIds;
import org.iglooproject.wicket.behavior.ClassAttributeAppender;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.more.ajax.SerializableListener;
import org.iglooproject.wicket.more.common.behavior.UpdateOnChangeAjaxEventBehavior;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.model.ComponentPageModel;
import org.iglooproject.wicket.more.markup.html.basic.EnclosureContainer;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.iglooproject.wicket.more.markup.html.template.model.NavigationMenuItem;
import org.iglooproject.wicket.more.model.ApplicationPropertyModel;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.iglooproject.wicket.more.util.model.Detachables;

public class SidebarPanel extends Panel {

	private static final long serialVersionUID = 3187936834300791175L;

	private final IModel<BasicUser> searchModel = GenericEntityModel.of((BasicUser) null);

	public SidebarPanel(
			String id,
			Supplier<List<NavigationMenuItem>> mainNavSupplier,
			Supplier<Class<? extends WebPage>> firstMenuPageSupplier,
			Supplier<Class<? extends WebPage>> secondMenuPageSupplier
	) {
		super(id);
		
		add(
				new EnclosureContainer("quickSearchContainer")
					.anyChildVisible()
					.add(
							new UserAjaxDropDownSingleChoice<>("user", searchModel, BasicUser.class)
								.setRequired(true)
								.setLabel(new ResourceModel("sidebar.quicksearch.user"))
								.add(new LabelPlaceholderBehavior())
								.add(
										new UpdateOnChangeAjaxEventBehavior()
												.onChange(new SerializableListener() {
													private static final long serialVersionUID = 1L;
													@Override
													public void onBeforeRespond(Map<String, Component> map, AjaxRequestTarget target) {
														IPageLinkDescriptor linkDescriptor = AdministrationUserDetailTemplate.<BasicUser>mapper()
																.setParameter2(new ComponentPageModel(SidebarPanel.this))
																.map(new GenericEntityModel<>(searchModel.getObject()));
														
														searchModel.setObject(null);
														searchModel.detach();
														
														if (linkDescriptor.isAccessible()) {
															throw linkDescriptor.newRestartResponseException();
														}
													}
												})
								)
					)
		);
		
		add(new ListView<NavigationMenuItem>("sidenavItems", mainNavSupplier.get()) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(ListItem<NavigationMenuItem> item) {
				NavigationMenuItem navItem = item.getModelObject();
				
				item.add(new ClassAttributeAppender(navItem.getCssClassesModel()));
				
				if (navItem.isActive(firstMenuPageSupplier.get())) {
					item.add(new ClassAttributeAppender("active"));
				}
				
				AbstractLink navLink = navItem.linkHidingIfInvalid("navLink");
				
				item.add(
						Condition.componentVisible(navLink).thenShow()
				);
				
				item.add(
						navLink
								.add(
										new EnclosureContainer("icon")
												.condition(Condition.hasText(navItem.getIconClassesModel()))
												.add(new ClassAttributeAppender(navItem.getIconClassesModel())),
										new CoreLabel("label", navItem.getLabelModel())
								)
				);
				
				List<NavigationMenuItem> subMenuItems = navItem.getSubMenuItems();
				
				item.add(
						new WebMarkupContainer("sidenavSubContainer")
								.add(
										new ListView<NavigationMenuItem>("sidenavSubItems", subMenuItems) {
											private static final long serialVersionUID = -2257358650754295013L;
											
											@Override
											protected void populateItem(ListItem<NavigationMenuItem> item) {
												NavigationMenuItem navItem = item.getModelObject();
												
												item.add(new ClassAttributeAppender(navItem.getCssClassesModel()));
												
												if (navItem.isActive(secondMenuPageSupplier.get())) {
													item.add(new ClassAttributeAppender("active"));
												}
												
												AbstractLink navLink = navItem.linkHidingIfInvalid("navLink");
												navLink.add(
														new CoreLabel("label", navItem.getLabelModel()),
														new EnclosureContainer("icon")
																.condition(Condition.hasText(navItem.getIconClassesModel()))
																.add(new ClassAttributeAppender(navItem.getIconClassesModel()))
												);
												
												item.add(navLink);
											}
											
											@Override
											protected void onDetach() {
												super.onDetach();
												Detachables.detach(getModelObject());
											}
										}
								)
								.setVisibilityAllowed(!subMenuItems.isEmpty())
				);
			}
			
			@Override
			protected void onDetach() {
				super.onDetach();
				Detachables.detach(getModelObject());
			}
		});
		
		add(
				new CoreLabel("version", new StringResourceModel("common.version", ApplicationPropertyModel.of(SpringPropertyIds.VERSION)))
						.add(new AttributeModifier("title", new StringResourceModel("common.version.full")
								.setParameters(ApplicationPropertyModel.of(SpringPropertyIds.VERSION), ApplicationPropertyModel.of(SpringPropertyIds.IGLOO_VERSION))
						))
		);
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(searchModel);
	}

}
