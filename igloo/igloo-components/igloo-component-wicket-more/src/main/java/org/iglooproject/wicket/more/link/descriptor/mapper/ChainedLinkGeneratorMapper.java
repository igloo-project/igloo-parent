package org.iglooproject.wicket.more.link.descriptor.mapper;

import com.google.common.collect.ImmutableList;
import java.util.Iterator;
import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.wicket.more.link.descriptor.generator.IImageResourceLinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.generator.ILinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.generator.IPageLinkGenerator;
import org.iglooproject.wicket.more.link.util.LinkDescriptors;
import org.iglooproject.wicket.more.model.SafeCastModel;
import org.javatuples.Pair;

/**
 * A {@link IOneParameterLinkDescriptorMapper} that will delegate to other link descriptor mappers.
 *
 * <p>This may be useful when you have a model of a general, abstract type, and want to use a
 * different link depending on the actual type of the model object.
 *
 * <p>This mappers' delegates are called "the chain". Upon mapping, each element of the chain will
 * be given a {@link SafeCastModel casted} model, and will return a {@link ILinkGenerator} (which
 * may or may not be {@link ILinkGenerator#isAccessible() accessible}). Those link generators will
 * be {@link ILinkGenerator#chain(ILinkGenerator) chained}, so that the resulting link descriptor
 * will delegate to the first valid link generator returned by the mapper chain.
 */
public class ChainedLinkGeneratorMapper<L extends ILinkGenerator, T>
    extends AbstractOneParameterLinkDescriptorMapper<L, T> {

  private static final long serialVersionUID = 1568363162862717474L;

  /**
   * Start building a {@link ChainedLinkGeneratorMapper} for mappers that return {@link
   * IPageLinkGenerator page link generators}.
   */
  public static <T> Builder<IPageLinkGenerator, T> forPage() {
    return new Builder<>(input -> input.getValue0().chain(input.getValue1()));
  }

  /**
   * Start building a {@link ChainedLinkGeneratorMapper} for mappers that return {@link
   * ILinkGenerator resource link generators}.
   */
  public static <T> Builder<ILinkGenerator, T> forResource() {
    return new Builder<>(input -> input.getValue0().chain(input.getValue1()));
  }

  /**
   * Start building a {@link ChainedLinkGeneratorMapper} for mappers that return {@link
   * IImageResourceLinkGenerator image resource link generators}.
   */
  public static <T> Builder<IImageResourceLinkGenerator, T> forImage() {
    return new Builder<>(input -> input.getValue0().chain(input.getValue1()));
  }

  public static class Builder<L extends ILinkGenerator, T> {

    private final ImmutableList.Builder<ILinkDescriptorMapper<? extends L, IModel<T>>> chain =
        ImmutableList.builder();

    private final SerializableFunction2<? super Pair<L, L>, ? extends L> generatorChainFunction;

    private Builder(SerializableFunction2<? super Pair<L, L>, ? extends L> generatorChainFunction) {
      this.generatorChainFunction = generatorChainFunction;
    }

    public <L2 extends L, T2 extends T> Builder<L, T> add(
        ILinkDescriptorMapper<L2, IModel<T>> delegate) {
      chain.add(delegate);
      return this;
    }

    public <L2 extends L, T2 extends T> Builder<L, T> add(
        ILinkDescriptorMapper<L2, IModel<T2>> delegate, Class<T2> castClass) {
      chain.add(new SafeCastedMapper<L2, T, T2>(delegate, castClass));
      return this;
    }

    public ChainedLinkGeneratorMapper<L, T> build() {
      return new ChainedLinkGeneratorMapper<>(chain.build(), generatorChainFunction);
    }
  }

  private static class SafeCastedMapper<L, T, T2 extends T>
      implements ILinkDescriptorMapper<L, IModel<T>> {
    private static final long serialVersionUID = 1L;

    private final ILinkDescriptorMapper<L, IModel<T2>> delegate;

    private final Class<T2> clazz;

    public SafeCastedMapper(ILinkDescriptorMapper<L, IModel<T2>> delegate, Class<T2> clazz) {
      super();
      this.delegate = delegate;
      this.clazz = clazz;
    }

    @Override
    public L map(IModel<T> model1) {
      return delegate.map(SafeCastModel.of(clazz, model1));
    }

    @Override
    public void detach() {
      delegate.detach();
    }
  }

  private final Iterable<ILinkDescriptorMapper<? extends L, IModel<T>>> chain;

  private final SerializableFunction2<? super Pair<L, L>, ? extends L> generatorChainFunction;

  public ChainedLinkGeneratorMapper(
      Iterable<ILinkDescriptorMapper<? extends L, IModel<T>>> chain,
      SerializableFunction2<? super Pair<L, L>, ? extends L> generatorChainFunction) {
    this.chain = chain;
    this.generatorChainFunction = generatorChainFunction;
  }

  @SuppressWarnings("unchecked")
  @Override
  public L map(IModel<T> model) {
    return recurseChain(model, (L) LinkDescriptors.invalid(), chain.iterator());
  }

  private L recurseChain(
      IModel<T> model, L first, Iterator<ILinkDescriptorMapper<? extends L, IModel<T>>> it) {
    if (it.hasNext()) {
      return recurseChain(
          model, generatorChainFunction.apply(Pair.with(first, (L) it.next().map(model))), it);
    } else {
      return first;
    }
  }
}
