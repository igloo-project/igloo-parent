package basicapp.back.config;

import basicapp.back.business.common.model.EmailAddress;
import basicapp.back.business.common.model.PhoneNumber;
import basicapp.back.business.common.model.PostalCode;
import basicapp.back.business.role.model.RoleBinding;
import basicapp.back.business.user.difference.service.IUserDifferenceService;
import basicapp.back.business.user.difference.service.UserDifferenceServiceImpl;
import basicapp.back.business.user.model.UserBinding;
import basicapp.back.util.binding.Bindings;
import igloo.difference.DifferenceIntrospector;
import igloo.difference.DifferenceIntrospectorDefaults;
import igloo.difference.model.DifferenceFields;
import java.util.Set;
import org.iglooproject.commons.util.fieldpath.FieldPath;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BasicApplicationBackDifferenceConfiguration {

  private static final Set<Class<?>> ADDITIONAL_SIMPLE_TYPES =
      Set.of(EmailAddress.class, PhoneNumber.class, PostalCode.class);

  @Bean
  public IUserDifferenceService userDifferenceService() {
    return new UserDifferenceServiceImpl(userFields());
  }

  public static DifferenceFields userFields() {
    DifferenceIntrospector differenceIntrospector =
        new DifferenceIntrospector(new UserBinding(), ADDITIONAL_SIMPLE_TYPES);

    DifferenceIntrospectorDefaults.ignoreCommonFields(differenceIntrospector);
    differenceIntrospector.addIgnoredPaths(FieldPath.fromString(".toStringHelper"));
    differenceIntrospector.addIgnoredPaths(Bindings.user().type());
    differenceIntrospector.addIgnoredPaths(Bindings.user().fullName());
    differenceIntrospector.addIgnoredPaths(Bindings.user().notificationEmailAddress());
    differenceIntrospector.addIgnoredPaths(Bindings.user().notificationEnabled());
    differenceIntrospector.addIgnoredPaths(Bindings.user().passwordHash());
    differenceIntrospector.addIgnoredPaths(Bindings.user().hasPasswordHash());
    differenceIntrospector.addIgnoredPaths(Bindings.user().passwordInformation());
    differenceIntrospector.addIgnoredPaths(Bindings.user().passwordRecoveryRequest());
    differenceIntrospector.addIgnoredPaths(Bindings.user().announcementInformation());
    differenceIntrospector.addIgnoredPaths(Bindings.user().creation());
    differenceIntrospector.addIgnoredPaths(Bindings.user().modification());
    differenceIntrospector.addIgnoredPaths(Bindings.user().lastLoginDate());

    differenceIntrospector.addBinding(Bindings.user().roles(), new RoleBinding());
    differenceIntrospector.addShallowPaths(Bindings.user().roles());

    return differenceIntrospector.visitBinding();
  }
}
