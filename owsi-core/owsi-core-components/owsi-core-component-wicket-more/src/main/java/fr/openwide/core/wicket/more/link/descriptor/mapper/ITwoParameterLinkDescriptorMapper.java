package fr.openwide.core.wicket.more.link.descriptor.mapper;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.INoParameterMapperState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.IOneParameterMapperState;
import fr.openwide.core.wicket.more.link.descriptor.parameter.extractor.ILinkParametersExtractor;

/**
 * An object that can create a {@link ILinkParametersExtractor} using two {@link IModel}.
 * @see INoParameterMapperState#addDynamicParameter(Class)
 * @see IOneParameterMapperState#addDynamicParameter(Class)
 */
public interface ITwoParameterLinkDescriptorMapper<L, T1, T2> extends IDetachable {
	
	L map(IModel<T1> model1, IModel<T2> model2);

	IOneParameterLinkDescriptorMapper<L, T2> setParameter1(final IModel<T1> model1);

	IOneParameterLinkDescriptorMapper<L, T2> ignoreParameter1();

	IOneParameterLinkDescriptorMapper<L, T1> setParameter2(final IModel<T2> model2);

	IOneParameterLinkDescriptorMapper<L, T1> ignoreParameter2();

}
