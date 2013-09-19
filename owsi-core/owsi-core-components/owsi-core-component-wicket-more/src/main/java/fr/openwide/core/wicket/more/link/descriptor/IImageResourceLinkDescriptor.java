package fr.openwide.core.wicket.more.link.descriptor;

import org.apache.wicket.request.resource.ResourceReference;

/**
 * An {@link ILinkDescriptor} pointing to a {@link ResourceReference} that is an image.
 * @see {@link ILinkDescriptor}, {@link IResourceLinkDescriptor}
 */
public interface IImageResourceLinkDescriptor extends ILinkDescriptor {
	
	/**
	 * Creates a {@link DynamicImage} that points to the same resource than this descriptor, with the same parameters.
	 * <p><strong>Note:</strong> special conditions apply to the rendering of this image if the parameters are invalid.
	 * See {@link DynamicImage} for more information.
	 * @return A {@link DynamicImage} matching this link descriptor.
	 * @see DynamicImage
	 */
	DynamicImage image(String wicketId);

}
