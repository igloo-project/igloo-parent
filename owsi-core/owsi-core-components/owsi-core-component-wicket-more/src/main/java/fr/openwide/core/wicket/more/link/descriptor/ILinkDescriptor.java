package fr.openwide.core.wicket.more.link.descriptor;

import org.apache.wicket.model.IDetachable;

import fr.openwide.core.wicket.more.link.descriptor.validator.ParameterValidationException;

public interface ILinkDescriptor extends IDetachable {

	/**
	 * Creates an {@link AbstractDynamicBookmarkableLink} that points to the same page/resource than this descriptor, with the same parameters.
	 * <p><strong>Note:</strong> special conditions apply to the rendering of this link if the parameters are invalid.
	 * See {@link AbstractDynamicBookmarkableLink} for more information.
	 * @return An {@link AbstractDynamicBookmarkableLink} matching this link descriptor.
	 * @see AbstractDynamicBookmarkableLink
	 */
	AbstractDynamicBookmarkableLink link(String wicketId);
	
	/**
	 * @return The full URL for this link descriptor, including protocol ("http://") and query parameters ("?arg0=true")
	 * @throws ParameterValidationException if the parameters validation returned an error
	 */
	String fullUrl() throws ParameterValidationException;

}
