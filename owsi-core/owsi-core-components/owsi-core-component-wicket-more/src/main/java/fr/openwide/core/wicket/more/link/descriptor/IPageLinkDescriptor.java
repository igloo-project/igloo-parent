package fr.openwide.core.wicket.more.link.descriptor;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.request.cycle.RequestCycle;

import fr.openwide.core.wicket.more.link.descriptor.validator.ParameterValidationException;

public interface IPageLinkDescriptor extends ILinkDescriptor {
	
	/**
	 * Sets the response page and parameters for the current {@link RequestCycle} to the value of this link descriptor.
	 * @throws ParameterValidationException if the parameters validation returned an error
	 * @see RequestCycle#setResponsePage(Class, org.apache.wicket.request.mapper.parameter.PageParameters)
	 */
	void setResponsePage() throws ParameterValidationException;

	/**
	 * Creates a {@link RestartResponseException} with the same page and parameters than this link descriptor.
	 * @throws ParameterValidationException if the parameters validation returned an error
	 * @see RestartResponseException
	 */
	RestartResponseException newRestartResponseException() throws ParameterValidationException;

	/**
	 * Creates a {@link RestartResponseAtInterceptPageException} with the same page and parameters than this link descriptor.
	 * @throws ParameterValidationException if the parameters validation returned an error
	 * @see RestartResponseAtInterceptPageException
	 */
	RestartResponseAtInterceptPageException newRestartResponseAtInterceptPageException() throws ParameterValidationException;

}
