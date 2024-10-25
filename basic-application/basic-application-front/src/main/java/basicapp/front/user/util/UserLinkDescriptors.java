package basicapp.front.user.util;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.atomic.UserType;
import basicapp.back.util.binding.Bindings;
import basicapp.front.user.page.BasicUserDetailPage;
import basicapp.front.user.page.TechnicalUserDetailPage;
import org.apache.wicket.Page;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.mapper.ITwoParameterLinkDescriptorMapper;
import org.iglooproject.wicket.more.link.util.LinkDescriptors;

public final class UserLinkDescriptors {

  public static ITwoParameterLinkDescriptorMapper<? extends IPageLinkDescriptor, User, Page>
      detailMapper(User user) {
    return detailMapper(Bindings.user().type().apply(user));
  }

  public static ITwoParameterLinkDescriptorMapper<? extends IPageLinkDescriptor, User, Page>
      detailMapper(UserType userType) {
    if (userType == null) {
      return LinkDescriptors.<User, Page>invalidTwoParameterMapper();
    }

    return switch (userType) {
      case BASIC -> BasicUserDetailPage.MAPPER;
      case TECHNICAL -> TechnicalUserDetailPage.MAPPER;
      default -> LinkDescriptors.<User, Page>invalidTwoParameterMapper();
    };
  }

  private UserLinkDescriptors() {}
}
