package basicapp.front.common.validator;

import basicapp.back.business.common.model.EmailAddress;
import basicapp.back.business.user.model.User;
import basicapp.back.business.user.service.controller.IUserControllerService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.util.validate.validators.AbstractUnicityValidator;

public class EmailAddressUnicityValidator extends AbstractUnicityValidator<User, EmailAddress> {

  private static final long serialVersionUID = 2113279975851524261L;

  @SpringBean private IUserControllerService userControllerService;

  public EmailAddressUnicityValidator(IModel<? extends User> mainObjectModel) {
    super(mainObjectModel, "common.validator.emailAddress.unicity");
    Injector.get().inject(this);
  }

  @Override
  protected User getByUniqueField(EmailAddress value) {
    return userControllerService.getByEmailAddressCaseInsensitive(value);
  }
}
