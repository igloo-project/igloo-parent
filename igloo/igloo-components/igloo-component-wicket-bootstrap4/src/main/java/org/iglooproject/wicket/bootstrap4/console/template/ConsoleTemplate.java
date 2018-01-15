package org.iglooproject.wicket.bootstrap4.console.template;

import static org.iglooproject.spring.property.SpringPropertyIds.VERSION;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_UNIT;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_VALUE;

import java.util.List;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.more.business.upgrade.service.IAbstractDataUpgradeService;
import org.iglooproject.jpa.security.business.person.model.GenericUser;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.behavior.ClassAttributeAppender;
import org.iglooproject.wicket.bootstrap4.console.common.model.ConsoleMenuItem;
import org.iglooproject.wicket.bootstrap4.console.common.model.ConsoleMenuSection;
import org.iglooproject.wicket.bootstrap4.console.maintenance.upgrade.page.ConsoleMaintenanceDonneesPage;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.collapse.BootstrapCollapseJavaScriptResourceReference;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.dropdown.BootstrapDropDownJavaScriptResourceReference;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.more.AbstractCoreSession;
import org.iglooproject.wicket.more.markup.html.CoreWebPage;
import org.iglooproject.wicket.more.markup.html.feedback.AnimatedGlobalFeedbackPanel;
import org.iglooproject.wicket.more.markup.html.template.AbstractWebPageTemplate;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.tooltip.BootstrapTooltip;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.tooltip.BootstrapTooltipDocumentBehavior;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.scrolltotop.ScrollToTopBehavior;
import org.iglooproject.wicket.more.model.ApplicationPropertyModel;
import org.iglooproject.wicket.more.security.page.LogoutPage;

import com.google.common.collect.Lists;

public abstract class ConsoleTemplate extends CoreWebPage {
	
	private static final long serialVersionUID = -477123413708677528L;
	
	private static final String HEAD_PAGE_TITLE_SEPARATOR = " ‹ ";
	
	@SpringBean
	protected IPropertyService propertyService;
	
	@SpringBean(required = false)
	protected IAbstractDataUpgradeService dataUpgradeService;
	
	private List<String> headPageTitleKeys = Lists.newArrayList();
	
	public ConsoleTemplate(PageParameters parameters) {
		super(parameters);
		
		ConsoleConfiguration configuration = ConsoleConfiguration.get();
		
		// Page title
		addHeadPageTitleKey(configuration.getConsolePageTitleKey());
		add(new ListView<String>("headPageTitle", headPageTitleKeys) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(final ListItem<String> item) {
				item.add(new Label("headPageTitleSeparator", HEAD_PAGE_TITLE_SEPARATOR) {
					private static final long serialVersionUID = 1L;
					
					@Override
					protected void onConfigure() {
						super.onConfigure();
						setVisible(item.getIndex() > 0);
					}
				});
				item.add(new Label("headPageTitleElement", new ResourceModel(item.getModelObject())));
			}
		});
		
		add(new AnimatedGlobalFeedbackPanel("animatedGlobalFeedbackPanel",
				propertyService.get(CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_VALUE),
				propertyService.get(CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_UNIT))
		);
		
