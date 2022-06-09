package org.iglooproject.wicket.more.link.descriptor.builder.impl.factory;

import org.javatuples.Tuple;

import igloo.wicket.factory.IDetachableFactory;

import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;

/**
 * A factory of link descriptors to be provided to link descriptor mappers built through {@link LinkDescriptorBuilder}.
 * @param <TTarget> The link target type (<code>Class<? extends Page></code>, <code>ResourceReference</code>, ...)
 * @param <TLinkDescriptor> The link descriptor type.
 */
public interface IBuilderMapperLinkDescriptorFactory<TLinkDescriptor> extends IDetachableFactory<Tuple, TLinkDescriptor> {

}