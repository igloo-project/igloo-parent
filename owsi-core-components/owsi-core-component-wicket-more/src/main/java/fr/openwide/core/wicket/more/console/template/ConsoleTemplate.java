package fr.openwide.core.wicket.more.console.template;

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
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.google.common.collect.Lists;

import fr.openwide.core.jpa.more.business.upgrade.service.IAbstractDataUpgradeService;
import fr.openwide.core.jpa.security.business.person.model.GenericUser;
import fr.openwide.core.spring.config.CoreConfigurer;
import fr.openwide.core.wicket.behavior.ClassAttributeAppender;
import fr.openwide.core.wicket.markup.html.basic.CoreLabel;
import fr.openwide.core.wicket.more.AbstractCoreSession;
import fr.openwide.core.wicket.more.console.common.model.ConsoleMenuItem;
import fr.openwide.core.wicket.more.console.common.model.ConsoleMenuSection;
import fr.openwide.core.wicket.more.console.maintenance.upgrade.page.ConsoleMaintenanceDonneesPage;
import fr.openwide.core.wicket.more.lesscss.LessCssResourceReference;
import fr.openwide.core.wicket.more.markup.html.CoreWebPage;
import fr.openwide.core.wicket.more.markup.html.feedback.AnimatedGlobalFeedbackPanel;
import fr.openwide.core.wicket.more.markup.html.template.AbstractWebPageTemplate;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.collapse.BootstrapCollapseJavaScriptResourceReference;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.dropdown.BootstrapDropDownJavaScriptResourceReference;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.tooltip.BootstrapTooltip;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.tooltip.BootstrapTooltipDocumentBehavior;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.scrolltotop.ScrollToTopBehavior;
import fr.openwide.core.wicket.more.security.page.LogoutPage;

public abstract class ConsoleTemplate extends CoreWebPage {
	
	private static final long serialVersionUID = -477123413708677528L;
	
	private static final String HEAD_PAGE_TITLE_SEPARATOR = " ‹ ";
	
	@SpringBean
	protected CoreConfigurer configurer;
	
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
		
		add(new AnimatedGlobalFeedbackPanel("animatedGlobalFeedbackPanel"));
		
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
		add(new Label("version", configurer.getVersion()));
		
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
		for (LessCssResourceReference lessCssResourceReference : ConsoleConfiguration.get().getLessCssResourcesReferences()) {
			response.render(CssHeaderItem.forReference(lessCssResourceReference));
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
		// La console en BS3 quoi qu'il arrive, il ne faut pas laisser l'application
		// qui l'utilise choisir si on utilise BS2 ou BS3.
		return AbstractWebPageTemplate.BOOTSTRAP3_VARIATION;
	}
}
