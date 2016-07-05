package fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.IParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IOneParameterLinkDescriptorMapper;
import fr.openwide.core.wicket.more.link.descriptor.mapper.ITwoParameterLinkDescriptorMapper;

/**
 * A builder state from which one may:
 * <ul>
 *  <li> build a LinkDescriptor directly, by calling any of the {@link IParameterMappingState} methods.
 *  <li> build a LinkDescriptorMapper (for example {@link IOneParameterLinkDescriptorMapper} or {@link ITwoParameterLinkDescriptorMapper}),
 *  by calling the {@link #model(Class)} method.
 * </ul>
 *
 * @param <TLinkDescriptor>
 */
public interface INoParameterMapperState<TLinkDescriptor> extends IParameterMappingState<TLinkDescriptor> {

	<TParam1> IOneParameterMapperState<TLinkDescriptor, TParam1> model(Class<? super TParam1> clazz);

}