		// Menu sections
		ListView<ConsoleMenuSection> menuSectionsListView = new ListView<ConsoleMenuSection>("menuSectionListView", 
				ConsoleConfiguration.get().getMenuSections()) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(ListItem<ConsoleMenuSection> item) {
				if (item.getModelObject().getMenuItems().size() > 0) {
					item.add(new MenuSectionWithDropDownFragment("menuSectionFragment", "menuSectionWithDropDown", item.getModel()));
				} else {
					item.add(new MenuSectionWithoutDropDownFragment("menuSectionFragment", "menuSectionWithoutDropDown", item.getModel()));
				}
			}
		};
		add(menuSectionsListView);
		
		// User menu
		add(new CoreLabel("userFullName", new LoadableDetachableModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected String load() {
				String userFullName = null;
				GenericUser<?, ?> user = AbstractCoreSession.get().getUser();
				if (user != null) {
					userFullName = user.getFullName();
				}
				return userFullName;
			}
		}).hideIfEmpty());
		
		add(new BookmarkablePageLink<Void>("logoutLink", LogoutPage.class));
		
		// Version
		add(new Label("version", ApplicationPropertyModel.of(VERSION)));
		
		add(new BootstrapTooltipDocumentBehavior(getBootstrapTooltip()));
		
		// Scroll to top
		add(new WebMarkupContainer("scrollToTop").add(new ScrollToTopBehavior()));
	}
	
	private BootstrapTooltip getBootstrapTooltip() {
		BootstrapTooltip bootstrapTooltip = new BootstrapTooltip();
		bootstrapTooltip.setSelector("[title],[data-original-title]");
		bootstrapTooltip.setAnimation(true);
		bootstrapTooltip.setContainer("body");
		return bootstrapTooltip;
	}
	
	protected abstract Class<? extends ConsoleTemplate> getMenuSectionPageClass();
	
	protected abstract Class<? extends ConsoleTemplate> getMenuItemPageClass();
	
	protected void addHeadPageTitleKey(String titleElementKey) {
		headPageTitleKeys.add(0, titleElementKey);
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		for (ResourceReference cssResourceReference : ConsoleConfiguration.get().getCssResourcesReferences()) {
			response.render(CssHeaderItem.forReference(cssResourceReference));
		}
		response.render(JavaScriptHeaderItem.forReference(BootstrapCollapseJavaScriptResourceReference.get()));
		response.render(JavaScriptHeaderItem.forReference(BootstrapDropDownJavaScriptResourceReference.get()));
	}

	private class MenuSectionWithoutDropDownFragment extends Fragment {
		private static final long serialVersionUID = 8410165897953003122L;
		
		public MenuSectionWithoutDropDownFragment(String id, String markupId, IModel<ConsoleMenuSection> menuSectionModel) {
			super(id, markupId, ConsoleTemplate.this, menuSectionModel);
			
			ConsoleMenuSection menuSection = menuSectionModel.getObject();
			
			WebMarkupContainer menuSectionContainer = new WebMarkupContainer("menuSection");
			add(menuSectionContainer);
			
			AbstractLink menuSectionLink = new BookmarkablePageLink<Void>("menuSectionLink", menuSection.getPageClass())
					.setBody(new ResourceModel(menuSection.getDisplayStringKey()));
			menuSectionContainer.add(menuSectionLink);
			if (menuSection.getPageClass() != null && menuSection.getPageClass().equals(getMenuSectionPageClass())) {
				menuSectionContainer.add(new ClassAttributeAppender("active"));
			}
		}
	}
	
	private class MenuSectionWithDropDownFragment extends Fragment {
		private static final long serialVersionUID = -7869292249062558408L;

		public MenuSectionWithDropDownFragment(String id, String markupId, IModel<ConsoleMenuSection> menuSectionModel) {
			super(id, markupId, ConsoleTemplate.this, menuSectionModel);
			
			ConsoleMenuSection menuSection = menuSectionModel.getObject();
			
			WebMarkupContainer menuSectionContainer = new WebMarkupContainer("menuSection");
			add(menuSectionContainer);

			if (menuSection.getPageClass() != null && menuSection.getPageClass().equals(getMenuSectionPageClass())) {
				menuSectionContainer.add(new ClassAttributeAppender("active"));
			}
			
			menuSectionContainer.add(new Label("menuSectionLabel", new ResourceModel(menuSection.getDisplayStringKey())));
			menuSectionContainer.add(new ListView<ConsoleMenuItem>("subMenuListView", menuSection.getMenuItems()) {
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(ListItem<ConsoleMenuItem> item) {
					ConsoleMenuItem menuItem = item.getModelObject();

					AbstractLink menuItemLink = new BookmarkablePageLink<Void>("subMenuLink", menuItem.getPageClass())
							.setBody(new ResourceModel(menuItem.getDisplayStringKey()));

					// On ajoute la page des DataUpgrade seulement si un
					// DataUpgradeService existe
					if (ConsoleMaintenanceDonneesPage.class.isAssignableFrom(menuItem.getPageClass())) {
						menuItemLink.setVisible(dataUpgradeService != null);
					}

					item.add(menuItemLink);

					if (menuItem.getPageClass() != null && menuItem.getPageClass().equals(getMenuItemPageClass())) {
						item.add(new ClassAttributeAppender("active"));
					}
				}

			});
		}

	}

	@Override
	public String getVariation() {
		// La console en BS4 quoi qu'il arrive, il ne faut pas laisser l'application qui l'utilise choisir la version.
		return AbstractWebPageTemplate.BOOTSTRAP4_VARIATION;
	}
}
