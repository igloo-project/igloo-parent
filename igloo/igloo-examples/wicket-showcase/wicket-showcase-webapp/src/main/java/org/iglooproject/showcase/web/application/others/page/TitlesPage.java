package org.iglooproject.showcase.web.application.others.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import org.iglooproject.showcase.web.application.util.template.MainTemplate;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;

public class TitlesPage extends MainTemplate {

	private static final long serialVersionUID = 182545144170932682L;

	public static final IPageLinkDescriptor linkDescriptor() {
		return LinkDescriptorBuilder.start()
				.page(TitlesPage.class);
	}

	public TitlesPage(PageParameters parameters) {
		super(parameters);
	}

	@Override
	protected Class<? extends WebPage> getFirstMenuPage() {
		return TitlesPage.class;
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return null;
	}

}
