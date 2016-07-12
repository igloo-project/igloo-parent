package fr.openwide.core.showcase.web.application.widgets.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.showcase.web.application.widgets.component.FileDownloadPanel;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;

public class FileDownloadPage extends WidgetsTemplate {

	private static final long serialVersionUID = -2672872859185908000L;

	public static final IPageLinkDescriptor linkDescriptor() {
		return LinkDescriptorBuilder.start()
				.page(FileDownloadPage.class);
	}

	public FileDownloadPage(PageParameters parameters) {
		super(parameters);
		add(new FileDownloadPanel("fileDownloadPanel"));
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return FileDownloadPage.class;
	}
}
