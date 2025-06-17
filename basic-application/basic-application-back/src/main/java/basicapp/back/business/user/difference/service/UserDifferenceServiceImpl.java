package basicapp.back.business.user.difference.service;

import basicapp.back.business.user.model.User;
import basicapp.back.util.binding.Bindings;
import java.util.List;
import org.bindgen.BindingRoot;
import org.iglooproject.commons.util.binding.ICoreBinding;
import org.iglooproject.jpa.more.business.difference.service.AbstractGenericEntityDifferenceServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserDifferenceServiceImpl extends AbstractGenericEntityDifferenceServiceImpl<User>
    implements IUserDifferenceService {

  @Override
  protected Iterable<? extends ICoreBinding<? extends User, ?>>
      getSimpleInitializationFieldsBindings() {
    // not used at the moment, we only use the minimal difference fields.
    return List.of();
  }

  @Override
  protected Iterable<? extends BindingRoot<? super User, ?>> getMinimalDifferenceFieldsBindings() {
    // we don't use this at the moment: the usage of differences is commented.
    return List.of(
        Bindings.user().firstName(),
        Bindings.user().lastName(),
        Bindings.user().emailAddress(),
        Bindings.user().locale(),
        Bindings.user().type());
  }
}
