package fr.openwide.core.wicket.more.link.descriptor.impl;

import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Args;

import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;

public class CorePageLinkDescriptorImpl implements IPageLinkDescriptor {
	
	private static final long serialVersionUID = -9139677593653180236L;

	private final IModel<Class<? extends Page>> pageClassModel;
	private final PageParametersModel parametersModel;

	public CorePageLinkDescriptorImpl(IModel<Class<? extends Page>> pageClassModel, PageParametersModel parametersModel) {
		super();
		Args.notNull(pageClassModel, "pageClassModel");
		Args.notNull(parametersModel, "parametersModel");
		this.pageClassModel = pageClassModel;
		this.parametersModel = parametersModel;
	}

	protected Class<? extends Page> getPageClass() {
		return pageClassModel.getObject();
	}

	protected PageParameters getParameters() {
		return parametersModel.getObject();
	}

	@Override
	public AbstractLink link(String wicketId) {
		return new DynamicBookmarkablePageLink(wicketId, pageClassModel, parametersModel);
	}

	@Override
	public String fullUrl() {
		RequestCycle requestCycle = RequestCycle.get();
		return requestCycle.getUrlRenderer()
				.renderFullUrl(
						Url.parse(
								requestCycle.urlFor(getPageClass(), getParameters())
						)
				);
	}
	
	@Override
	public void setResponsePage() {
		RequestCycle.get().setResponsePage(getPageClass(), getParameters());
	}

	@Override
	public RestartResponseException restartResponseException() {
		return new RestartResponseException(getPageClass(), getParameters());
	}
	
	@Override
	public RestartResponseAtInterceptPageException restartResponseAtInterceptPageException() {
		return new RestartResponseAtInterceptPageException(getPageClass(), getParameters());
	}

	@Override
	public void detach() {
		pageClassModel.detach();
		parametersModel.detach();
	}

}
