package org.iglooproject.basicapp.web.application.common.validator;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.service.IUserService;
import org.iglooproject.wicket.more.util.validate.validators.AbstractUnicityValidator;

public class EmailUnicityValidator extends AbstractUnicityValidator<User, String> {

  private static final long serialVersionUID = 2113279975851524261L;

  @SpringBean private IUserService userService;

  public EmailUnicityValidator(IModel<? extends User> mainObjectModel) {
    super(mainObjectModel, "common.validator.email.unicity");
    Injector.get().inject(this);
  }

  @Override
  protected User getByUniqueField(String value) {
    return userService.getByEmailCaseInsensitive(value);
  }
}
