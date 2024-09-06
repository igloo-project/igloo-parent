package igloo.console.navigation.page;

import igloo.console.template.ConsoleAccessTemplate;
import igloo.igloojs.showpassword.ShowPasswordBehavior;
import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.AbstractCoreSession;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class ConsoleSignInPage extends ConsoleAccessTemplate {

  private static final long serialVersionUID = 3401416708867386953L;

  private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleSignInPage.class);

  public static final IPageLinkDescriptor linkDescriptor() {
    return LinkDescriptorBuilder.start().page(ConsoleSignInPage.class);
  }

  public ConsoleSignInPage(PageParameters parameters) {
    super(parameters);

    addHeadPageTitlePrependedElement(
        new BreadCrumbElement(new ResourceModel("console.navigation.signIn")));
  }

  @Override
  protected IModel<String> getTitleModel() {
    return new ResourceModel("console.signIn.title");
  }

  @Override
  protected Component getContentComponent(String wicketId) {
    return new ContentFragment(wicketId);
  }

  private class ContentFragment extends Fragment {

    private static final long serialVersionUID = 1L;

    public ContentFragment(String id) {
      super(id, "content", ConsoleSignInPage.this);

      IModel<String> usernameModel = Model.of();
      IModel<String> passwordModel = Model.of();

      Form<Void> form =
          new Form<Void>("form") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
              try {
                AbstractCoreSession.get()
                    .signIn(usernameModel.getObject(), passwordModel.getObject());
                throw new RestartResponseException(ConsoleLoginSuccessPage.class);
              } catch (BadCredentialsException e) { // NOSONAR
                Session.get().error(getString("console.signIn.error.authentication"));
              } catch (UsernameNotFoundException | AccountStatusException e) { // NOSONAR
                Session.get().error(getString("console.signIn.error.authentication"));
              } catch (RestartResponseException e) { // NOSONAR
                throw e;
              } catch (Exception e) {
                LOGGER.error("Unknown error during authentification", e);
                Session.get().error(getString("console.signIn.error.authentication"));
              }

              throw new RestartResponseException(ConsoleLoginFailurePage.class);
            }
          };
      add(form);

      PasswordTextField password = new PasswordTextField("password", passwordModel);

      form.add(
          new TextField<>("username", usernameModel)
              .setRequired(true)
              .setLabel(new ResourceModel("console.signIn.form.username"))
              .add(new LabelPlaceholderBehavior())
              .setOutputMarkupId(true),
          password
              .setRequired(true)
              .setLabel(new ResourceModel("console.signIn.form.password"))
              .add(new LabelPlaceholderBehavior())
              .setOutputMarkupId(true),
          new BlankLink("showPassword").add(new ShowPasswordBehavior(password)));
    }
  }
}
