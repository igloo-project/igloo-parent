package basicapp.front.user.template;

import basicapp.back.business.user.model.BasicUser;
import basicapp.back.business.user.model.TechnicalUser;
import basicapp.back.business.user.model.User;
import basicapp.back.business.user.service.IUserService;
import basicapp.back.security.service.ISecurityManagementService;
import basicapp.front.user.page.BasicUserDetailPage;
import basicapp.front.user.page.TechnicalUserDetailPage;
import igloo.wicket.model.Detachables;
import igloo.wicket.model.ReadOnlyModel;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.link.descriptor.mapper.ITwoParameterLinkDescriptorMapper;
import org.iglooproject.wicket.more.link.descriptor.parameter.CommonParameters;
import org.iglooproject.wicket.more.link.model.PageModel;
import org.iglooproject.wicket.more.markup.html.factory.DetachableFactories;
import org.iglooproject.wicket.more.model.GenericEntityModel;

public abstract class UserDetailTemplate<U extends User> extends UserTemplate {

  private static final long serialVersionUID = -550100874222819991L;

  public static final <U extends User>
      ITwoParameterLinkDescriptorMapper<IPageLinkDescriptor, U, Page> mapper() {
    return mapper(User.class).<U>castParameter1();
  }

  protected static final <U extends User>
      ITwoParameterLinkDescriptorMapper<IPageLinkDescriptor, U, Page> mapper(Class<U> clazz) {
    return LinkDescriptorBuilder.start()
        .model(clazz)
        .model(Page.class)
        .pickFirst()
        .map(CommonParameters.ID)
        .mandatory()
        .pickSecond()
        .map(CommonParameters.SOURCE_PAGE_ID)
        .optional()
        .pickFirst()
        .page(
            DetachableFactories.forUnit(
                ReadOnlyModel.factory(
                    u -> {
                      if (u instanceof BasicUser) {
                        return BasicUserDetailPage.class;
                      } else if (u instanceof TechnicalUser) {
                        return TechnicalUserDetailPage.class;
                      }
                      return null;
                    })));
  }

  @SpringBean protected IUserService userService;

  @SpringBean protected ISecurityManagementService securityManagementService;

  protected final IModel<U> userModel = new GenericEntityModel<>();

  protected final IModel<Page> sourcePageModel = new PageModel<>();

  public UserDetailTemplate(PageParameters parameters) {
    super(parameters);
  }

  @Override
  protected Class<? extends WebPage> getFirstMenuPage() {
    return getClass();
  }

  @Override
  protected Class<? extends WebPage> getSecondMenuPage() {
    return getClass();
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(userModel, sourcePageModel);
  }
}
