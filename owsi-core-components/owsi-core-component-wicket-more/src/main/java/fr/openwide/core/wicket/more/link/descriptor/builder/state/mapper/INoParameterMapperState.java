package fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.IParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IOneParameterLinkDescriptorMapper;
import fr.openwide.core.wicket.more.link.descriptor.mapper.ITwoParameterLinkDescriptorMapper;

/**
 * A builder state from which one may:
 * <ul>
 *  <li> build a LinkDescriptor directly, by calling any of the {@link IParameterMappingState} methods.
 *  <li> build a LinkDescriptorMapper (for example {@link IOneParameterLinkDescriptorMapper} or {@link ITwoParameterLinkDescriptorMapper}),
 *  by calling the {@link #addDynamicParameter(Class)} method.
 * </ul>
 *
 * @param <L>
 */
public interface INoParameterMapperState<L> extends IParameterMappingState<L> {

	<T1> IOneParameterMapperState<L, T1> addDynamicParameter(Class<? super T1> clazz);

}
