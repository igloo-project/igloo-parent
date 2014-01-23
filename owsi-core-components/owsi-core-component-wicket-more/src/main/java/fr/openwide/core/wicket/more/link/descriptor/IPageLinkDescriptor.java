package fr.openwide.core.wicket.more.link.descriptor;

import org.apache.wicket.Page;
import org.apache.wicket.model.IDetachable;

import fr.openwide.core.wicket.more.link.descriptor.generator.IPageLinkGenerator;
import fr.openwide.core.wicket.more.link.descriptor.parameter.extractor.IPageLinkParametersExtractor;

/**
 * An {@link ILinkDescriptor} pointing to a {@link Page}.
 * 
 * <p>Re-implementing this interface is not recommended, as it may be extended with additional methods without prior notice.
 * <p><strong>Warning:</strong> this interface extends {@link IDetachable}. Thus, it <em>must</em> be detached before serialization.
 * 
 * @see ILinkDescriptor
 * @see IPageLinkGenerator
 * @see IPageLinkParametersExtractor
 */
public interface IPageLinkDescriptor extends ILinkDescriptor, IPageLinkGenerator, IPageLinkParametersExtractor, IDetachable {

}
