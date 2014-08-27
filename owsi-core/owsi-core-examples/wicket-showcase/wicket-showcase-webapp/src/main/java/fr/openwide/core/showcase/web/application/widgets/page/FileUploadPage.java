package fr.openwide.core.showcase.web.application.widgets.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.showcase.web.application.widgets.component.FileUploadPanel;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;

public class FileUploadPage extends WidgetsTemplate {

	private static final long serialVersionUID = 3280671690404455426L;
	
	public static IPageLinkDescriptor linkDescriptor() {
		return new LinkDescriptorBuilder()
				.page(FileUploadPage.class)
				.build();
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
