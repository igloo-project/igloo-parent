package org.iglooproject.basicapp.web.application.administration.template;

import igloo.wicket.model.Detachables;
import igloo.wicket.model.ReadOnlyModel;
import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.core.business.user.model.BasicUser;
import org.iglooproject.basicapp.core.business.user.model.TechnicalUser;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.service.IUserService;
import org.iglooproject.basicapp.core.security.service.ISecurityManagementService;
import org.iglooproject.basicapp.web.application.administration.page.AdministrationBasicUserDetailPage;
import org.iglooproject.basicapp.web.application.administration.page.AdministrationTechnicalUserDetailPage;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.link.descriptor.mapper.ITwoParameterLinkDescriptorMapper;
import org.iglooproject.wicket.more.link.descriptor.parameter.CommonParameters;
import org.iglooproject.wicket.more.link.model.PageModel;
import org.iglooproject.wicket.more.markup.html.factory.DetachableFactories;
import org.iglooproject.wicket.more.model.GenericEntityModel;

public abstract class AdministrationUserDetailTemplate<U extends User>
    extends AdministrationUserTemplate {

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
                        return AdministrationBasicUserDetailPage.class;
                      } else if (u instanceof TechnicalUser) {
                        return AdministrationTechnicalUserDetailPage.class;
                      }
                      return null;
                    })));
  }

  @SpringBean protected IUserService userService;

  @SpringBean protected ISecurityManagementService securityManagementService;

  protected final IModel<U> userModel = new GenericEntityModel<>();

  protected final IModel<Page> sourcePageModel = new PageModel<>();

  public AdministrationUserDetailTemplate(PageParameters parameters) {
    super(parameters);
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(userModel, sourcePageModel);
  }
}
