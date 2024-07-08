package org.iglooproject.jpa.autoconfigure;

import java.util.List;
import java.util.Properties;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.boot.spi.MetadataBuilderContributor;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.spi.IntegratorProvider;
import org.igloo.hibernate.hbm.MetadataRegistryIntegrator;
import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.iglooproject.jpa.business.generic.dao.EntityDaoImpl;
import org.iglooproject.jpa.business.generic.dao.IEntityDao;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.query.EntityReferenceQueryImpl;
import org.iglooproject.jpa.business.generic.query.IEntityReferenceQuery;
import org.iglooproject.jpa.business.generic.service.EntityServiceImpl;
import org.iglooproject.jpa.business.generic.service.IEntityService;
import org.iglooproject.jpa.business.generic.service.ITransactionScopeIndependantRunnerService;
import org.iglooproject.jpa.business.generic.service.ITransactionalAspectAwareService;
import org.iglooproject.jpa.business.generic.service.TransactionScopeIndependantRunnerServiceImpl;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.util.EntityManagerUtils;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.Ordered;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import com.google.common.collect.Lists;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

/**
 * Provides additions to a classic spring-boot JPA Repository configuration.
 * 
 * <ul>
 * <li>(optional <code>spring.jpa.igloo.component-path.enabled=true</code>, default false) Customize database naming strategy.</li>
 * <li>(optional <code>spring.jpa.igloo.old-style-transaction-advisor</code>, default false) Customize transaction
 * interceptors with the old-style {@link ITransactionalAspectAwareService} interface and method-name based behaviors.</li>
 * <li>Add {@link IEntityService} / {@link IEntityDao} / {@link IEntityReferenceQuery} beans.</li>
 * <li>Add {@link ITransactionScopeIndependantRunnerService} bean that allows to launch {@link EntityManager}/Transaction
 * isolated jobs.</li>
 * <li>Add {@link EntityManagerUtils} bean; this bean provides utility methods to lookup thead-local bound {@link EntityManager}.</li>
 * <li>Add {@link MetadataRegistryIntegrator} bean</li>
 * </ul>
 * 
 * <h2>Database naming strategy</h2>
 * 
 * <p>See <code>igloo-component-jpa/component-path.properties</code> resource. Embedded names are included
 * inside property paths (useful to insert a same {@link Embeddable} multiple times) and entity/property names
 * are left untouched (see {@link PhysicalNamingStrategyStandardImpl}).</p>
 * 
 * <h2>Old-style transaction interceptor</h2>
 * 
 * <p>Based on {@link ITransactionalAspectAwareService} interface. <code>get/is/has/list/search/count/find</code> methods
 * are read-only and other methods are read-write.</p>
 * 
 * <h2>MetadataRegistryIntegrator</h2>
 * 
 * <p>This bean allows to store {@link Metadata} Hibernate object during {@link EntityManagerFactory} initialization.
 * This object is really helpful to perform some advanced Hibernate customization. This bean does not modify default
 * Hibernate behaviors.</p>
 * 
 * <p>Order for transaction management is modified from LOWEST_PRECEDENCE (last to be called) to
 * LOWEST_PRECEDENCE - 1.000.000, to allow to push other interceptors after transaction management. Authorization
 * related orders can be found in AuthorizationInterceptorsOrder and are from 200 to 600.</p>
 */
@Configuration
@ConditionalOnClass({ LocalContainerEntityManagerFactoryBean.class, EntityManager.class })
@EnableTransactionManagement(order = JpaAutoConfiguration.TRANSACTION_PROXY_ORDER)
public class JpaAutoConfiguration {

	public static final int TRANSACTION_PROXY_ORDER = Ordered.LOWEST_PRECEDENCE - 1_000_000;

	@ConditionalOnProperty(name = "spring.jpa.igloo.component-path.enabled", havingValue = "true", matchIfMissing = false)
	@Configuration
	@PropertySource(
		name = IglooPropertySourcePriority.FRAMEWORK,
		value = {
			"classpath:igloo-component-jpa/component-path.properties"
		},
		encoding = "UTF-8"
	)
	public static class JpaComponentPathConfiguration {
	}

