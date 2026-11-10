package org.iglooproject.wicket.more.link.dto.security;

import org.iglooproject.wicket.more.link.dto.dto.ILinkDataDto;

public class PageLinkPermissionEvaluator implements IPageLinkPermissionEvaluator {

  // TODO voir comment on implemente çà
  @Override
  public boolean hasPermission(ILinkDataDto dataDto, String permission) {
    return true;
  }
}
