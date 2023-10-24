package igloo.console.template;

import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_UNIT;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_VALUE;

import igloo.bootstrap.tooltip.BootstrapTooltipBehavior;
import igloo.bootstrap.tooltip.BootstrapTooltipOptions;
import igloo.bootstrap5.markup.html.template.js.bootstrap.Bootstrap5OverrideJavaScriptResourceReference;
import igloo.bootstrap5.markup.html.template.js.bootstrap.dropdown.BootstrapDropdownBehavior;
import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.model.BindingModel;
import java.util.List;
import java.util.Objects;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.more.business.upgrade.service.IAbstractDataUpgradeService;
import org.iglooproject.spring.property.SpringPropertyIds;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.more.AbstractCoreSession;
import org.iglooproject.wicket.more.console.common.model.ConsoleMenuItem;
import org.iglooproject.wicket.more.console.common.model.ConsoleMenuSection;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.feedback.AnimatedGlobalFeedbackPanel;
import org.iglooproject.wicket.more.markup.html.template.AbstractWebPageTemplate;
import org.iglooproject.wicket.more.markup.html.template.component.BodyBreadCrumbPanel;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.model.ApplicationPropertyModel;
import org.iglooproject.wicket.more.security.page.LogoutPage;
import org.iglooproject.wicket.more.util.binding.CoreWicketMoreBindings;

public abstract class ConsoleTemplate extends AbstractWebPageTemplate {

  private static final long serialVersionUID = -477123413708677528L;

  @SpringBean protected IPropertyService propertyService;

  @SpringBean(required = false)
  protected IAbstractDataUpgradeService dataUpgradeService;

