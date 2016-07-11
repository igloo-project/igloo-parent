package fr.openwide.core.wicket.more.link.descriptor.mapper;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.javatuples.Pair;

import com.google.common.base.Function;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.INoMappableParameterMainState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.IOneMappableParameterMainState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.ITwoMappableParameterMainState;
import fr.openwide.core.wicket.more.link.descriptor.parameter.extractor.ILinkParametersExtractor;

/**
 * An object that can create a {@link ILinkParametersExtractor} using three {@link IModel}.
 * @see INoMappableParameterMainState#addDynamicParameter(Class)
 * @see IOneMappableParameterMainState#addDynamicParameter(Class)
 * @see ITwoMappableParameterMainState#addDynamicParameter(Class)
 */
public interface IThreeParameterLinkDescriptorMapper<L, T1, T2, T3> extends IDetachable {
	
	L map(IModel<T1> model1, IModel<T2> model2, IModel<T3> model3);

	ITwoParameterLinkDescriptorMapper<L, T2, T3> setParameter1(final IModel<T1> model1);

	ITwoParameterLinkDescriptorMapper<L, T2, T3> setParameter1(Function<Pair<T2, T3>, T1> function);

	<U1 extends T1> IThreeParameterLinkDescriptorMapper<L, U1, T2, T3> castParameter1();

	ITwoParameterLinkDescriptorMapper<L, T2, T3> ignoreParameter1();

	ITwoParameterLinkDescriptorMapper<L, T1, T3> setParameter2(final IModel<T2> model2);

	ITwoParameterLinkDescriptorMapper<L, T1, T3> setParameter2(Function<Pair<T1, T3>, T2> function);

	<U2 extends T2> IThreeParameterLinkDescriptorMapper<L, T1, U2, T3> castParameter2();

	ITwoParameterLinkDescriptorMapper<L, T1, T3> ignoreParameter2();

	ITwoParameterLinkDescriptorMapper<L, T1, T2> setParameter3(final IModel<T3> model3);

	ITwoParameterLinkDescriptorMapper<L, T1, T2> setParameter3(Function<Pair<T1, T2>, T3> function);

	<U3 extends T3> IThreeParameterLinkDescriptorMapper<L, T1, T2, U3> castParameter3();

	ITwoParameterLinkDescriptorMapper<L, T1, T2> ignoreParameter3();

}
