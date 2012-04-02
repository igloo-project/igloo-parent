package fr.openwide.core.wicket.more.markup.html;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
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

	public final void redirect(final Class<? extends Page> clazz) {
		throw new RestartResponseException(clazz);
	}

	public final void redirect(final Class<? extends Page> clazz, PageParameters parameters) {
		throw new RestartResponseException(clazz, parameters);
	}

	public final void redirect(final Page page) {
		throw new RestartResponseException(page);
	}
	
	protected Component visible(Component component, boolean visible) {
		component.setVisible(visible);
		return component;
	}

}