	@ConditionalOnProperty(name = "spring.jpa.igloo.sequence-naming.enabled", havingValue = "true", matchIfMissing = false)
	@Configuration
	@PropertySource(
		name = IglooPropertySourcePriority.FRAMEWORK,
		value = {
			"classpath:igloo-component-jpa/sequence-naming.properties"
		},
		encoding = "UTF-8"
	)
	public static class SequenceConfiguration {
	}

	@Bean
	@ConditionalOnProperty(name = "spring.jpa.igloo.old-style-transaction-advisor", havingValue = "true", matchIfMissing = false)
	public Advisor oldStyleTransactionAdvisor(PlatformTransactionManager transactionManager) {
		AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
		
		advisor.setExpression("this(" + ITransactionalAspectAwareService.class.getName() + ")");
		advisor.setAdvice(defaultTransactionInterceptor(transactionManager));
		advisor.setOrder(TRANSACTION_PROXY_ORDER + 1); // call after @Transactional annotation
		
		return advisor;
	}

	@Bean
	public IEntityService entityService() {
		return new EntityServiceImpl();
	}

	@Bean
	public IEntityDao entityDao() {
		return new EntityDaoImpl();
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public IEntityReferenceQuery<GenericEntity<?, ?>> entityReferenceQuery() {
		return new EntityReferenceQueryImpl<>();
	}

	@Bean
	public ITransactionScopeIndependantRunnerService transactionScopeIndependantRunnerService() {
		return new TransactionScopeIndependantRunnerServiceImpl();
	}

	@Bean
	public EntityManagerUtils entityManagerUtils() {
		return new EntityManagerUtils();
	}

	@Bean
	public MetadataRegistryIntegrator metdataRegistryIntegrator() {
		return new MetadataRegistryIntegrator();
	}

	/**
	 * Register {@link MetadataRegistryIntegrator} in Hibernate configuration.
	 */
	@Bean
	public HibernatePropertiesCustomizer hibernateIntegratorProperty(MetadataRegistryIntegrator integrator) {
		return p -> p.put(
				EntityManagerFactoryBuilderImpl.INTEGRATOR_PROVIDER,
				(IntegratorProvider) () -> List.of(integrator));
	}

	@Bean
	public HibernatePropertiesCustomizer typeCustomizer(List<MetadataBuilderContributor> metadataBuilderContributor) {
		return hp -> {
			hp.put("hibernate.metadata_builder_contributor", new MetadataBuilderContributor() {
				@Override
				public void contribute(MetadataBuilder metadataBuilder) {
					metadataBuilderContributor.stream().forEach(i -> i.contribute(metadataBuilder));
				}
			});
		};
	}

	/**
	 * Construit un transactionInterceptor avec une configuration par défaut.
	 */
	public static TransactionInterceptor defaultTransactionInterceptor(PlatformTransactionManager transactionManager) {
		TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
		Properties transactionAttributes = new Properties();
		
		List<RollbackRuleAttribute> rollbackRules = Lists.newArrayList();
		rollbackRules.add(new RollbackRuleAttribute(ServiceException.class));
		
		DefaultTransactionAttribute readOnlyTransactionAttributes =
				new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRED);
		readOnlyTransactionAttributes.setReadOnly(true);
		
		RuleBasedTransactionAttribute writeTransactionAttributes =
				new RuleBasedTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRED, rollbackRules);
		
		String readOnlyTransactionAttributesDefinition = readOnlyTransactionAttributes.toString();
		String writeTransactionAttributesDefinition = writeTransactionAttributes.toString();
		// read-only
		transactionAttributes.setProperty("is*", readOnlyTransactionAttributesDefinition);
		transactionAttributes.setProperty("has*", readOnlyTransactionAttributesDefinition);
		transactionAttributes.setProperty("get*", readOnlyTransactionAttributesDefinition);
		transactionAttributes.setProperty("list*", readOnlyTransactionAttributesDefinition);
		transactionAttributes.setProperty("search*", readOnlyTransactionAttributesDefinition);
		transactionAttributes.setProperty("find*", readOnlyTransactionAttributesDefinition);
		transactionAttributes.setProperty("count*", readOnlyTransactionAttributesDefinition);
		// write et rollback-rule
		transactionAttributes.setProperty("*", writeTransactionAttributesDefinition);
		
		transactionInterceptor.setTransactionAttributes(transactionAttributes);
		transactionInterceptor.setTransactionManager(transactionManager);
		return transactionInterceptor;
	}
}
