package fr.openwide.core.wicket.more.link.descriptor.impl;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.core.request.handler.RenderPageRequestHandler;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.link.descriptor.AbstractDynamicBookmarkableLink;
import fr.openwide.core.wicket.more.link.descriptor.LinkInvalidTargetRuntimeException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationRuntimeException;

public class DynamicBookmarkablePageInstanceLink extends AbstractDynamicBookmarkableLink {
	
	private static final long serialVersionUID = 3375563675399910552L;
	
	private final IModel<? extends Page> pageInstanceModel;

	private final IModel<? extends Class<? extends Page>> expectedPageClassModel;
	
	public DynamicBookmarkablePageInstanceLink(String wicketId,
			IModel<? extends Page> pageInstanceModel,
			IModel<? extends Class<? extends Page>> expectedPageClassModel) {
		super(wicketId);
		this.pageInstanceModel = pageInstanceModel;
		this.expectedPageClassModel = expectedPageClassModel;
	}

	protected final Page getPageInstance() {
		return pageInstanceModel.getObject();
	}

	@Override
	protected boolean isValid() {
		Page pageInstance = getPageInstance();
		Class<? extends Page> expectedPageClass = expectedPageClassModel.getObject();
		return getPageInstance() != null
				&& (expectedPageClass == null || expectedPageClass.isAssignableFrom(pageInstance.getClass()))
				&& Session.get().getAuthorizationStrategy().isActionAuthorized(pageInstance, Page.RENDER);
	}

	@Override
	protected CharSequence getRelativeURL() throws LinkInvalidTargetRuntimeException,
			LinkParameterValidationRuntimeException {
		Page pageInstance = getPageInstance();
		if (pageInstance == null) {
			throw new LinkInvalidTargetRuntimeException("The target page instance of a link of type '" + getClass() + "' was null when trying to render the URL.");
		}
		Class<? extends Page> expectedPageClass = expectedPageClassModel.getObject();
		if (expectedPageClass != null && !expectedPageClass.isAssignableFrom(pageInstance.getClass())) {
			throw new LinkInvalidTargetRuntimeException("The target page instance '" + pageInstance + "' had unexpected type when trying to render the URL : got " + pageInstance.getClass().getName() + ", expected " + expectedPageClass.getName());
		}
		if (!Session.get().getAuthorizationStrategy().isActionAuthorized(pageInstance, Page.RENDER)) {
			throw new LinkInvalidTargetRuntimeException("The rendering of the target page '" + pageInstance + "' was not authorized when trying to render the URL.");
		}
		return urlFor(new RenderPageRequestHandler(new PageProvider(pageInstance)));
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		pageInstanceModel.detach();
	}

}
