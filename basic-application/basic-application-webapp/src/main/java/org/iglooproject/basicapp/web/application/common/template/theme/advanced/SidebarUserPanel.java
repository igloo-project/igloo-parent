package org.iglooproject.basicapp.web.application.common.template.theme.advanced;

import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.model.BindingModel;
import igloo.wicket.model.Detachables;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.BasicApplicationSession;
import org.iglooproject.basicapp.web.application.profile.page.ProfilePage;
import org.iglooproject.wicket.more.security.page.LoginSuccessPage;
import org.iglooproject.wicket.more.security.page.LogoutPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

public class SidebarUserPanel extends Panel {

  private static final long serialVersionUID = -4710074067415473028L;

  private static final Logger LOGGER = LoggerFactory.getLogger(SidebarUserPanel.class);

  private final IModel<Authentication> originalAuthenticationModel =
      new LoadableDetachableModel<Authentication>() {
        private static final long serialVersionUID = 1L;

        @Override
        protected Authentication load() {
          return BasicApplicationSession.get().getOriginalAuthentication();
        }
      };

  private final IModel<String> originalAuthenticationNameModel =
      new LoadableDetachableModel<String>() {
        private static final long serialVersionUID = 1L;

        @Override
        protected String load() {
          Authentication originalAuthentication = originalAuthenticationModel.getObject();
          return originalAuthentication != null ? originalAuthentication.getName() : null;
        }
      };

  public SidebarUserPanel(String id) {
    super(id);

    add(Condition.anyChildVisible(this).thenShow());

    add(
        new EnclosureContainer("userContainer")
            .anyChildVisible()
            .add(
                new EnclosureContainer("originalAuthenticationContainer")
                    .condition(Condition.modelNotNull(originalAuthenticationModel))
                    .add(
                        new CoreLabel("originalAuthentication", originalAuthenticationNameModel)
                            .hideIfEmpty(),
                        new AjaxLink<Void>("signInAsMe") {
                          private static final long serialVersionUID = 1L;

                          @Override
                          public void onClick(AjaxRequestTarget target) {
                            try {
                              BasicApplicationSession.get().signInAsMe();
                              BasicApplicationSession.get()
                                  .success(getString("authentication.back.success"));
                            } catch (Exception e) {
                              LOGGER.error("Erreur lors de la reconnexion de l'utilisateur.", e);
                              Session.get().error(getString("common.error.unknown"));
                            }
                            throw LoginSuccessPage.linkDescriptor().newRestartResponseException();
                          }
                        }),
                ProfilePage.linkDescriptor()
                    .link("profileLink")
                    .add(
                        new CoreLabel(
                                "fullName",
                                BindingModel.of(
                                    BasicApplicationSession.get().getUserModel(),
                                    Bindings.user().fullName()))
                            .hideIfEmpty()),
                new CoreLabel(
                        "username",
                        BindingModel.of(
                            BasicApplicationSession.get().getUserModel(),
                            Bindings.user().username()))
                    .hideIfEmpty(),
                new BookmarkablePageLink<Void>("logout", LogoutPage.class)));
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(originalAuthenticationModel, originalAuthenticationNameModel);
  }
}
