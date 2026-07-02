package org.iglooproject.wicket.more.link.dto.security;

import org.iglooproject.wicket.more.link.dto.dto.IPageLinkDataDto;

public interface IPageLinkPermissionEvaluator {

  boolean hasPermission(IPageLinkDataDto dataDto, String permission);
}
