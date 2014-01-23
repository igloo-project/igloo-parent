package fr.openwide.core.wicket.more.link.descriptor.generator;


import java.util.Collection;

import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.flow.RedirectToUrlException;

import fr.openwide.core.wicket.more.link.descriptor.AbstractDynamicBookmarkableLink;
import fr.openwide.core.wicket.more.link.descriptor.LinkInvalidTargetRuntimeException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.injector.LinkParameterInjectionRuntimeException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationRuntimeException;
import fr.openwide.core.wicket.more.markup.html.template.model.NavigationMenuItem;


/**
 * An {@link ILinkGenerator} pointing to a {@link Page}.
 * 
 * <p>Re-implementing this interface is not recommended, as it may be extended with additional methods without prior notice.
 * <p><strong>Warning:</strong> this interface extends {@link IDetachable}. Thus, it <em>must</em> be detached before serialization.
 * 
 * @see ILinkGenerator
 */
public interface IPageLinkGenerator extends ILinkGenerator, IDetachable  {

	/**
	 * Creates an {@link AbstractDynamicBookmarkableLink} that points to the same page than this descriptor, with the same parameters,
	 * using the given anchor.
	 * <p><strong>Note:</strong> special conditions apply to the rendering of this link if the parameters are invalid.
	 * See {@link AbstractDynamicBookmarkableLink} for more information.
	 * @return An {@link AbstractDynamicBookmarkableLink} matching this link descriptor.
	 * @see AbstractDynamicBookmarkableLink
	 */
	AbstractDynamicBookmarkableLink link(String wicketId, String anchor);

	/**
	 * Sets the response page and parameters for the current {@link RequestCycle} to the value of this link descriptor.
	 * @throws LinkInvalidTargetRuntimeException if the target page was invalid (null, for example)
	 * @throws LinkParameterValidationRuntimeException if the parameters validation returned an error
	 * @throws LinkParameterInjectionRuntimeException if an error occurred during parameters injection (most probably during the conversion)
	 * @see RequestCycle#setResponsePage(Class, org.apache.wicket.request.mapper.parameter.PageParameters)
	 */
	void setResponsePage() throws LinkInvalidTargetRuntimeException, LinkParameterValidationRuntimeException,
			LinkParameterInjectionRuntimeException;

	/**
	 * Creates a {@link RestartResponseException} with the same page and parameters than this link descriptor.
	 * @throws LinkInvalidTargetRuntimeException if the target page was invalid (null, for example)
	 * @throws LinkParameterValidationRuntimeException if the parameters validation returned an error
	 * @throws LinkParameterInjectionRuntimeException if an error occurred during parameters injection (most probably during the conversion)
	 * @see RestartResponseException
	 */
	RestartResponseException newRestartResponseException() throws LinkInvalidTargetRuntimeException,
			LinkParameterValidationRuntimeException, LinkParameterInjectionRuntimeException;

	/**
	 * Creates a {@link RestartResponseAtInterceptPageException} with the same page and parameters than this link descriptor.
	 * @throws LinkInvalidTargetRuntimeException if the target page was invalid (null, for example)
	 * @throws LinkParameterValidationRuntimeException if the parameters validation returned an error
	 * @throws LinkParameterInjectionRuntimeException if an error occurred during parameters injection (most probably during the conversion)
	 * @see RestartResponseAtInterceptPageException
	 */
	RestartResponseAtInterceptPageException newRestartResponseAtInterceptPageException()
			throws LinkInvalidTargetRuntimeException, LinkParameterValidationRuntimeException,
			LinkParameterInjectionRuntimeException;

	/**
	 * Creates a {@link RedirectToUrlException} with the same page and parameters than this link descriptor.
	 * @throws LinkInvalidTargetRuntimeException if the target page was invalid (null, for example)
	 * @throws LinkParameterValidationRuntimeException if the parameters validation returned an error
	 * @throws LinkParameterInjectionRuntimeException if an error occurred during parameters injection (most probably during the conversion)
	 * @see RedirectToUrlException
	 */
	RedirectToUrlException newRedirectToUrlException() throws LinkInvalidTargetRuntimeException,
			LinkParameterValidationRuntimeException, LinkParameterInjectionRuntimeException;

	/**
	 * Creates a {@link RedirectToUrlException} with the same page and parameters than this link descriptor.
	 * @throws LinkInvalidTargetRuntimeException if the target page was invalid (null, for example)
	 * @throws LinkParameterValidationRuntimeException if the parameters validation returned an error
	 * @throws LinkParameterInjectionRuntimeException if an error occurred during parameters injection (most probably during the conversion)
	 * @see RedirectToUrlException
	 */
	RedirectToUrlException newRedirectToUrlException(String anchor) throws LinkInvalidTargetRuntimeException,
			LinkParameterValidationRuntimeException, LinkParameterInjectionRuntimeException;

	/**
	 * Returns a NavigationMenyItem with the same page and parameters than this link descriptor.
	 * @throws LinkInvalidTargetRuntimeException if the target page was invalid (null, for example)
	 * @throws LinkParameterValidationRuntimeException if the parameters validation returned an error
	 * @see NavigationMenuItem
	 */
	NavigationMenuItem navigationMenuItem(IModel<String> labelModel) throws LinkInvalidTargetRuntimeException,
			LinkParameterValidationRuntimeException;

	/**
	 * Returns a NavigationMenyItem with the same page and parameters than this link descriptor.
	 * @throws LinkInvalidTargetRuntimeException if the target page was invalid (null, for example)
	 * @throws LinkParameterValidationRuntimeException if the parameters validation returned an error
	 * @see NavigationMenuItem
	 */
	NavigationMenuItem navigationMenuItem(IModel<String> labelModel, Collection<NavigationMenuItem> subMenuItems)
			throws LinkInvalidTargetRuntimeException, LinkParameterValidationRuntimeException;

	/**
	 * Returns true if the target page is accessible by checking it against the authorization strategy defined in Wicket.
	 * @return
	 */
	boolean isAccessible();

}
