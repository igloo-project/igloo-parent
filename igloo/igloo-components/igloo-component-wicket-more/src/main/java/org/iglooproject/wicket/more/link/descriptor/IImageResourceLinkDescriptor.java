package org.iglooproject.wicket.more.link.descriptor;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.request.resource.ResourceReference;
import org.iglooproject.wicket.more.link.descriptor.generator.IImageResourceLinkGenerator;

/**
 * An {@link ILinkDescriptor} pointing to a {@link ResourceReference} that is an image.
 *
 * <p>Re-implementing this interface is not recommended, as it may be extended with additional
 * methods without prior notice.
 *
 * <p><strong>Warning:</strong> this interface extends {@link IDetachable}. Thus, it <em>must</em>
 * be detached before serialization.
 *
 * @see ILinkDescriptor
 * @see IResourceLinkDescriptor
 * @see IImageResourceLinkGenerator
 */
public interface IImageResourceLinkDescriptor
    extends IResourceLinkDescriptor, IImageResourceLinkGenerator, IDetachable {}
