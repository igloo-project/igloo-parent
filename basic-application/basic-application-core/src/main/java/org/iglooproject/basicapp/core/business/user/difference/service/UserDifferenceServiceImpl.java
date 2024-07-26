package org.iglooproject.basicapp.core.business.user.difference.service;

import com.google.common.collect.ImmutableList;
import org.bindgen.BindingRoot;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.util.binding.Bindings;
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
    return ImmutableList.of();
  }

  @Override
  protected Iterable<? extends BindingRoot<? super User, ?>> getMinimalDifferenceFieldsBindings() {
    // we don't use this at the moment: the usage of differences is commented.
    return ImmutableList.of(
        Bindings.user().firstName(),
        Bindings.user().lastName(),
        Bindings.user().email(),
        Bindings.user().locale(),
        Bindings.user().groups());
  }
}
