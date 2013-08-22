package fr.openwide.core.wicket.more.link.descriptor.parameter.extractor;

import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationException;

/**
 * An utility object mapped to {@link IModel models}, that allows for simple link parameters extraction to this models.
 * <p><strong>Warning:</strong> this interface extends {@link IDetachable}. Thus, it <em>must</em> be detached before serialization.
 */
public interface ILinkParametersExtractor extends IDetachable {

	/**
	 * Extracts the given parameters to the underlying models.
	 * <p>The underlying model must handle the {@link IModel#setObject(Object)} operation, even if they are non-wrapped-yet {@link IComponentAssignedModel}.
	 * Otherwise, the behavior is undefined.
	 * @throws LinkParameterValidationException if the parameters validation returned an error
	 * @throws LinkParameterExtractionRuntimeException if an error occurred during parameters extraction (most probably during the conversion)
	 */
	void extract(PageParameters parameters) throws LinkParameterValidationException, LinkParameterExtractionRuntimeException;

}