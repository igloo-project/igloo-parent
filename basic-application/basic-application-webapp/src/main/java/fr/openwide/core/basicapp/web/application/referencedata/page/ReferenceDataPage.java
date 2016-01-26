package fr.openwide.core.basicapp.web.application.referencedata.page;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.basicapp.core.business.referencedata.model.City;
import fr.openwide.core.basicapp.web.application.common.component.NavTabsPanel;
import fr.openwide.core.basicapp.web.application.referencedata.component.SimpleGenericListItemPanel;
import fr.openwide.core.basicapp.web.application.referencedata.template.ReferenceDataTemplate;
import fr.openwide.core.commons.util.functional.SerializableSupplier;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;

public class ReferenceDataPage extends ReferenceDataTemplate {

	private static final long serialVersionUID = -4381694964311714573L;

	public static final IPageLinkDescriptor linkDescriptor() {
		return new LinkDescriptorBuilder().page(ReferenceDataPage.class)
				.build();
	}

	public ReferenceDataPage(PageParameters parameters) {
		super(parameters);
		
		final SerializableSupplier<City> citySupplier = new SerializableSupplier<City>() {
			private static final long serialVersionUID = 1L;
			@Override
			public City get() {
				return new City();
			}
		};
		
		add(new Label("pageTitle", pageTitleModel));
		
		add(new NavTabsPanel("tabs")
				.add(
						new NavTabsPanel.SimpleTabFactory("business.city") {
							@Override
							public Component createContent(String wicketId) {
								return new SimpleGenericListItemPanel<City>(wicketId, citySupplier);
							}
						}
				)
		);
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		// TODO Auto-generated method stub
		return null;
	}
}
