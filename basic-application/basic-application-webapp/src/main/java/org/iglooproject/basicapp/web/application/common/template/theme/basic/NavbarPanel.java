package org.iglooproject.basicapp.web.application.common.template.theme.basic;

import java.util.List;
import java.util.function.Supplier;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.BasicApplicationApplication;
import org.iglooproject.basicapp.web.application.BasicApplicationSession;
import org.iglooproject.basicapp.web.application.common.template.theme.common.AbstractNavbarPanel;
import org.iglooproject.basicapp.web.application.profile.page.ProfilePage;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.wicket.behavior.ClassAttributeAppender;
import org.iglooproject.wicket.bootstrap5.markup.html.template.js.bootstrap.dropdown.BootstrapDropdownBehavior;
import org.iglooproject.wicket.component.CoreLabel;
import org.iglooproject.wicket.component.EnclosureContainer;
import org.iglooproject.wicket.condition.Condition;
import org.iglooproject.wicket.model.BindingModel;
import org.iglooproject.wicket.more.link.descriptor.generator.IPageLinkGenerator;
import org.iglooproject.wicket.more.markup.html.template.model.NavigationMenuItem;
import org.iglooproject.wicket.more.security.page.LoginSuccessPage;
import org.iglooproject.wicket.more.security.page.LogoutPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

public class NavbarPanel extends AbstractNavbarPanel {

	private static final long serialVersionUID = 3273009208331893767L;

	private static final Logger LOGGER = LoggerFactory.getLogger(NavbarPanel.class);

