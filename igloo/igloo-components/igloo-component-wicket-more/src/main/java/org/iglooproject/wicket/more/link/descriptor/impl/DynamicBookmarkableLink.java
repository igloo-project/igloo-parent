package org.iglooproject.wicket.more.link.descriptor.impl;

import org.apache.wicket.util.lang.Args;
import org.iglooproject.wicket.more.link.descriptor.AbstractDynamicBookmarkableLink;
import org.iglooproject.wicket.more.link.descriptor.LinkInvalidTargetRuntimeException;
import org.iglooproject.wicket.more.link.descriptor.generator.ILinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationRuntimeException;

public class DynamicBookmarkableLink extends AbstractDynamicBookmarkableLink {
  private static final long serialVersionUID = 6850747758117881107L;

  private final ILinkGenerator linkGenerator;

  public DynamicBookmarkableLink(String wicketId, ILinkGenerator linkGenerator) {
    super(wicketId);
    Args.notNull(linkGenerator, "parametersMapplinkGeneratoring");
    this.linkGenerator = linkGenerator.wrap(this);
  }

  protected ILinkGenerator getLinkGenerator() {
    return linkGenerator;
  }

  @Override
  protected boolean isValid() {
    return linkGenerator.isAccessible();
  }

  @Override
  protected CharSequence getRelativeURL()
      throws LinkInvalidTargetRuntimeException, LinkParameterValidationRuntimeException {
    return linkGenerator.url();
  }

  @Override
  protected CharSequence getAbsoluteURL()
      throws LinkInvalidTargetRuntimeException, LinkParameterValidationRuntimeException {
    return linkGenerator.fullUrl();
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    linkGenerator.detach();
  }
}
