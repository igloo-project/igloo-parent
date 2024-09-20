package basicapp.front.common.validator;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.service.controller.IUserControllerService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.util.validate.validators.AbstractUnicityValidator;

public class UsernameUnicityValidator extends AbstractUnicityValidator<User, String> {

  private static final long serialVersionUID = 7351972908406394930L;

  @SpringBean private IUserControllerService userControllerService;

  public UsernameUnicityValidator(IModel<? extends User> mainObjectModel) {
    super(mainObjectModel, "common.validator.username.unicity");
    Injector.get().inject(this);
  }

  @Override
  protected User getByUniqueField(String value) {
    return userControllerService.getByUsername(value);
  }
}
