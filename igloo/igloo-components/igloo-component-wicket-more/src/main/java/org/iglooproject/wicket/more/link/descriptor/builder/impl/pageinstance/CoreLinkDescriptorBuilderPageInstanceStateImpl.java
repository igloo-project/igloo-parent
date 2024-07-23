package org.iglooproject.wicket.more.link.descriptor.builder.impl.pageinstance;

import com.google.common.collect.Sets;
import java.util.Set;
import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.iglooproject.wicket.more.link.descriptor.builder.state.pageinstance.IPageInstanceState;
import org.iglooproject.wicket.more.link.descriptor.generator.IPageLinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.impl.CorePageInstanceLinkGenerator;

public class CoreLinkDescriptorBuilderPageInstanceStateImpl
    implements IPageInstanceState<IPageLinkGenerator> {

  private final IModel<? extends Page> pageInstanceModel;

  private Set<IModel<? extends Class<? extends Page>>> expectedPageClassModels = Sets.newHashSet();

  public CoreLinkDescriptorBuilderPageInstanceStateImpl(IModel<? extends Page> pageInstanceModel) {
    this.pageInstanceModel = pageInstanceModel;
  }

  @Override
  public <P extends Page> IPageInstanceState<IPageLinkGenerator> validate(
      Class<P> expectedPageClass) {
    return validate(Model.of(expectedPageClass));
  }

  @Override
  public IPageInstanceState<IPageLinkGenerator> validate(
      IModel<? extends Class<? extends Page>> expectedPageClassModel) {
    expectedPageClassModels.add(expectedPageClassModel);
    return this;
  }

  @Override
  public IPageLinkGenerator build() {
    return new CorePageInstanceLinkGenerator(pageInstanceModel, expectedPageClassModels);
  }
}
