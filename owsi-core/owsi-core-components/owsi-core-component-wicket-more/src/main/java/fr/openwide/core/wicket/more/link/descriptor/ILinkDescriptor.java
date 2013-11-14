package fr.openwide.core.wicket.more.link.descriptor;

import org.apache.wicket.model.IDetachable;

import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.link.descriptor.generator.ILinkGenerator;
import fr.openwide.core.wicket.more.link.descriptor.parameter.extractor.ILinkParametersExtractor;

/**
 * A utility object implementing both {@link ILinkDescriptor} and {@link ILinkParametersExtractor}.
 * <p>Object implementing this interface, and its sub-interfaces ({@link IResourceLinkDescriptor}, {@link IPageLinkDescriptor}),
 * can be instantiated using the {@link LinkDescriptorBuilder}.
 * 
 * <p>Re-implementing this interface is not recommended, as it may be extended with additional methods without prior notice.
 * <p><strong>Warning:</strong> this interface extends {@link IDetachable}. Thus, it <em>must</em> be detached before serialization.
 * 
 * @see ILinkDescriptor
 * @see ILinkParametersExtractor
 */
public interface ILinkDescriptor extends ILinkParametersExtractor, ILinkGenerator, IDetachable {

}
