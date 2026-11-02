package igloo.console.maintenance.authentication.page;

import igloo.console.maintenance.template.ConsoleMaintenanceTemplate;
import igloo.wicket.model.Detachables;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.wicket.more.AbstractCoreAuthenticatedSession;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.security.page.LoginSuccessPage;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class ConsoleMaintenanceAuthenticationPage extends ConsoleMaintenanceTemplate {

  private static final long serialVersionUID = 3401416708867386953L;

  public static final IPageLinkDescriptor linkDescriptor() {
    return LinkDescriptorBuilder.start().page(ConsoleMaintenanceAuthenticationPage.class);
  }

  private final IModel<String> usernameModel = new Model<>();

  public ConsoleMaintenanceAuthenticationPage(PageParameters parameters) {
    super(parameters);

    addBreadCrumbElement(
        new BreadCrumbElement(new ResourceModel("console.navigation.maintenance.authentication")));

    add(
        new Form<Void>("form") {
          private static final long serialVersionUID = 1L;

          @Override
          protected void onSubmit() {
            String username = usernameModel.getObject();

            if (!StringUtils.hasText(username)) {
              AbstractCoreAuthenticatedSession.get()
                  .error(
                      getString("console.maintenance.authentication.signInAs.error.userUnknown"));
              throw linkDescriptor().newRestartResponseException();
            }

            try {
              AbstractCoreAuthenticatedSession.get().signInAs(username);
              AbstractCoreAuthenticatedSession.get()
                  .success(
                      new StringResourceModel("console.maintenance.authentication.signInAs.success")
                          .setParameters(username)
                          .getObject());

              throw LoginSuccessPage.linkDescriptor().newRestartResponseException();
            } catch (UsernameNotFoundException e) {
              AbstractCoreAuthenticatedSession.get()
                  .error(
                      getString("console.maintenance.authentication.signInAs.error.userUnknown"));
            } catch (DisabledException e) {
              AbstractCoreAuthenticatedSession.get()
                  .error(
                      getString("console.maintenance.authentication.signInAs.error.userDisabled"));
            }

            throw linkDescriptor().newRestartResponseException();
          }
        }.add(
            new TextField<>("username", usernameModel)
                .setRequired(true)
                .setLabel(
                    new ResourceModel("console.maintenance.authentication.signInAs.form.username"))
                .add(new LabelPlaceholderBehavior())));
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(usernameModel);
  }

  @Override
  protected Class<? extends WebPage> getSecondMenuPage() {
    return ConsoleMaintenanceAuthenticationPage.class;
  }
}
