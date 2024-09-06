package basicapp.front.usergroup.page;

import basicapp.back.business.user.model.UserGroup;
import basicapp.back.util.binding.Bindings;
import basicapp.front.navigation.link.LinkFactory;
import basicapp.front.usergroup.component.UserGroupDetailDescriptionPanel;
import basicapp.front.usergroup.component.UserGroupDetailUsersPanel;
import basicapp.front.usergroup.template.UserGroupTemplate;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.condition.Condition;
import igloo.wicket.model.BindingModel;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.link.descriptor.generator.IPageLinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.mapper.IOneParameterLinkDescriptorMapper;
import org.iglooproject.wicket.more.link.descriptor.mapper.ITwoParameterLinkDescriptorMapper;
import org.iglooproject.wicket.more.link.descriptor.parameter.CommonParameters;
import org.iglooproject.wicket.more.link.model.PageModel;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.model.GenericEntityModel;

public class UserGroupDetailPage extends UserGroupTemplate {

  private static final long serialVersionUID = -5780326896837623229L;

  public static final IOneParameterLinkDescriptorMapper<IPageLinkDescriptor, UserGroup> MAPPER =
      LinkDescriptorBuilder.start()
          .model(UserGroup.class)
          .map(CommonParameters.ID)
          .mandatory()
          .page(UserGroupDetailPage.class);

  public static final ITwoParameterLinkDescriptorMapper<IPageLinkDescriptor, UserGroup, Page>
      MAPPER_SOURCE =
          LinkDescriptorBuilder.start()
              .model(UserGroup.class)
              .map(CommonParameters.ID)
              .mandatory()
              .model(Page.class)
              .pickSecond()
              .map(CommonParameters.SOURCE_PAGE_ID)
              .optional()
              .page(UserGroupDetailPage.class);

  public static final IPageLinkDescriptor linkDescriptor(
      IModel<UserGroup> userGroupModel, IModel<Page> sourcePageModel) {
    return MAPPER_SOURCE.map(userGroupModel, sourcePageModel);
  }

  public UserGroupDetailPage(PageParameters parameters) {
    super(parameters);

    IModel<UserGroup> userGroupModel = new GenericEntityModel<>();
    IModel<Page> sourcePageModel = new PageModel<>();

    linkDescriptor(userGroupModel, sourcePageModel)
        .extractSafely(
            parameters, UserGroupListPage.linkDescriptor(), getString("common.error.unexpected"));

    addBreadCrumbElement(
        new BreadCrumbElement(BindingModel.of(userGroupModel, Bindings.userGroup().name())));

    Component backToSourcePage =
        LinkFactory.get()
            .linkGenerator(sourcePageModel, UserGroupDetailPage.class)
            .link("backToSourcePage")
            .hideIfInvalid();

    add(
        backToSourcePage,
        UserGroupListPage.linkDescriptor()
            .link("backToList")
            .add(Condition.componentVisible(backToSourcePage).thenHide()),
        new CoreLabel("title", BindingModel.of(userGroupModel, Bindings.userGroup().name())));

    add(
        new UserGroupDetailDescriptionPanel("description", userGroupModel),
        new UserGroupDetailUsersPanel("users", userGroupModel));
  }

  public static IPageLinkGenerator linkGenerator(IModel<UserGroup> userGroupModel) {
    return linkDescriptor(userGroupModel, Model.of((Page) null));
  }
}