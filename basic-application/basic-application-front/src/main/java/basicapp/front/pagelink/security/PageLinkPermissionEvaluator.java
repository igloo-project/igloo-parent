package basicapp.front.pagelink.security;

import basicapp.front.pagelink.dto.IPageLinkDataDto;
import org.springframework.stereotype.Component;

@Component
public class PageLinkPermissionEvaluator implements IPageLinkPermissionEvaluator {

  @Override
  public boolean hasPermission(IPageLinkDataDto dataDto, String permission) {
    return true;
  }
}
