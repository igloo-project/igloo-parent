package org.iglooproject.wicket.more.link.descriptor.impl;

import com.google.common.collect.ImmutableList;
import igloo.wicket.model.Models;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ResourceReference;
import org.iglooproject.wicket.more.link.descriptor.DynamicImage;
import org.iglooproject.wicket.more.link.descriptor.IImageResourceLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.generator.IImageResourceLinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.parameter.mapping.LinkParametersMapping;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;

public class CoreImageResourceLinkDescriptorImpl extends CoreResourceLinkDescriptorImpl
    implements IImageResourceLinkDescriptor {

  private static final long serialVersionUID = 1L;

  public CoreImageResourceLinkDescriptorImpl(
      IModel<? extends ResourceReference> resourceReferenceModel,
      LinkParametersMapping parametersMapping,
      ILinkParameterValidator validator) {
    super(resourceReferenceModel, parametersMapping, validator);
  }

  @Override
  public DynamicImage image(String wicketId) {
    return new DynamicImage(
        wicketId, resourceReferenceModel, parametersMapping, parametersValidator);
  }

  @Override
  public IImageResourceLinkDescriptor wrap(Component component) {
    return new CoreImageResourceLinkDescriptorImpl(
        Models.wrap(resourceReferenceModel, component),
        parametersMapping.wrapOnAssignment(component),
        parametersValidator);
  }

  @Override
  public IImageResourceLinkGenerator chain(IImageResourceLinkGenerator other) {
    if (other instanceof IImageResourceLinkGenerator) {
      return chain((IImageResourceLinkGenerator) other);
    }
    return new ChainedImageResourceLinkGeneratorImpl(ImmutableList.of(this, other));
  }
}
