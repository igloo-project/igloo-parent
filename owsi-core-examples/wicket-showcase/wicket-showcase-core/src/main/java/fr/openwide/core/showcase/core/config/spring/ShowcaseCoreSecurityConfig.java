package fr.openwide.core.showcase.core.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.Permission;

import fr.openwide.core.jpa.security.config.spring.AbstractJpaSecuritySecuredConfig;
import fr.openwide.core.jpa.security.service.AuthenticationUserNameComparison;
import fr.openwide.core.showcase.core.security.acl.service.ShowcaseAclServiceImpl;

@Configuration
public class ShowcaseCoreSecurityConfig extends AbstractJpaSecuritySecuredConfig {

	@Bean
	@Override
	public AuthenticationUserNameComparison authenticationUserNameComparison() {
		return AuthenticationUserNameComparison.CASE_INSENSITIVE;
	}

	@Bean
	@Override
	public AclService aclService() {
		return new ShowcaseAclServiceImpl();
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
