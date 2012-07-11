package fr.openwide.core.basicapp.core.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.Permission;

import fr.openwide.core.basicapp.core.security.acl.service.BasicApplicationAclServiceImpl;
import fr.openwide.core.jpa.security.config.spring.AbstractJpaSecurityConfig;
import fr.openwide.core.jpa.security.service.AuthenticationUserNameComparison;

@Configuration
public class BasicApplicationCoreSecurityConfig extends AbstractJpaSecurityConfig {

	@Bean
	@Override
	public AuthenticationUserNameComparison authenticationUserNameComparison() {
		return AuthenticationUserNameComparison.CASE_INSENSITIVE;
	}

	@Bean
	@Override
	public AclService aclService() {
		return new BasicApplicationAclServiceImpl();
	}

	@Override
	public Class<? extends Permission> permissionClass() {
		return BasePermission.class;
	}

	@Override
	public String roleHierarchyAsString() {
		return defaultRoleHierarchyAsString();
	}

	@Override
	public String permissionHierarchyAsString() {
		return defaultPermissionHierarchyAsString();
	}

}
