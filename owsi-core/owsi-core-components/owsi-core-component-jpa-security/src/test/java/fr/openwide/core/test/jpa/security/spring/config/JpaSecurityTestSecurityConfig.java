package fr.openwide.core.test.jpa.security.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.Permission;

import fr.openwide.core.jpa.security.service.AuthenticationUserNameComparison;
import fr.openwide.core.jpa.security.spring.config.AbstractJpaSecurityConfig;
import fr.openwide.core.test.jpa.security.acl.service.MockAclServiceImpl;

@Configuration
public class JpaSecurityTestSecurityConfig extends AbstractJpaSecurityConfig {

	@Override
	public Class<? extends Permission> permissionClass() {
		return BasePermission.class;
	}

	@Override
	public String roleHierarchyAsString() {
		return defaultRoleHierarchyAsString() +
				"ROLE_ADMIN > ROLE_GROUP_1\n" +
				"ROLE_ADMIN > ROLE_GROUP_2\n" +
				"ROLE_GROUP_1 > ROLE_GROUP_3\n";
	}

	@Override
	public String permissionHierarchyAsString() {
		return defaultPermissionHierarchyAsString();
	}

	@Override
	public AuthenticationUserNameComparison authenticationUserNameComparison() {
		return AuthenticationUserNameComparison.CASE_INSENSITIVE;
	}

	@Override
	public AclService aclService() {
		return new MockAclServiceImpl();
	}

}
