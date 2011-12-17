package fr.openwide.core.wicket.more.markup.html;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public abstract class CoreWebPage extends WebPage {

	private static final long serialVersionUID = 607411189304353902L;

	public CoreWebPage() {
	}

	public CoreWebPage(IModel<?> model) {
		super(model);
	}

	public CoreWebPage(PageParameters parameters) {
		super(parameters);
	}

	public final <C extends IRequestablePage> void redirect(final Class<C> cls) {
		setResponsePage(cls);
	}

	public final <C extends IRequestablePage> void redirect(final Class<C> cls, PageParameters parameters) {
		setResponsePage(cls, parameters);
	}

	public final void redirect(final Page page) {
		setResponsePage(page);
	}

}
