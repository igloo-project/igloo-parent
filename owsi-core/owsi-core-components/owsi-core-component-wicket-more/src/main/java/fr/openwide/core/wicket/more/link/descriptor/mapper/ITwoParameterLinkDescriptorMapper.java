package fr.openwide.core.wicket.more.link.descriptor.mapper;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

import com.google.common.base.Function;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.INoMappableParameterMainState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.IOneMappableParameterMainState;
import fr.openwide.core.wicket.more.link.descriptor.parameter.extractor.ILinkParametersExtractor;

/**
 * An object that can create a {@link ILinkParametersExtractor} using two {@link IModel}.
 * @see INoMappableParameterMainState#addDynamicParameter(Class)
 * @see IOneMappableParameterMainState#addDynamicParameter(Class)
 */
public interface ITwoParameterLinkDescriptorMapper<L, T1, T2> extends IDetachable {
	
	L map(IModel<T1> model1, IModel<T2> model2);

	IOneParameterLinkDescriptorMapper<L, T2> setParameter1(final IModel<T1> model1);

	IOneParameterLinkDescriptorMapper<L, T2> setParameter1(Function<T2, T1> function);

	<U1 extends T1> ITwoParameterLinkDescriptorMapper<L, U1, T2> castParameter1();

	IOneParameterLinkDescriptorMapper<L, T2> ignoreParameter1();

	IOneParameterLinkDescriptorMapper<L, T1> setParameter2(final IModel<T2> model2);

	IOneParameterLinkDescriptorMapper<L, T1> setParameter2(Function<T1, T2> function);

	<U2 extends T2> ITwoParameterLinkDescriptorMapper<L, T1, U2> castParameter2();

	IOneParameterLinkDescriptorMapper<L, T1> ignoreParameter2();

}
