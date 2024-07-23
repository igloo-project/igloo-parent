package org.iglooproject.wicket.more.link.descriptor.impl;

import com.google.common.collect.ImmutableList;
import org.apache.wicket.Component;
import org.iglooproject.wicket.more.link.descriptor.DynamicImage;
import org.iglooproject.wicket.more.link.descriptor.generator.IImageResourceLinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.generator.ILinkGenerator;

public class ChainedImageResourceLinkGeneratorImpl
    extends AbstractChainedLinkGenerator<IImageResourceLinkGenerator>
    implements IImageResourceLinkGenerator {

  private static final long serialVersionUID = -2475023459193693212L;

  public ChainedImageResourceLinkGeneratorImpl(
      Iterable<? extends IImageResourceLinkGenerator> chain) {
    super(chain);
  }

  @Override
  public ILinkGenerator chain(ILinkGenerator other) {
    if (other instanceof IImageResourceLinkGenerator) {
      return chain((IImageResourceLinkGenerator) other);
    }
    return new ChainedLinkGeneratorImpl(
        ImmutableList.<ILinkGenerator>builder().addAll(getChain()).add(other).build());
  }

  @Override
  public IImageResourceLinkGenerator chain(IImageResourceLinkGenerator other) {
    return new ChainedImageResourceLinkGeneratorImpl(
        ImmutableList.<IImageResourceLinkGenerator>builder().addAll(getChain()).add(other).build());
  }

  @Override
  public IImageResourceLinkGenerator wrap(Component component) {
    ImmutableList.Builder<IImageResourceLinkGenerator> builder = ImmutableList.builder();
    for (IImageResourceLinkGenerator element : getChain()) {
      builder.add(element.wrap(component));
    }
    return new ChainedImageResourceLinkGeneratorImpl(builder.build());
  }

  @Override
  public DynamicImage image(String wicketId) {
    return delegate().image(wicketId);
  }
}
