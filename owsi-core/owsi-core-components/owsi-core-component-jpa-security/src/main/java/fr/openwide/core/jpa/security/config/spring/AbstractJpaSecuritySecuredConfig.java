package fr.openwide.core.jpa.security.config.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.access.annotation.Secured;

/**
 * Par rapport à son parent, cette classe active la protection via les
 * annotations de sécurité spring.
 * 
 * @see Secured
 */
@Configuration
@ImportResource("classpath:spring/owsi-core-component-jpa-security-context.xml")
// définition des proxys Secured
public abstract class AbstractJpaSecuritySecuredConfig extends AbstractJpaSecurityConfig {

}
