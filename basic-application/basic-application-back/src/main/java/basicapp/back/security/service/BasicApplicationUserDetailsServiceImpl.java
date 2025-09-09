package basicapp.back.security.service;

import basicapp.back.business.role.model.Role;
import basicapp.back.business.role.service.business.IRoleService;
import basicapp.back.business.user.model.User;
import basicapp.back.business.user.predicate.UserPredicates;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.iglooproject.commons.util.exception.IllegalSwitchValueException;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.jpa.security.business.user.model.IUser;
import org.iglooproject.jpa.security.service.CoreJpaUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class BasicApplicationUserDetailsServiceImpl extends CoreJpaUserDetailsServiceImpl
    implements IBasicApplicationUserDetailsService {

  @Autowired private IRoleService roleService;

  @Autowired private PermissionFactory permissionFactory;

  @Override
  public Pair<Set<GrantedAuthority>, Set<Permission>> getAuthoritiesAndPermissions(
      IUser loggedInUser) {

    User user =
        Optional.ofNullable(loggedInUser)
            .filter(User.class::isInstance)
            .map(u -> (User) u)
            .orElseThrow(IllegalStateException::new);

    // Chargement des Authority Spring Security
    Set<GrantedAuthority> grantedAuthorities =
        getAuthorities(user).stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toUnmodifiableSet());

    // Chargement des Permission Spring ACL
    Set<Role> roles =
        UserPredicates.technical().apply(user)
            ? Sets.newHashSet(roleService.findAll())
            : user.getRoles();
    Set<Permission> permissions =
        roles.stream()
            .flatMap(
                role ->
                    permissionFactory.buildFromNames(List.copyOf(role.getPermissions())).stream())
            .collect(Collectors.toUnmodifiableSet());

    return new ImmutablePair<>(grantedAuthorities, permissions);
  }

  private List<String> getAuthorities(User user) {
    return switch (user.getType()) {
      case BASIC -> List.of(CoreAuthorityConstants.ROLE_AUTHENTICATED);
      case TECHNICAL ->
          List.of(CoreAuthorityConstants.ROLE_AUTHENTICATED, CoreAuthorityConstants.ROLE_ADMIN);
      default -> throw new IllegalSwitchValueException(user.getType());
    };
  }
}
