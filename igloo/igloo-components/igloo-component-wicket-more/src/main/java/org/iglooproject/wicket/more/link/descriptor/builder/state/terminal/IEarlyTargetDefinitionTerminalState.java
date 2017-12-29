package org.iglooproject.wicket.more.link.descriptor.builder.state.terminal;

import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;


/**
 * @deprecated This is only useful if you used the deprecated
 * {@link LinkDescriptorBuilder#LinkDescriptorBuilder() LinkDescriptorBuilder constructor}. See
 * {@link LinkDescriptorBuilder}.
 */
@Deprecated
public interface IEarlyTargetDefinitionTerminalState<Result> {
	
	/**
	 * @return The result of the build, which is a link descriptor or a link descriptor mapper pointing to the target
	 * that was previously defined with {@link LinkDescriptorBuilder#page(Class)},
	 * {@link LinkDescriptorBuilder#page(org.apache.wicket.model.IModel)},
	 * {@link LinkDescriptorBuilder#resource(org.apache.wicket.request.resource.ResourceReference)}, etc.
	 */
	Result build();

}
