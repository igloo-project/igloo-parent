package fr.openwide.core.basicapp.web.application.referencedata.page;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.basicapp.core.business.referencedata.model.City;
import fr.openwide.core.basicapp.core.business.referencedata.search.ICitySearchQuery;
import fr.openwide.core.basicapp.web.application.common.component.NavTabsPanel;
import fr.openwide.core.basicapp.web.application.referencedata.component.SimpleGenericListItemListPanel;
import fr.openwide.core.basicapp.web.application.referencedata.template.ReferenceDataTemplate;
import fr.openwide.core.commons.util.functional.SerializableSupplier;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;

public class ReferenceDataPage extends ReferenceDataTemplate {

	private static final long serialVersionUID = -4381694964311714573L;
	
	private static final SerializableSupplier<City> CITY_SUPPLIER = new SerializableSupplier<City>() {
		private static final long serialVersionUID = 1L;
		@Override
		public City get() {
			return new City();
		}
	};

	public static final IPageLinkDescriptor linkDescriptor() {
		return new LinkDescriptorBuilder().page(ReferenceDataPage.class)
				.build();
	}

	public ReferenceDataPage(PageParameters parameters) {
		super(parameters);
		
		add(new Label("pageTitle", pageTitleModel));
		
		add(new NavTabsPanel("tabs")
				.add(
						new NavTabsPanel.SimpleTabFactory("business.city") {
							@Override
							public Component createContent(String wicketId) {
								return new SimpleGenericListItemListPanel<City>(wicketId, CITY_SUPPLIER, ICitySearchQuery.class);
							}
						}
				)
		);
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return null;
	}
}
