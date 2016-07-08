package fr.openwide.core.wicket.more.link.descriptor.mapper;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.INoMappableParameterMainState;
import fr.openwide.core.wicket.more.link.descriptor.parameter.extractor.ILinkParametersExtractor;

/**
 * An object that can create a {@link ILinkParametersExtractor} using one {@link IModel}.
 * @see INoMappableParameterMainState#addDynamicParameter(Class)
 */
public interface IOneParameterLinkDescriptorMapper<L, T> extends IDetachable {
	
	L map(IModel<T> model);

}
