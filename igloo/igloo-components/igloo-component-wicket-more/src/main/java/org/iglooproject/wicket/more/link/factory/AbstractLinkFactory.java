package org.iglooproject.wicket.more.link.factory;

import java.util.Collection;
import java.util.Collections;
import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.link.descriptor.builder.state.pageinstance.IPageInstanceState;
import org.iglooproject.wicket.more.link.descriptor.generator.IPageLinkGenerator;
import org.iglooproject.wicket.more.link.model.PageModel;

public abstract class AbstractLinkFactory {

  public IPageLinkGenerator linkGenerator(Page pageInstance, Class<? extends Page> expectedClass) {
    return linkGenerator(pageInstance, Collections.singleton(expectedClass));
  }

  public IPageLinkGenerator linkGenerator(
      IModel<? extends Page> pageInstanceModel, Class<? extends Page> expectedClass) {
    return linkGenerator(pageInstanceModel, Collections.singleton(expectedClass));
  }

  public IPageLinkGenerator linkGenerator(
      Page pageInstance, Collection<? extends Class<? extends Page>> expectedClasses) {
    return linkGenerator(PageModel.of(pageInstance), expectedClasses);
  }

  public IPageLinkGenerator linkGenerator(
      IModel<? extends Page> pageInstanceModel,
      Collection<? extends Class<? extends Page>> expectedClasses) {
    IPageInstanceState<?> builder = LinkDescriptorBuilder.toPageInstance(pageInstanceModel);
    for (Class<? extends Page> expectedClass : expectedClasses) {
      builder.validate(expectedClass);
    }
    return builder.build();
  }
}
