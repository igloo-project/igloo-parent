package org.iglooproject.basicapp.core.business.authority;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import org.iglooproject.jpa.security.business.authority.model.Authority;
import org.iglooproject.jpa.security.business.authority.service.IAuthorityService;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("basicApplicationAuthorityUtils")
public class BasicApplicationAuthorityUtils {

  public static final List<String> PUBLIC_AUTHORITIES =
      ImmutableList.of(
          CoreAuthorityConstants.ROLE_AUTHENTICATED, CoreAuthorityConstants.ROLE_ADMIN);

  @Autowired private IAuthorityService authorityService;

  public List<Authority> getPublicAuthorities() {
    List<Authority> publicAuthorities = new ArrayList<>();

    for (String authorityName : PUBLIC_AUTHORITIES) {
      Authority authority = authorityService.getByName(authorityName);
      if (authority != null) {
        publicAuthorities.add(authority);
      }
    }

    return publicAuthorities;
  }
}
