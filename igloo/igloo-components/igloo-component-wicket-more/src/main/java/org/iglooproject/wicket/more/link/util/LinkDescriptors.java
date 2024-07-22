package org.iglooproject.wicket.more.link.util;

import org.apache.wicket.model.IDetachable;
import org.iglooproject.wicket.more.link.descriptor.generator.IImageResourceLinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.generator.ILinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.generator.IPageLinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.impl.DynamicBookmarkableLink;
import org.iglooproject.wicket.more.link.descriptor.mapper.ILinkDescriptorMapper;

public final class LinkDescriptors {

  private LinkDescriptors() {}

  /**
   * Returns an always-invalid link generator, i.e. one that will generate an always-disabled {@link
   * DynamicBookmarkableLink} and will always throw exceptions when asked to render the URL or to
   * redirect.
   *
   * <p>The returned generator implements {@link ILinkGenerator}, {@link IPageLinkGenerator}
   * <em>and</em> {@link IImageResourceLinkGenerator}.
   */
  public static InvalidLinkGenerator invalid() {
    return INVALID;
  }

  private static final InvalidLinkGenerator INVALID =
      new InvalidLinkGenerator() {
        private static final long serialVersionUID = 1L;

        private Object readResolve() {
          return LinkDescriptors.INVALID;
        }
      };

  /**
   * Returns a mapper that will always return an invalid link generator.
   *
   * @see LinkDescriptors#invalid()
   */
  public static ILinkDescriptorMapper<InvalidLinkGenerator, Object> invalidMapper() {
    return constantMapper(INVALID);
  }

  /** Returns a mapper that will only return the given link descriptor, regardless of parameters. */
  public static <L> ILinkDescriptorMapper<L, Object> constantMapper(L descriptor) {
    return new ConstantLinkDescriptorMapper<>(descriptor);
  }

  private static class ConstantLinkDescriptorMapper<L> implements ILinkDescriptorMapper<L, Object> {

    private static final long serialVersionUID = -4926581483390516183L;

    private final L result;

    public ConstantLinkDescriptorMapper(L result) {
      super();
      this.result = result;
    }

    @Override
    public void detach() {
      if (result instanceof IDetachable) {
        ((IDetachable) result).detach();
      }
    }

    @Override
    public L map(Object param) {
      return result;
    }
  }
}