	public NavbarPanel(
		String id,
		Supplier<List<NavigationMenuItem>> mainNavSupplier,
		Supplier<Class<? extends WebPage>> firstMenuPageSupplier,
		Supplier<Class<? extends WebPage>> secondMenuPageSupplier
	) {
		super(id);
		
		add(
			BasicApplicationApplication.get().getHomePageLinkDescriptor()
				.link("home")
		);
		
		EnclosureContainer navbarNavContainer = new EnclosureContainer("navbarNavContainer");
		add(navbarNavContainer.anyChildVisible());
		
		List<NavigationMenuItem> menuItems =
			mainNavSupplier.get().stream()
				.filter(
					mainNavItem -> {
						return	(mainNavItem.getPageLinkGenerator() != null && Condition.visible(mainNavItem.linkHidingIfInvalid("navLink")).applies())
							||	Condition.componentsAnyVisible(
									mainNavItem.getSubMenuItems()
										.stream()
										.map(subMenuItem -> subMenuItem.linkHidingIfInvalid("navLink"))
										.collect(ImmutableList.toImmutableList())
								).applies();
					}
				)
				.collect(ImmutableList.toImmutableList());
		
		navbarNavContainer.add(
			new ListView<NavigationMenuItem>("navbarNavItems", menuItems) {
				private static final long serialVersionUID = 1L;
				
				@Override
				protected void populateItem(ListItem<NavigationMenuItem> item) {
					NavigationMenuItem navItem = item.getModelObject();
					MarkupContainer navLink = navItem.linkHidingIfInvalid("navLink");
					
					item.add(navLink);
					
					navLink
						.add(
							new EnclosureContainer("icon")
								.condition(Condition.hasText(navItem.getIconClassesModel()))
								.add(new ClassAttributeAppender(navItem.getIconClassesModel())),
							new CoreLabel("label", navItem.getLabelModel())
						);
					
					List<NavigationMenuItem> subMenuItems =
						navItem.getSubMenuItems().stream()
							.filter(subMenuItem -> Condition.visible(subMenuItem.linkHidingIfInvalid("navLink")).applies())
							.collect(ImmutableList.toImmutableList());
					
					Condition isSubVisibleCondition = Condition.constant(!subMenuItems.isEmpty());
					
					IModel<Boolean> isSubActiveModel = Model.of(false);
					Condition isSubActiveCondition = Condition.isTrue(isSubActiveModel);
					
					EnclosureContainer navbarNavSubContainer = new EnclosureContainer("navbarNavSubContainer");
					item.add(
						navbarNavSubContainer
							.anyChildVisible()
							.setOutputMarkupId(true)
					);
					
					navbarNavSubContainer
						.add(
							new ListView<NavigationMenuItem>("navbarNavSubItems", subMenuItems) {
								private static final long serialVersionUID = -2257358650754295013L;
								
								@Override
								protected void populateItem(ListItem<NavigationMenuItem> item) {
									NavigationMenuItem navItem = item.getModelObject();
									MarkupContainer navLink = navItem.linkHidingIfInvalid("navLink");
									
									item.add(navLink);
									
									navLink.add(
										new EnclosureContainer("icon")
											.condition(Condition.hasText(navItem.getIconClassesModel()))
											.add(new ClassAttributeAppender(navItem.getIconClassesModel())),
										new CoreLabel("label", navItem.getLabelModel())
									);
									
									item.add(new ClassAttributeAppender(navItem.getCssClassesModel()));
									
									if (navItem.isActive(secondMenuPageSupplier.get())) {
										navLink.add(new ClassAttributeAppender(Model.of("active")));
										isSubActiveModel.setObject(true);
									}
								}
							}
								.setVisibilityAllowed(!subMenuItems.isEmpty())
						);
					
					if (navItem.getPageLinkGenerator() == null) {
						item.add(isSubVisibleCondition.thenShow());
					}
					
					navLink
						.add(
							new ClassAttributeAppender(
								isSubVisibleCondition
									.then(Model.of("dropdown-toggle"))
									.otherwise(Model.of(""))
							),
							new BootstrapDropdownBehavior(navbarNavSubContainer) {
								private static final long serialVersionUID = 1L;
								@Override
								public boolean isEnabled(Component component) {
									return isSubVisibleCondition.applies();
								}
							},
							new ClassAttributeAppender(
								Condition.or(
									isSubVisibleCondition.and(isSubActiveCondition),
									Condition.isTrue(() -> navItem.isActive(firstMenuPageSupplier.get()))
								)
									.then(Model.of("active"))
									.otherwise(Model.of())
							)
						);
					
					item.add(new ClassAttributeAppender(navItem.getCssClassesModel()));
					
					item.add(
						new ClassAttributeAppender(
							isSubVisibleCondition
								.then(Model.of("dropdown"))
								.otherwise(Model.of(""))
						)
					);
				}
			}
				.setVisibilityAllowed(!menuItems.isEmpty())
		);
		
		addNavItem(
			"profileLinkNavItem",
			ProfilePage.linkDescriptor(),
			firstMenuPageSupplier.get(),
			new SerializableFunction2<IPageLinkGenerator, Component>() {
				private static final long serialVersionUID = 1L;
				@Override
				public Component apply(IPageLinkGenerator pageLinkGenerator) {
					return pageLinkGenerator
						.link("profileLink")
						.add(
							new CoreLabel("originalAuthentication", new IModel<String>() {
								private static final long serialVersionUID = 1L;
								@Override
								public String getObject() {
									return BasicApplicationSession.get().getOriginalAuthentication() != null ? BasicApplicationSession.get().getOriginalAuthentication().getName() : null;
								}
							})
								.hideIfEmpty(),
							new CoreLabel("userFullName", BindingModel.of(BasicApplicationSession.get().getUserModel(), Bindings.user().fullName()))
								.hideIfEmpty()
						);
				}
			}
		);
		
		add(
			new AjaxLink<Void>("signInAsMe") {
				private static final long serialVersionUID = 1L;
				@Override
				public void onClick(AjaxRequestTarget target) {
					try {
						BasicApplicationSession.get().signInAsMe();
						BasicApplicationSession.get().success(getString("authentication.back.success"));
					} catch (Exception e) {
						LOGGER.error("Erreur lors de la reconnexion de l'utilisateur.", e);
						Session.get().error(getString("common.error.unknown"));
					}
					throw LoginSuccessPage.linkDescriptor().newRestartResponseException();
				}
				
				@Override
				protected void onConfigure() {
					super.onConfigure();
					setVisible(BasicApplicationSession.get().getOriginalAuthentication() != null);
				}
			},
			
			new BookmarkablePageLink<Void>("logout", LogoutPage.class)
		);
	}

}
