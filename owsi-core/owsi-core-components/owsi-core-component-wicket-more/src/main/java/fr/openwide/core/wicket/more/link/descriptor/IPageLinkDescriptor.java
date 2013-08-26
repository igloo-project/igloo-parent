package fr.openwide.core.wicket.more.link.descriptor;

import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.cycle.RequestCycle;

import fr.openwide.core.wicket.more.link.descriptor.parameter.injector.LinkParameterInjectionRuntimeException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationRuntimeException;
import fr.openwide.core.wicket.more.markup.html.template.model.NavigationMenuItem;

/**
 * An {@link ILinkDescriptor} pointing to a {@link Page}.
 * @see ILinkDescriptor
 */
public interface IPageLinkDescriptor extends ILinkDescriptor {
	
	/**
	 * Sets the response page and parameters for the current {@link RequestCycle} to the value of this link descriptor.
	 * @throws LinkParameterValidationRuntimeException if the parameters validation returned an error
	 * @throws LinkParameterInjectionRuntimeException if an error occurred during parameters injection (most probably during the conversion)
	 * @see RequestCycle#setResponsePage(Class, org.apache.wicket.request.mapper.parameter.PageParameters)
	 */
	void setResponsePage() throws LinkParameterValidationRuntimeException, LinkParameterInjectionRuntimeException;

	/**
	 * Creates a {@link RestartResponseException} with the same page and parameters than this link descriptor.
	 * @throws LinkParameterValidationRuntimeException if the parameters validation returned an error
	 * @throws LinkParameterInjectionRuntimeException if an error occurred during parameters injection (most probably during the conversion)
	 * @see RestartResponseException
	 */
	RestartResponseException newRestartResponseException() throws LinkParameterValidationRuntimeException, LinkParameterInjectionRuntimeException;

	/**
	 * Creates a {@link RestartResponseAtInterceptPageException} with the same page and parameters than this link descriptor.
	 * @throws LinkParameterValidationRuntimeException if the parameters validation returned an error
	 * @throws LinkParameterInjectionRuntimeException if an error occurred during parameters injection (most probably during the conversion)
	 * @see RestartResponseAtInterceptPageException
	 */
	RestartResponseAtInterceptPageException newRestartResponseAtInterceptPageException() throws LinkParameterValidationRuntimeException, LinkParameterInjectionRuntimeException;
	
	/**
	 * Returns a NavigationMenyItem with the same page and parameters than this link descriptor.
	 * @throws LinkParameterValidationRuntimeException if the parameters validation returned an error
	 * @see NavigationMenuItem
	 */
	NavigationMenuItem navigationMenuItem(IModel<String> labelModel) throws LinkParameterValidationRuntimeException;
	
	/**
	 * Returns true if the page is accessible by checking it against the authorization strategy defined in Wicket.
	 * @return
	 */
	boolean isAccessible();

}
