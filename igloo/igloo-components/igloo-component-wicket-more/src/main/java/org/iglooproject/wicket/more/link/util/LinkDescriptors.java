package org.iglooproject.wicket.more.link.util;

import igloo.wicket.model.Detachables;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.link.descriptor.IImageResourceLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.impl.DynamicBookmarkableLink;
import org.iglooproject.wicket.more.link.descriptor.mapper.AbstractFourParameterLinkDescriptorMapper;
import org.iglooproject.wicket.more.link.descriptor.mapper.AbstractOneParameterLinkDescriptorMapper;
import org.iglooproject.wicket.more.link.descriptor.mapper.AbstractThreeParameterLinkDescriptorMapper;
import org.iglooproject.wicket.more.link.descriptor.mapper.AbstractTwoParameterLinkDescriptorMapper;
import org.iglooproject.wicket.more.link.descriptor.mapper.IFourParameterLinkDescriptorMapper;
import org.iglooproject.wicket.more.link.descriptor.mapper.ILinkDescriptorMapper;
import org.iglooproject.wicket.more.link.descriptor.mapper.IOneParameterLinkDescriptorMapper;
import org.iglooproject.wicket.more.link.descriptor.mapper.IThreeParameterLinkDescriptorMapper;
import org.iglooproject.wicket.more.link.descriptor.mapper.ITwoParameterLinkDescriptorMapper;
import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.javatuples.Triplet;

public final class LinkDescriptors {

  private LinkDescriptors() {}

  /**
   * Returns an always-invalid link descriptor, i.e. one that will generate an always-disabled
   * {@link DynamicBookmarkableLink} and will always throw exceptions when asked to render the URL
   * or to redirect.
   *
   * <p>The returned descriptor implements {@link IPageLinkDescriptor} <em>and</em> {@link
   * IImageResourceLinkDescriptor}.
   */
  public static InvalidLinkDescriptor invalid() {
    return INVALID;
  }

  private static final InvalidLinkDescriptor INVALID =
      new InvalidLinkDescriptor() {
        private static final long serialVersionUID = 1L;

        private Object readResolve() {
          return LinkDescriptors.INVALID;
        }
      };

  /**
   * Returns a mapper that will always return an invalid link descriptor.
   *
   * @see LinkDescriptors#invalid()
   */
  public static <P> ILinkDescriptorMapper<InvalidLinkDescriptor, P> invalidMapper() {
    return constantMapper(INVALID);
  }

  /** Returns a mapper that will only return the given link descriptor, regardless of parameters. */
  public static <L, P> ILinkDescriptorMapper<L, P> constantMapper(L descriptor) {
    return new ConstantLinkDescriptorMapper<>(descriptor);
  }

  private static class ConstantLinkDescriptorMapper<L, P> implements ILinkDescriptorMapper<L, P> {

    private static final long serialVersionUID = 1L;

    private final L result;

    public ConstantLinkDescriptorMapper(L result) {
      super();
      this.result = result;
    }

    @Override
    public L map(P param) {
      return result;
    }

    @Override
    public void detach() {
      if (result instanceof IDetachable resultDetachable) {
        Detachables.detach(resultDetachable);
      }
    }
  }

  /**
   * Returns a mapper that will always return an invalid link descriptor.
   *
   * @see LinkDescriptors#invalid()
   */
  public static <T1>
      IOneParameterLinkDescriptorMapper<InvalidLinkDescriptor, T1> invalidOneParameterMapper() {
    return constantOneParameterMapper(INVALID);
  }

  /** Returns a mapper that will only return the given link descriptor, regardless of parameters. */
  public static <L, T1> IOneParameterLinkDescriptorMapper<L, T1> constantOneParameterMapper(
      L descriptor) {
    return new ConstantOneParameterLinkDescriptorMapper<>(descriptor);
  }

  private static class ConstantOneParameterLinkDescriptorMapper<L, T1>
      extends AbstractOneParameterLinkDescriptorMapper<L, T1> {

    private static final long serialVersionUID = 1L;

    private final L result;

    public ConstantOneParameterLinkDescriptorMapper(L result) {
      super();
      this.result = result;
    }

    @Override
    public L map(IModel<T1> model) {
      return result;
    }

    @Override
    public void detach() {
      if (result instanceof IDetachable resultDetachable) {
        Detachables.detach(resultDetachable);
      }
    }
  }

