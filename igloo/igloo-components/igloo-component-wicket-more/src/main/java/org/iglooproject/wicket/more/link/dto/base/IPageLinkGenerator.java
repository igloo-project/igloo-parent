package org.iglooproject.wicket.more.link.dto.base;

import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.link.descriptor.LinkInvalidTargetRuntimeException;
import org.iglooproject.wicket.more.link.descriptor.generator.IBasicPageLinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationRuntimeException;
import org.iglooproject.wicket.more.link.dto.component.PageLinkBookmarkablePageLink;
import org.iglooproject.wicket.more.markup.html.template.model.NavigationMenuItem;

public interface IPageLinkGenerator<P extends Page> extends IDetachable, IBasicPageLinkGenerator {

  Class<P> getPageClass();

  IPageLinkGenerator<P> bypassPermissions();

  boolean isValid();

  @Override
  PageLinkBookmarkablePageLink<P> link(String wicketId);

  RestartResponseException restartResponseException();

  String url();

  String fullUrl();

  NavigationMenuItem navigationMenuItem(IModel<String> labelModel)
      throws LinkInvalidTargetRuntimeException, LinkParameterValidationRuntimeException;
}