  protected ConsoleTemplate(PageParameters parameters) {
    super(parameters);

    add(
        new TransparentWebMarkupContainer("htmlElement")
            .add(
                AttributeAppender.append(
                    "lang", AbstractCoreSession.get().getLocale().getLanguage())));

    addHeadPageTitlePrependedElement(
        new BreadCrumbElement(new ResourceModel("console.common.application.name")));
    addHeadPageTitlePrependedElement(
        new BreadCrumbElement(new ResourceModel("console.common.console")));
    add(createHeadPageTitle("headPageTitle"));

    EnclosureContainer navbarNavContainer = new EnclosureContainer("navbarNavContainer");
    add(navbarNavContainer.anyChildVisible());

    navbarNavContainer.add(getApplicationHomePageLinkDescriptor().link("applicationHomePageLink"));

    List<ConsoleMenuSection> menuItems = ConsoleConfiguration.get().getMenuSections();

    navbarNavContainer.add(
        new ListView<ConsoleMenuSection>("navbarNavItems", menuItems) {
          private static final long serialVersionUID = 1L;

          @Override
          protected void populateItem(ListItem<ConsoleMenuSection> item) {
            ConsoleMenuSection navItem = item.getModelObject();
            AbstractLink navLink =
                new BookmarkablePageLink<Void>("navLink", navItem.getPageClass());

            item.add(navLink);

            navLink.add(new CoreLabel("label", new ResourceModel(navItem.getDisplayStringKey())));

            List<ConsoleMenuItem> subMenuItems = navItem.getMenuItems();

            Condition isSubVisibleCondition = Condition.constant(!subMenuItems.isEmpty());

            IModel<Boolean> isSubActiveModel = Model.of(false);
            Condition isSubActiveCondition = Condition.isTrue(isSubActiveModel);

            EnclosureContainer navbarNavSubContainer =
                new EnclosureContainer("navbarNavSubContainer");
            item.add(navbarNavSubContainer.anyChildVisible().setOutputMarkupId(true));

            navbarNavSubContainer.add(
                new ListView<ConsoleMenuItem>("navbarNavSubItems", subMenuItems) {
                  private static final long serialVersionUID = -2257358650754295013L;

                  @Override
                  protected void populateItem(ListItem<ConsoleMenuItem> item) {
                    ConsoleMenuItem navItem = item.getModelObject();
                    AbstractLink navLink =
                        new BookmarkablePageLink<Void>("navLink", navItem.getPageClass());

                    item.add(navLink);

                    navLink.add(
                        new CoreLabel("label", new ResourceModel(navItem.getDisplayStringKey())));

                    if (Objects.equals(
                        navItem.getPageClass(), ConsoleTemplate.this.getSecondMenuPage())) {
                      navLink.add(new ClassAttributeAppender("active"));
                      isSubActiveModel.setObject(true);
                    }
                  }
                }.setVisibilityAllowed(!subMenuItems.isEmpty()));

            navLink.add(
                new ClassAttributeAppender(
                    isSubVisibleCondition
                        .then(Model.of("dropdown-toggle"))
                        .otherwise(Model.of(""))),
                new BootstrapDropdownBehavior(navbarNavSubContainer) {
                  private static final long serialVersionUID = 1L;

                  @Override
                  public boolean isEnabled(Component component) {
                    return isSubVisibleCondition.applies();
                  }
                },
                new ClassAttributeAppender(
                    Condition.or(
                            isSubVisibleCondition.and(isSubActiveCondition),
                            Condition.isTrue(
                                () ->
                                    Objects.equals(
                                        navItem.getPageClass(),
                                        ConsoleTemplate.this.getFirstMenuPage())))
                        .then(Model.of("active"))
                        .otherwise(Model.of())));

            item.add(
                new ClassAttributeAppender(
                    isSubVisibleCondition.then(Model.of("dropdown")).otherwise(Model.of(""))));
          }
        }.setVisibilityAllowed(!menuItems.isEmpty()));

    add(
        new CoreLabel(
                "username",
                BindingModel.of(
                    AbstractCoreSession.get().getUserModel(),
                    CoreWicketMoreBindings.user().username()))
            .hideIfEmpty());

    add(new BookmarkablePageLink<Void>("logoutLink", LogoutPage.class));

    add(
        ConsoleConfiguration.get()
            .getConsoleHeaderEnvironmentComponentFactory()
            .create("headerEnvironment"));

    add(
        ConsoleConfiguration.get()
            .getConsoleHeaderAdditionalContentComponentFactory()
            .create("headerAdditionalContent"));

    add(createBodyBreadCrumb("breadCrumb").add(displayBreadcrumb().thenShow()));

    add(
        new CoreLabel(
            "applicationVersion",
            new StringResourceModel(
                "console.common.application.version.application",
                ApplicationPropertyModel.of(SpringPropertyIds.VERSION))),
        new CoreLabel(
            "iglooVersion",
            new StringResourceModel(
                "console.common.application.version.igloo",
                ApplicationPropertyModel.of(SpringPropertyIds.IGLOO_VERSION))));

    add(
        new AnimatedGlobalFeedbackPanel(
            "feedback",
            propertyService.get(CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_VALUE),
            propertyService.get(CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_UNIT)));

    add(new BootstrapTooltipBehavior(getBootstrapTooltipOptionsModel()));
  }

  protected IPageLinkDescriptor getApplicationHomePageLinkDescriptor() {
    return LinkDescriptorBuilder.start().page(getApplication().getHomePage());
  }

  @Override
  protected Component createBodyBreadCrumb(String wicketId) {
    // By default, we remove one element from the breadcrumb as it is usually also used to generate
    // the page title.
    // The last element is usually the title of the current page and shouldn't be displayed in the
    // breadcrumb.
    return new BodyBreadCrumbPanel(
            wicketId, bodyBreadCrumbPrependedElementsModel, breadCrumbElementsModel, 1)
        .setDividerModel(Model.of(""))
        .setTrailingSeparator(true);
  }

  protected Condition displayBreadcrumb() {
    return Condition.alwaysTrue();
  }

  protected IModel<BootstrapTooltipOptions> getBootstrapTooltipOptionsModel() {
    return BootstrapTooltipOptions::get;
  }

  @Override
  public void renderHead(IHeaderResponse response) {
    super.renderHead(response);
    for (ResourceReference cssResourceReference :
        ConsoleConfiguration.get().getCssResourcesReferences()) {
      response.render(CssHeaderItem.forReference(cssResourceReference));
    }
    response.render(
        JavaScriptHeaderItem.forReference(Bootstrap5OverrideJavaScriptResourceReference.get()));
  }

  @Override
  public String getVariation() {
    return AbstractWebPageTemplate.BOOTSTRAP5_VARIATION;
  }
}
