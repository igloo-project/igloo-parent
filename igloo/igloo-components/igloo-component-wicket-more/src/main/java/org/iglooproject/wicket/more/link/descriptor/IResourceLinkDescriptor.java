package org.iglooproject.wicket.more.link.descriptor;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.request.resource.ResourceReference;

/**
 * An {@link ILinkDescriptor} pointing to a {@link ResourceReference}.
 *
 * <p>Re-implementing this interface is not recommended, as it may be extended with additional
 * methods without prior notice.
 *
 * <p><strong>Warning:</strong> this interface extends {@link IDetachable}. Thus, it <em>must</em>
 * be detached before serialization.
 *
 * @see ILinkDescriptor
 */
public interface IResourceLinkDescriptor extends ILinkDescriptor, IDetachable {}
