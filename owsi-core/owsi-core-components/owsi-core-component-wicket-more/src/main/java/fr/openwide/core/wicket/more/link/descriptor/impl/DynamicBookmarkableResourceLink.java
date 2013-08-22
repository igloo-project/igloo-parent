package fr.openwide.core.wicket.more.link.descriptor.impl;

import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.handler.resource.ResourceReferenceRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.lang.Args;

import fr.openwide.core.wicket.more.link.descriptor.AbstractDynamicBookmarkableLink;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;

/**
 * An {@link AbstractDynamicBookmarkableLink} targeting a {@link ResourceReference} that may change during the page life cycle.
 * <p>This implementation could not derive from {@link ResourceLink}, whose target Resource is inherently static.
 * @see ResourceLink
 */
public class DynamicBookmarkableResourceLink extends AbstractDynamicBookmarkableLink {
	
	private static final long serialVersionUID = 7217475839311474526L;

	private final IModel<? extends ResourceReference> resourceReferenceModel;

	public DynamicBookmarkableResourceLink(
			String wicketId,
			IModel<? extends ResourceReference> resourceReferenceModel,
			IModel<PageParameters> parametersMapping,
			ILinkParameterValidator parametersValidator) {
		super(wicketId, parametersMapping, parametersValidator);
		Args.notNull(resourceReferenceModel, "resourceReferenceModel");
		this.resourceReferenceModel = wrap(resourceReferenceModel); 
	}

	protected final ResourceReference getResourceReference() {
		ResourceReference resourceReference = resourceReferenceModel.getObject();
		if (resourceReference == null) {
			throw new IllegalStateException("The resourceReference of a link of type " + getClass() + " was found to be null.");
		}
		return resourceReference;
	}

	/**
	 * @see ResourceLink
	 */
	@Override
	protected final CharSequence getURL(PageParameters resourceParameters) {
		ResourceReference resourceReference = getResourceReference();
		
		//---------------------------------------------------------------------------------------------------------
		// CODE AND COMMENTS FROM org.apache.wicket.markup.html.link.ResourceLink IN Wicket 6.9.1 (OWSI-Core 0.8.5-SNAPSHOT)
		//---------------------------------------------------------------------------------------------------------
		// TODO post 1.2: should we have support for locale changes when the
		// resource reference (or resource??) is set manually..
		// We should get a new resource reference for the current locale
		// then
		// that points to the same resource but with another locale if it
		// exists.
		// something like
		// SharedResource.getResourceReferenceForLocale(resourceReference);
		if (resourceReference.canBeRegistered()) {
			getApplication().getResourceReferenceRegistry().registerResourceReference(resourceReference);
		}
		
		return getRequestCycle().urlFor(
			new ResourceReferenceRequestHandler(resourceReference, resourceParameters)
		);
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		resourceReferenceModel.detach();
	}

}
