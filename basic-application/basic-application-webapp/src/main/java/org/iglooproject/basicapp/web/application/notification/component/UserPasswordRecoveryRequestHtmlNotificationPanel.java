package org.iglooproject.basicapp.web.application.notification.component;

import igloo.wicket.component.CoreLabel;
import igloo.wicket.model.BindingModel;
import java.util.Date;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.util.ResourceKeyGenerator;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.wicket.more.link.descriptor.generator.IPageLinkGenerator;

public class UserPasswordRecoveryRequestHtmlNotificationPanel<T extends User>
    extends AbstractHtmlNotificationPanel<T> {

  private static final long serialVersionUID = -6941290354402094613L;

  public UserPasswordRecoveryRequestHtmlNotificationPanel(
      String id,
      ResourceKeyGenerator resourceKeyGenerator,
      IModel<T> objectModel,
      IModel<User> authorModel,
      IModel<Date> dateModel,
      IPageLinkGenerator linkGenerator) {
    this(
        id,
        resourceKeyGenerator,
        resourceKeyGenerator,
        objectModel,
        authorModel,
        dateModel,
        linkGenerator);
  }

  public UserPasswordRecoveryRequestHtmlNotificationPanel(
      String id,
      ResourceKeyGenerator resourceKeyGenerator,
      ResourceKeyGenerator defaultResourceKeyGenerator,
      IModel<T> objectModel,
      IModel<User> authorModel,
      IModel<Date> dateModel,
      IPageLinkGenerator linkGenerator) {
    super(id, objectModel);

    StringResourceModel descriptionTextModel =
        new StringResourceModel(resourceKeyGenerator.resourceKey("text"), objectModel)
            .setParameters(dateModel, authorModel)
            .setDefaultValue(
                new StringResourceModel(
                        defaultResourceKeyGenerator.resourceKey("intro"), objectModel)
                    .setParameters(dateModel, authorModel));

    add(new CoreLabel("intro", descriptionTextModel).setEscapeModelStrings(false));

    StringResourceModel linkIntroModel =
        new StringResourceModel(resourceKeyGenerator.resourceKey("link.intro"), objectModel)
            .setParameters(dateModel, authorModel)
            .setDefaultValue(
                new StringResourceModel(
                        defaultResourceKeyGenerator.resourceKey("link.intro"), objectModel)
                    .setParameters(dateModel, authorModel));

    StringResourceModel linkLabelModel =
        new StringResourceModel(resourceKeyGenerator.resourceKey("link.label"), objectModel)
            .setParameters(dateModel, authorModel)
            .setDefaultValue(
                new StringResourceModel(
                        defaultResourceKeyGenerator.resourceKey("link.label"), objectModel)
                    .setParameters(dateModel, authorModel));

    IModel<String> urlModel =
        LoadableDetachableModel.of(() -> linkGenerator.bypassPermissions().fullUrl());

    add(
        new CoreLabel("linkIntro", linkIntroModel),
        new ExternalLink("mainLink", urlModel, linkLabelModel),
        new ExternalLink("url", urlModel, urlModel));

    add(
        new WebMarkupContainer("helpUsername")
            .add(
                new CoreLabel(
                    "username", BindingModel.of(objectModel, Bindings.user().username()))));
  }
}
