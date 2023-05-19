package org.iglooproject.jpa.config.spring;

import java.util.List;
import java.util.Properties;

import org.igloo.hibernate.hbm.MetadataRegistryIntegrator;
import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.iglooproject.jpa.batch.CoreJpaBatchPackage;
import org.iglooproject.jpa.business.generic.CoreJpaBusinessGenericPackage;
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
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import com.google.common.collect.Lists;

@Configuration
@EnableTransactionManagement
@ComponentScan(
	basePackageClasses = {
		CoreJpaBatchPackage.class
	},
	excludeFilters = @Filter(Configuration.class)
)
@EntityScan(basePackageClasses = CoreJpaBusinessGenericPackage.class)
public class IglooJpaConfiguration {

	@ConditionalOnProperty(name = "spring.jpa.igloo.component-path", havingValue = "true", matchIfMissing = false)
	@Configuration
	@PropertySource(
		name = IglooPropertySourcePriority.FRAMEWORK,
		value = {
			"classpath:igloo-jpa-component-path.properties"
		},
		encoding = "UTF-8"
	)
	public static class JpaComponentPathConfiguration {
	}

	@Bean
	public IEntityService entityService() {
		return new EntityServiceImpl();
	}

	@Bean
	public IEntityDao entittyDao() {
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
	
	@Bean
	@ConditionalOnProperty(name = "spring.jpa.igloo.old-style-transaction-advisor", havingValue = "true", matchIfMissing = false)
	public Advisor oldStyleTransactionAdvisor(PlatformTransactionManager transactionManager) {
		AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
		
		advisor.setExpression("this(" + ITransactionalAspectAwareService.class.getName() + ")");
		advisor.setAdvice(defaultTransactionInterceptor(transactionManager));
		
		return advisor;
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
