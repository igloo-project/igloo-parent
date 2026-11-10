package org.iglooproject.wicket.more.link.dto.security;

import org.iglooproject.wicket.more.link.dto.dto.ILinkDataDto;

public interface IPageLinkPermissionEvaluator {

  boolean hasPermission(ILinkDataDto dataDto, String permission);
}
