package org.iglooproject.basicapp.web.application.common.template.theme.basic;

import java.util.List;
import java.util.function.Supplier;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.BasicApplicationApplication;
import org.iglooproject.basicapp.web.application.BasicApplicationSession;
import org.iglooproject.basicapp.web.application.common.template.theme.common.AbstractNavbarPanel;
import org.iglooproject.basicapp.web.application.common.template.theme.common.ChangeApplicationThemeAjaxLink;
import org.iglooproject.basicapp.web.application.profile.page.ProfilePage;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.wicket.behavior.ClassAttributeAppender;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.link.descriptor.generator.IPageLinkGenerator;
import org.iglooproject.wicket.more.markup.html.basic.EnclosureContainer;
import org.iglooproject.wicket.more.markup.html.template.model.NavigationMenuItem;
import org.iglooproject.wicket.more.model.BindingModel;
import org.iglooproject.wicket.more.security.page.LoginSuccessPage;
import org.iglooproject.wicket.more.security.page.LogoutPage;
import org.iglooproject.wicket.more.util.model.Detachables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		
		add(new ListView<NavigationMenuItem>("mainNav", mainNavSupplier.get()) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(ListItem<NavigationMenuItem> item) {
				NavigationMenuItem navItem = item.getModelObject();
				
				AbstractLink navLink = navItem.linkHidingIfInvalid("navLink");
				
				item.add(Condition.componentVisible(navLink).thenShow());
				
				item.add(
					navLink
						.add(
							new EnclosureContainer("icon")
								.condition(Condition.hasText(navItem.getIconClassesModel()))
								.add(new ClassAttributeAppender(navItem.getIconClassesModel())),
							new CoreLabel("label", navItem.getLabelModel())
						)
						.add(new AttributeModifier("name", navItem.getLabelModel()))
				);
				
				item.add(new ClassAttributeAppender(navItem.getCssClassesModel()));
				
				addActiveClass(item, firstMenuPageSupplier.get(), item);
				
				List<NavigationMenuItem> subMenuItems = navItem.getSubMenuItems();
				
				if (!subMenuItems.isEmpty()) {
					item.add(new ClassAttributeAppender("dropdown"));
					navLink.add(new ClassAttributeAppender("dropdown-toggle"));
					navLink.add(new AttributeModifier("data-toggle", "dropdown"));
					navLink.add(new AttributeModifier("data-hover", "dropdown"));
				}
				
				item.add(
					new WebMarkupContainer("subNavContainer")
						.add(
							new ListView<NavigationMenuItem>("subNav", subMenuItems) {
								private static final long serialVersionUID = -2257358650754295013L;
								
								@Override
								protected void populateItem(ListItem<NavigationMenuItem> item) {
									NavigationMenuItem navItem = item.getModelObject();
									
									AbstractLink navLink = navItem.linkHidingIfInvalid("navLink");
									
									item.add(Condition.componentVisible(navLink).thenShow());
									
									navLink.add(
										new CoreLabel("label", navItem.getLabelModel()),
										new EnclosureContainer("icon")
											.condition(Condition.hasText(navItem.getIconClassesModel()))
											.add(new ClassAttributeAppender(navItem.getIconClassesModel()))
									);
									
									navLink.add(new AttributeModifier("name", navItem.getLabelModel()));
									
									item.add(new ClassAttributeAppender(navItem.getCssClassesModel()));
									
									addActiveClass(item, secondMenuPageSupplier.get(), navLink);
									
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
			new AjaxLink<Void>("reconnexionLink") {
				private static final long serialVersionUID = 1L;
				@Override
				public void onClick(AjaxRequestTarget target) {
					try {
						BasicApplicationSession.get().signInAsMe();
						BasicApplicationSession.get().success(getString("authentication.back.success"));
					} catch (Exception e) {
						LOGGER.error("Erreur lors de la reconnexion de l'utilisateur.", e);
						Session.get().error(getString("signIn.error.unknown"));
					}
					throw LoginSuccessPage.linkDescriptor().newRestartResponseException();
				}
				
				@Override
				protected void onConfigure() {
					super.onConfigure();
					setVisible(BasicApplicationSession.get().getOriginalAuthentication() != null);
				}
			},
			
			new BookmarkablePageLink<Void>("logoutLink", LogoutPage.class)
		);
		
		add(
			new ChangeApplicationThemeAjaxLink("changeTheme")
		);
	}

}
