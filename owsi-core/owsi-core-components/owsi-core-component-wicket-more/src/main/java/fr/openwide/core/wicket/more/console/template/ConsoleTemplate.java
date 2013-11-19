package fr.openwide.core.wicket.more.console.template;

import java.util.Collections;
import java.util.List;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.google.common.collect.Lists;

import fr.openwide.core.jpa.more.business.upgrade.service.IAbstractDataUpgradeService;
import fr.openwide.core.wicket.behavior.ClassAttributeAppender;
import fr.openwide.core.wicket.more.console.common.model.ConsoleMenuItem;
import fr.openwide.core.wicket.more.console.common.model.ConsoleMenuSection;
import fr.openwide.core.wicket.more.console.maintenance.upgrade.page.ConsoleMaintenanceDonneesPage;
import fr.openwide.core.wicket.more.lesscss.LessCssResourceReference;
import fr.openwide.core.wicket.more.markup.html.CoreWebPage;
import fr.openwide.core.wicket.more.markup.html.feedback.AnimatedGlobalFeedbackPanel;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.tooltip.BootstrapTooltip;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.tooltip.BootstrapTooltipDocumentBehavior;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.scrolltotop.ScrollToTopBehavior;

public abstract class ConsoleTemplate extends CoreWebPage {
	
	private static final long serialVersionUID = -477123413708677528L;
	
	private static final String HEAD_PAGE_TITLE_SEPARATOR = " ‹ ";
	
	@SpringBean(required = false)
	protected IAbstractDataUpgradeService dataUpgradeService;
	
	private List<String> headPageTitleKeys = Lists.newArrayList();
	
	private LessCssResourceReference lessCssResourceReference;
	
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
		ListView<ConsoleMenuSection> menuSectionsListView = new ListView<ConsoleMenuSection>("menuSectionsListView", 
				ConsoleConfiguration.get().getMenuSections()) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(ListItem<ConsoleMenuSection> item) {
				ConsoleMenuSection menuSection = item.getModelObject();
				Link<Void> menuSectionLink = new BookmarkablePageLink<Void>("menuSectionLink", menuSection.getPageClass());
				menuSectionLink.add(new Label("menuSectionLabel", new ResourceModel(menuSection.getDisplayStringKey())));
				item.add(menuSectionLink);
				if (menuSection.getPageClass() != null && menuSection.getPageClass().equals(getMenuSectionPageClass())) {
					item.add(new ClassAttributeAppender("active"));
				}
			}
		};
		add(menuSectionsListView);
		
		// Menu items of the selected menu section
		ConsoleMenuSection selectedMenuSection = getSelectedSection();
		
		List<ConsoleMenuItem> menuItems;
		if (selectedMenuSection != null) {
			menuItems = selectedMenuSection.getMenuItems();
		} else {
			menuItems = Collections.emptyList();
		}
		
		ListView<ConsoleMenuItem> menuItemsListView = new ListView<ConsoleMenuItem>("menuItemsListView", menuItems) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(ListItem<ConsoleMenuItem> item) {
				ConsoleMenuItem menuItem = item.getModelObject();
				
				BookmarkablePageLink<Void> menuItemLink = new BookmarkablePageLink<Void>("menuItemLink", menuItem.getPageClass());
				
				// On ajoute la page des DataUpgrade seulement si un DataUpgradeService existe
				if (ConsoleMaintenanceDonneesPage.class.isAssignableFrom(menuItem.getPageClass())) {
					menuItemLink.setVisible(dataUpgradeService != null);
				}
				
				menuItemLink.add(new Label("menuItemLabel", new ResourceModel(menuItem.getDisplayStringKey())));
				
				item.add(menuItemLink);
				
				if (menuItem.getPageClass() != null && menuItem.getPageClass().equals(getMenuItemPageClass())) {
					item.add(new ClassAttributeAppender("active"));
				}
			}
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(!getModelObject().isEmpty());
			}
		};
		add(menuItemsListView);
		
		add(new BootstrapTooltipDocumentBehavior(getBootstrapTooltip()));
		
		// Scroll to top
		WebMarkupContainer scrollToTop = new WebMarkupContainer("scrollToTop");
		scrollToTop.add(new ScrollToTopBehavior());
		add(scrollToTop);
	}
	
	private BootstrapTooltip getBootstrapTooltip() {
		BootstrapTooltip bootstrapTooltip = new BootstrapTooltip();
		bootstrapTooltip.setSelector("[title],[data-original-title]");
		bootstrapTooltip.setAnimation(true);
		bootstrapTooltip.setContainer("body");
		return bootstrapTooltip;
	}
	
	private ConsoleMenuSection getSelectedSection() {
		for (ConsoleMenuSection menuSection : ConsoleConfiguration.get().getMenuSections()) {
			if (menuSection.getPageClass() != null && menuSection.getPageClass().equals(getMenuSectionPageClass())) {
				return menuSection;
			}
		}
		return null;
	}
	
	protected abstract Class<? extends ConsoleTemplate> getMenuSectionPageClass();
	
	protected abstract Class<? extends ConsoleTemplate> getMenuItemPageClass();
	
	protected void addHeadPageTitleKey(String titleElementKey) {
		headPageTitleKeys.add(0, titleElementKey);
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		if (lessCssResourceReference == null) {
			lessCssResourceReference = ConsoleConfiguration.get().getLessCssResourceReference();
		}
		response.render(CssHeaderItem.forReference(lessCssResourceReference));
	}
}
