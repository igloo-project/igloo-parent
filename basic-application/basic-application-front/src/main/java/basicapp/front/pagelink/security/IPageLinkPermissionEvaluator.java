package basicapp.front.pagelink.security;

import basicapp.front.pagelink.dto.IPageLinkDataDto;

public interface IPageLinkPermissionEvaluator {

  boolean hasPermission(IPageLinkDataDto dataDto, String permission);
}
