package org.iglooproject.wicket.more.link.descriptor.generator;

import org.apache.wicket.Component;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.request.resource.ResourceReference;
import org.iglooproject.wicket.more.link.descriptor.DynamicImage;

/**
 * An {@link ILinkGenerator} pointing to a {@link ResourceReference} that is an image.
 *
 * <p>Re-implementing this interface is not recommended, as it may be extended with additional
 * methods without prior notice.
 *
 * <p><strong>Warning:</strong> this interface extends {@link IDetachable}. Thus, it <em>must</em>
 * be detached before serialization.
 *
 * @see ILinkGenerator
 */
public interface IImageResourceLinkGenerator extends ILinkGenerator, IDetachable {

  /**
   * Creates a {@link DynamicImage} that points to the same resource than this descriptor, with the
   * same parameters.
   *
   * <p><strong>Note:</strong> special conditions apply to the rendering of this image if the
   * parameters are invalid. See {@link DynamicImage} for more information.
   *
   * @return A {@link DynamicImage} matching this link descriptor.
   * @see DynamicImage
   */
  DynamicImage image(String wicketId);

  /** {@inheritDoc} */
  @Override
  IImageResourceLinkGenerator wrap(Component component);

  /**
   * @see #chain(ILinkGenerator)
   */
  IImageResourceLinkGenerator chain(IImageResourceLinkGenerator other);
}
