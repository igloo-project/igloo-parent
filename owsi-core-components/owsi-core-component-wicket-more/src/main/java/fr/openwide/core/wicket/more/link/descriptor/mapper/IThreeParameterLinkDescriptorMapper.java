package fr.openwide.core.wicket.more.link.descriptor.mapper;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.INoParameterMapperState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.IOneParameterMapperState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.ITwoParameterMapperState;
import fr.openwide.core.wicket.more.link.descriptor.parameter.extractor.ILinkParametersExtractor;

/**
 * An object that can create a {@link ILinkParametersExtractor} using three {@link IModel}.
 * @see INoParameterMapperState#addDynamicParameter(Class)
 * @see IOneParameterMapperState#addDynamicParameter(Class)
 * @see ITwoParameterMapperState#addDynamicParameter(Class)
 */
public interface IThreeParameterLinkDescriptorMapper<L, T1, T2, T3> extends IDetachable {
	
	L map(IModel<T1> model1, IModel<T2> model2, IModel<T3> model3);

	ITwoParameterLinkDescriptorMapper<L, T2, T3> setParameter1(final IModel<T1> model1);

	ITwoParameterLinkDescriptorMapper<L, T1, T3> setParameter2(final IModel<T2> model2);

	ITwoParameterLinkDescriptorMapper<L, T1, T2> setParameter3(final IModel<T3> model3);

}
