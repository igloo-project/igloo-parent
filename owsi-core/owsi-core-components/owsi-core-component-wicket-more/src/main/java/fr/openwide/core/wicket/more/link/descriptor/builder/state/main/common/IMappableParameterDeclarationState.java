package fr.openwide.core.wicket.more.link.descriptor.builder.state.main.common;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.INoMappableParameterMainState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IChosenParameterState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IOneChosenParameterState;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IOneParameterLinkDescriptorMapper;

/**
 * A state from which one can declare a mappable parameter.
 */
public interface IMappableParameterDeclarationState {
	
	/**
	 * Declare a mappable parameter that can be provided later through a
	 * {@link IOneParameterLinkDescriptorMapper#map(org.apache.wicket.model.IModel) link descriptor mapper},
	 * but can still be referenced when building through methods such as {@link IOneChosenParameterState#map(String)} or
	 * {@link IChosenParameterState#map(fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.factory.ILinkParameterMappingEntryFactory)}
	 * @param clazz The type of the parameter on the Java side, i.e. the type of objects that will be contained in models
	 * passed to the {@link IOneParameterLinkDescriptorMapper#map(org.apache.wicket.model.IModel) link descriptor mapper}.
	 * @return A builder state that allows for referencing the newly-added parameter. The exact type of this state is
	 * defined in extending interfaces such as {@link INoMappableParameterMainState}.
	 */
	<TParam> IMainState<?> model(Class<? super TParam> clazz);
	
}
