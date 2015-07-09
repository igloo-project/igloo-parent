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
public interface IFourParameterLinkDescriptorMapper<L, T1, T2, T3, T4> extends IDetachable {
	
	L map(IModel<T1> model1, IModel<T2> model2, IModel<T3> model3, IModel<T4> model4);

	IThreeParameterLinkDescriptorMapper<L, T2, T3, T4> setParameter1(final IModel<T1> model1);

	IThreeParameterLinkDescriptorMapper<L, T2, T3, T4> ignoreParameter1();

	IThreeParameterLinkDescriptorMapper<L, T1, T3, T4> setParameter2(final IModel<T2> model2);

	IThreeParameterLinkDescriptorMapper<L, T1, T3, T4> ignoreParameter2();

	IThreeParameterLinkDescriptorMapper<L, T1, T2, T4> setParameter3(final IModel<T3> model3);

	IThreeParameterLinkDescriptorMapper<L, T1, T2, T4> ignoreParameter3();

	IThreeParameterLinkDescriptorMapper<L, T1, T2, T3> setParameter4(final IModel<T4> model4);

	IThreeParameterLinkDescriptorMapper<L, T1, T2, T3> ignoreParameter4();

}
