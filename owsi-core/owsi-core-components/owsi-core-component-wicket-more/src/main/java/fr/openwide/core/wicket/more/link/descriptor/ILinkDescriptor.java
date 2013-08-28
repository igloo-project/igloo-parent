package fr.openwide.core.wicket.more.link.descriptor;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.link.descriptor.parameter.extractor.ILinkParametersExtractor;
import fr.openwide.core.wicket.more.link.descriptor.parameter.extractor.LinkParameterExtractionRuntimeException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.injector.LinkParameterInjectionRuntimeException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationException;

/**
 * An utility object mapped to {@link IModel models}, that allows for simple manipulation around the concept of linking.
 * <p>Object implementing this interface, and its sub-interfaces ({@link IResourceLinkDescriptor}, {@link IPageLinkDescriptor}),
 * can be instantiated using the {@link LinkDescriptorBuilder}.
 * <p>Re-implementing this interface is not recommended, as it may be extended with additional methods without prior notice.
 * <p><strong>Warning:</strong> this interface extends {@link IDetachable}. Thus, it <em>must</em> be detached before serialization.
 */
public interface ILinkDescriptor extends ILinkParametersExtractor, IDetachable {

	/**
	 * Creates an {@link AbstractDynamicBookmarkableLink} that points to the same page/resource than this descriptor, with the same parameters.
	 * <p><strong>Note:</strong> special conditions apply to the rendering of this link if the parameters are invalid.
	 * See {@link AbstractDynamicBookmarkableLink} for more information.
	 * @return An {@link AbstractDynamicBookmarkableLink} matching this link descriptor.
	 * @see AbstractDynamicBookmarkableLink
	 */
	AbstractDynamicBookmarkableLink link(String wicketId);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	void extract(PageParameters parameters) throws LinkParameterValidationException, LinkParameterExtractionRuntimeException;
	
	/**
	 * Renders the full URL for this link descriptor.
	 * <p>The resulting string includes protocol ("http://"), host, port, and path, as well as query parameters ("?arg0=true"), if any.
	 * @return The full URL for this link descriptor.
	 * @throws LinkParameterValidationException if the parameters validation returned an error
	 * @throws LinkParameterInjectionRuntimeException if an error occurred during parameters injection (most probably during the conversion)
	 */
	String fullUrl() throws LinkParameterValidationException, LinkParameterInjectionRuntimeException;
	
	/**
	 * Renders the full URL for this link descriptor.
	 * <p>The resulting string includes protocol ("http://"), host, port, and path, as well as query parameters ("?arg0=true"), if any.
	 * @return The full URL for this link descriptor.
	 * @throws LinkParameterValidationException if the parameters validation returned an error
	 * @throws LinkParameterInjectionRuntimeException if an error occurred during parameters injection (most probably during the conversion)
	 */
	String fullUrl(RequestCycle requestCycle) throws LinkParameterValidationException, LinkParameterInjectionRuntimeException;

}
