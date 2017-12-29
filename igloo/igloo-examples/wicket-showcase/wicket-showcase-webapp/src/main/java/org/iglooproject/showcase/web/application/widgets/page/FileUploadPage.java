package org.iglooproject.showcase.web.application.widgets.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import org.iglooproject.showcase.web.application.widgets.component.FileUploadPanel;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;

public class FileUploadPage extends WidgetsTemplate {

	private static final long serialVersionUID = 3280671690404455426L;

	public static final IPageLinkDescriptor linkDescriptor() {
		return LinkDescriptorBuilder.start()
				.page(FileUploadPage.class);
	}

	public FileUploadPage(PageParameters parameters) {
		super(parameters);
		add(new FileUploadPanel("fileUploadPanel"));
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return FileUploadPage.class;
	}
}
