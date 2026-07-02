package org.iglooproject.wicket.more.link.dto.security;

import org.iglooproject.wicket.more.link.dto.dto.IPageLinkDataDto;

public class PageLinkPermissionEvaluator implements IPageLinkPermissionEvaluator {

  // TODO voir comment on implemente çà
  @Override
  public boolean hasPermission(IPageLinkDataDto dataDto, String permission) {
    return true;
  }
}
