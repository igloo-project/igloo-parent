package fr.openwide.core.wicket.more.link.descriptor.builder.impl.factory;

import org.javatuples.Tuple;

import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.markup.html.factory.IDetachableFactory;

/**
 * A factory of link descriptors to be provided to link descriptor mappers built through {@link LinkDescriptorBuilder}.
 * @param <TTarget> The link target type (<code>Class<? extends Page></code>, <code>ResourceReference</code>, ...)
 * @param <TLinkDescriptor> The link descriptor type.
 */
public interface IBuilderMapperLinkDescriptorFactory<TLinkDescriptor> extends IDetachableFactory<Tuple, TLinkDescriptor> {

}