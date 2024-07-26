package org.iglooproject.basicapp.web.application.common.model;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.typedescriptor.UserTypeDescriptor;

public abstract class UserTypeDescriptorModel<U extends User>
    extends LoadableDetachableModel<UserTypeDescriptor<U>> {

  private static final long serialVersionUID = 8336608480925285825L;

  @SuppressWarnings("unchecked")
  public static final <U extends User> UserTypeDescriptorModel<U> fromUser(IModel<U> userModel) {
    if (userModel == null) {
      return null;
    }
    return (UserTypeDescriptorModel<U>) fromClass(userModel.map(User::getClass));
  }

  public static final <U extends User> UserTypeDescriptorModel<U> fromClass(
      Class<? extends U> userClass) {
    return fromClass(Model.of(userClass));
  }

  public static final <U extends User> UserTypeDescriptorModel<U> fromClass(
      IModel<? extends Class<? extends U>> userClassModel) {
    return new UserTypeDescriptorModel<U>() {
      private static final long serialVersionUID = -6898587427928612341L;

      @Override
      protected UserTypeDescriptor<U> load() {
        return UserTypeDescriptor.get(userClassModel.getObject());
      }
    };
  }
}
