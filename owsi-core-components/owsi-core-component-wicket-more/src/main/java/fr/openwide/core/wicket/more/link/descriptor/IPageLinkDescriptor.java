package fr.openwide.core.wicket.more.link.descriptor;

import java.util.Collection;

import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;

import fr.openwide.core.wicket.more.link.descriptor.parameter.injector.LinkParameterInjectionRuntimeException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationRuntimeException;
import fr.openwide.core.wicket.more.markup.html.template.model.NavigationMenuItem;

/**
 * An {@link ILinkDescriptor} pointing to a {@link Page}.
 * @see ILinkDescriptor
 */
public interface IPageLinkDescriptor extends ILinkDescriptor {
	
	/**
	 * Attempts to extract the page parameters, {@link #newRestartResponseException() throwing a RestartResponseException}
	 * with the provided fallback link if any {@link Exception} is caught.
	 * <p>If an exception is caught, it is {@link Logger logged} at error level on the {@link IPageLinkDescriptor} class logger.
	 * @see #extract(PageParameters)
	 */
	void extractSafely(PageParameters parameters, IPageLinkDescriptor fallbackLink) throws RestartResponseException;
	
	/**
	 * Attempts to extract the page parameters, {@link #newRestartResponseException() throwing a RestartResponseException}
	 * with the provided fallback link if any {@link Exception} is caught. The provided error message is added to the session.
	 * <p>If an exception is caught, it is {@link Logger logged} at error level on the {@link IPageLinkDescriptor} class logger.
	 * @see #extract(PageParameters)
	 */
	void extractSafely(PageParameters parameters, IPageLinkDescriptor fallbackLink, String errorMessage)
			throws RestartResponseException;
	
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
	void setResponsePage() throws LinkInvalidTargetRuntimeException, LinkParameterValidationRuntimeException, LinkParameterInjectionRuntimeException;

	/**
	 * Creates a {@link RestartResponseException} with the same page and parameters than this link descriptor.
	 * @throws LinkInvalidTargetRuntimeException if the target page was invalid (null, for example)
	 * @throws LinkParameterValidationRuntimeException if the parameters validation returned an error
	 * @throws LinkParameterInjectionRuntimeException if an error occurred during parameters injection (most probably during the conversion)
	 * @see RestartResponseException
	 */
	RestartResponseException newRestartResponseException() throws LinkInvalidTargetRuntimeException, LinkParameterValidationRuntimeException, LinkParameterInjectionRuntimeException;

	/**
	 * Creates a {@link RestartResponseAtInterceptPageException} with the same page and parameters than this link descriptor.
	 * @throws LinkInvalidTargetRuntimeException if the target page was invalid (null, for example)
	 * @throws LinkParameterValidationRuntimeException if the parameters validation returned an error
	 * @throws LinkParameterInjectionRuntimeException if an error occurred during parameters injection (most probably during the conversion)
	 * @see RestartResponseAtInterceptPageException
	 */
	RestartResponseAtInterceptPageException newRestartResponseAtInterceptPageException() throws LinkInvalidTargetRuntimeException, LinkParameterValidationRuntimeException, LinkParameterInjectionRuntimeException;
	
	/**
	 * Creates a {@link RedirectToUrlException} with the same page and parameters than this link descriptor.
	 * @throws LinkInvalidTargetRuntimeException if the target page was invalid (null, for example)
	 * @throws LinkParameterValidationRuntimeException if the parameters validation returned an error
	 * @throws LinkParameterInjectionRuntimeException if an error occurred during parameters injection (most probably during the conversion)
	 * @see RedirectToUrlException
	 */
	RedirectToUrlException newRedirectToUrlException() throws LinkInvalidTargetRuntimeException, LinkParameterValidationRuntimeException, LinkParameterInjectionRuntimeException;
	
	/**
	 * Creates a {@link RedirectToUrlException} with the same page and parameters than this link descriptor.
	 * @throws LinkInvalidTargetRuntimeException if the target page was invalid (null, for example)
	 * @throws LinkParameterValidationRuntimeException if the parameters validation returned an error
	 * @throws LinkParameterInjectionRuntimeException if an error occurred during parameters injection (most probably during the conversion)
	 * @see RedirectToUrlException
	 */
	RedirectToUrlException newRedirectToUrlException(String anchor) throws LinkInvalidTargetRuntimeException, LinkParameterValidationRuntimeException, LinkParameterInjectionRuntimeException;
	
	/**
	 * Returns a NavigationMenyItem with the same page and parameters than this link descriptor.
	 * @throws LinkInvalidTargetRuntimeException if the target page was invalid (null, for example)
	 * @throws LinkParameterValidationRuntimeException if the parameters validation returned an error
	 * @see NavigationMenuItem
	 */
	NavigationMenuItem navigationMenuItem(IModel<String> labelModel) throws LinkInvalidTargetRuntimeException, LinkParameterValidationRuntimeException;
	
	/**
	 * Returns a NavigationMenyItem with the same page and parameters than this link descriptor.
	 * @throws LinkInvalidTargetRuntimeException if the target page was invalid (null, for example)
	 * @throws LinkParameterValidationRuntimeException if the parameters validation returned an error
	 * @see NavigationMenuItem
	 */
	NavigationMenuItem navigationMenuItem(IModel<String> labelModel, Collection<NavigationMenuItem> subMenuItems)
			throws LinkInvalidTargetRuntimeException, LinkParameterValidationRuntimeException;
	
	/**
	 * Returns true if the page is accessible by checking it against the authorization strategy defined in Wicket.
	 * @return
	 */
	boolean isAccessible();

}
