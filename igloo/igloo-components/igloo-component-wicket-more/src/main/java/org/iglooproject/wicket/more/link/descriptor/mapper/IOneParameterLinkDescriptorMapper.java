package org.iglooproject.wicket.more.link.descriptor.mapper;

import org.apache.wicket.model.IModel;

import org.iglooproject.wicket.more.link.descriptor.ILinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.common.IMappableParameterDeclarationState;

/**
 * An object that can create a {@link ILinkDescriptor} using one {@link IModel}.
 * @see ILinkDescriptorMapper
 * @see IMappableParameterDeclarationState#model(Class)
 */
public interface IOneParameterLinkDescriptorMapper<L, T> extends ILinkDescriptorMapper<L, IModel<T>> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	L map(IModel<T> model);

	<U1 extends T> IOneParameterLinkDescriptorMapper<L, U1> castParameter1();

}
