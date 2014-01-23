package fr.openwide.core.wicket.more.link.descriptor.generator;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.cycle.RequestCycle;

import fr.openwide.core.wicket.more.link.descriptor.AbstractDynamicBookmarkableLink;
import fr.openwide.core.wicket.more.link.descriptor.LinkInvalidTargetRuntimeException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.injector.LinkParameterInjectionRuntimeException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationRuntimeException;

/**
 * An utility object mapped to {@link IModel models}, that allows for simple link generation using these models to determine the target and parameters of the generated link.
 * 
 * <p>Re-implementing this interface is not recommended, as it may be extended with additional methods without prior notice.
 * <p><strong>Warning:</strong> this interface extends {@link IDetachable}. Thus, it <em>must</em> be detached before serialization.
 */
public interface ILinkGenerator extends IDetachable {

	/**
	 * Creates an {@link AbstractDynamicBookmarkableLink} that points to the same page/resource than this generator, with the same parameters.
	 * <p><strong>Note:</strong> special conditions apply to the rendering of this link if the parameters are invalid.
	 * See {@link AbstractDynamicBookmarkableLink} for more information.
	 * @return An {@link AbstractDynamicBookmarkableLink} matching this link generator.
	 * @see AbstractDynamicBookmarkableLink
	 */
	AbstractDynamicBookmarkableLink link(String wicketId);
	
	/**
	 * Renders the full URL for this link generator.
	 * <p>The resulting string includes protocol ("http://"), host, port, and path, as well as query parameters ("?arg0=true"), if any.
	 * @return The full URL for this link generator.
	 */
	String fullUrl() throws LinkInvalidTargetRuntimeException, LinkParameterInjectionRuntimeException, LinkParameterValidationRuntimeException;
	
	/**
	 * Renders the full URL for this link generator.
	 * <p>The resulting string includes protocol ("http://"), host, port, and path, as well as query parameters ("?arg0=true"), if any.
	 * @return The full URL for this link generator.
	 */
	String fullUrl(RequestCycle requestCycle) throws LinkInvalidTargetRuntimeException, LinkParameterInjectionRuntimeException, LinkParameterValidationRuntimeException;

}