  /**
   * Returns a mapper that will always return an invalid link descriptor.
   *
   * @see LinkDescriptors#invalid()
   */
  public static <T1, T2>
      ITwoParameterLinkDescriptorMapper<InvalidLinkDescriptor, T1, T2> invalidTwoParameterMapper() {
    return constantTwoParameterMapper(INVALID);
  }

  /** Returns a mapper that will only return the given link descriptor, regardless of parameters. */
  public static <L, T1, T2> ITwoParameterLinkDescriptorMapper<L, T1, T2> constantTwoParameterMapper(
      L descriptor) {
    return new ConstantTwoParameterLinkDescriptorMapper<>(descriptor);
  }

  private static class ConstantTwoParameterLinkDescriptorMapper<L, T1, T2>
      extends AbstractTwoParameterLinkDescriptorMapper<L, T1, T2> {

    private static final long serialVersionUID = 1L;

    private final L result;

    public ConstantTwoParameterLinkDescriptorMapper(L result) {
      super();
      this.result = result;
    }

    @Override
    public L map(Pair<? extends IModel<T1>, ? extends IModel<T2>> param) {
      return result;
    }

    @Override
    public void detach() {
      if (result instanceof IDetachable resultDetachable) {
        Detachables.detach(resultDetachable);
      }
    }
  }

  /**
   * Returns a mapper that will always return an invalid link descriptor.
   *
   * @see LinkDescriptors#invalid()
   */
  public static <T1, T2, T3>
      IThreeParameterLinkDescriptorMapper<InvalidLinkDescriptor, T1, T2, T3>
          invalidThreeParameterMapper() {
    return constantThreeParameterMapper(INVALID);
  }

  /** Returns a mapper that will only return the given link descriptor, regardless of parameters. */
  public static <L, T1, T2, T3>
      IThreeParameterLinkDescriptorMapper<L, T1, T2, T3> constantThreeParameterMapper(
          L descriptor) {
    return new ConstantThreeParameterLinkDescriptorMapper<>(descriptor);
  }

  private static class ConstantThreeParameterLinkDescriptorMapper<L, T1, T2, T3>
      extends AbstractThreeParameterLinkDescriptorMapper<L, T1, T2, T3> {

    private static final long serialVersionUID = 1L;

    private final L result;

    public ConstantThreeParameterLinkDescriptorMapper(L result) {
      super();
      this.result = result;
    }

    @Override
    public L map(Triplet<? extends IModel<T1>, ? extends IModel<T2>, ? extends IModel<T3>> param) {
      return result;
    }

    @Override
    public void detach() {
      if (result instanceof IDetachable resultDetachable) {
        Detachables.detach(resultDetachable);
      }
    }
  }

  /**
   * Returns a mapper that will always return an invalid link descriptor.
   *
   * @see LinkDescriptors#invalid()
   */
  public static <T1, T2, T3, T4>
      IFourParameterLinkDescriptorMapper<InvalidLinkDescriptor, T1, T2, T3, T4>
          invalidFourParameterMapper() {
    return constantFourParameterMapper(INVALID);
  }

  /** Returns a mapper that will only return the given link descriptor, regardless of parameters. */
  public static <L, T1, T2, T3, T4>
      IFourParameterLinkDescriptorMapper<L, T1, T2, T3, T4> constantFourParameterMapper(
          L descriptor) {
    return new ConstantFourParameterLinkDescriptorMapper<>(descriptor);
  }

  private static class ConstantFourParameterLinkDescriptorMapper<L, T1, T2, T3, T4>
      extends AbstractFourParameterLinkDescriptorMapper<L, T1, T2, T3, T4> {

    private static final long serialVersionUID = 1L;

    private final L result;

    public ConstantFourParameterLinkDescriptorMapper(L result) {
      super();
      this.result = result;
    }

    @Override
    public L map(
        Quartet<
                ? extends IModel<T1>,
                ? extends IModel<T2>,
                ? extends IModel<T3>,
                ? extends IModel<T4>>
            param) {
      return result;
    }

    @Override
    public void detach() {
      if (result instanceof IDetachable resultDetachable) {
        Detachables.detach(resultDetachable);
      }
    }
  }
}
