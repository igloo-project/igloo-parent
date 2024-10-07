package basicapp.front.notification.service;

import basicapp.back.business.notification.service.IBasicApplicationNotificationUrlBuilderService;
import basicapp.back.business.user.model.User;
import basicapp.back.business.user.predicate.UserPredicates;
import basicapp.front.user.page.BasicUserDetailPage;
import basicapp.front.user.page.TechnicalUserDetailPage;
import java.util.concurrent.Callable;
import org.apache.wicket.Page;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.generator.IPageLinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.mapper.ITwoParameterLinkDescriptorMapper;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.iglooproject.wicket.more.notification.service.AbstractNotificationUrlBuilderServiceImpl;
import org.iglooproject.wicket.more.notification.service.IWicketContextProvider;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This service is used to generate the URL used in the text version of the notification emails.
 *
 * <p>It shouldn't be used for other purposes.
 */
public class BasicApplicationNotificationUrlBuilderServiceImpl
    extends AbstractNotificationUrlBuilderServiceImpl
    implements IBasicApplicationNotificationUrlBuilderService {

  @Autowired
  public BasicApplicationNotificationUrlBuilderServiceImpl(IWicketContextProvider contextProvider) {
    super(contextProvider);
  }

  @Override
  public String getUserDescriptionUrl(final User user) {
    ITwoParameterLinkDescriptorMapper<IPageLinkDescriptor, User, Page> mapper =
        UserPredicates.technical().apply(user)
            ? TechnicalUserDetailPage.MAPPER
            : BasicUserDetailPage.MAPPER;
    Callable<IPageLinkGenerator> pageLinkGeneratorTask =
        () -> mapper.ignoreParameter2().map(GenericEntityModel.of(user));

    return buildUrl(pageLinkGeneratorTask);
  }
}
