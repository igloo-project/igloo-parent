package org.igloo.spring.autoconfigure.jpa;

import java.util.Set;

import org.iglooproject.jpa.more.business.CoreJpaMoreBusinessPackage;
import org.iglooproject.jpa.more.util.CoreJpaMoreUtilPackage;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionDefaults;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.AnnotationScopeMetadataResolver;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.context.annotation.ScopeMetadata;
import org.springframework.context.annotation.ScopeMetadataResolver;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.type.AnnotationMetadata;

// @ConditionalOnBean + @ComponentScan = 
// see https://github.com/spring-projects/spring-boot/issues/1625
@Configuration
public class IglooJpaMoreComponentScanConfig implements ImportBeanDefinitionRegistrar {

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		AnnotationBeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();
		ScopeMetadataResolver scopeMetadataResolver = new AnnotationScopeMetadataResolver();
		BeanDefinitionDefaults beanDefinitionDefaults = new BeanDefinitionDefaults();
		for (Class<?> basePackageClass : new Class<?>[] { CoreJpaMoreBusinessPackage.class, CoreJpaMoreUtilPackage.class }) {
			ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(true);
			Set<BeanDefinition> definitions = scanner.findCandidateComponents(basePackageClass.getPackage().getName());
			// usage of ClassPathScanningCandidateComponentProvider extracted from ClassPathBeanDefinitionParser#doScan
			for (BeanDefinition candidate : definitions) {
				ScopeMetadata scopeMetadata = scopeMetadataResolver.resolveScopeMetadata(candidate);
				candidate.setScope(scopeMetadata.getScopeName());
				String beanName = beanNameGenerator.generateBeanName(candidate, registry);
				if (candidate instanceof AbstractBeanDefinition) {
					((AbstractBeanDefinition) candidate).applyDefaults(beanDefinitionDefaults);
				}
				if (candidate instanceof AnnotatedBeanDefinition) {
					AnnotationConfigUtils.processCommonDefinitionAnnotations((AnnotatedBeanDefinition) candidate);
				}
				if (checkCandidate(registry, beanName, candidate)) {
					BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(candidate, beanName);
					definitionHolder = applyScopedProxyMode(scopeMetadata, definitionHolder, registry);
					BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, registry);
				}
			}
		}
	}

	/**
	 * From {@link AnnotationConfigUtils}
	 */
	private BeanDefinitionHolder applyScopedProxyMode(
			ScopeMetadata metadata, BeanDefinitionHolder definition, BeanDefinitionRegistry registry) {

		ScopedProxyMode scopedProxyMode = metadata.getScopedProxyMode();
		if (scopedProxyMode.equals(ScopedProxyMode.NO)) {
			return definition;
		}
		boolean proxyTargetClass = scopedProxyMode.equals(ScopedProxyMode.TARGET_CLASS);
		return ScopedProxyUtils.createScopedProxy(definition, registry, proxyTargetClass);
	}


	/**
	 * From {@link ClassPathBeanDefinitionScanner}
	 */
	protected boolean checkCandidate(BeanDefinitionRegistry registry, String beanName, BeanDefinition beanDefinition) throws IllegalStateException {
		if (!registry.containsBeanDefinition(beanName)) {
			return true;
		}
		BeanDefinition existingDef = registry.getBeanDefinition(beanName);
		BeanDefinition originatingDef = existingDef.getOriginatingBeanDefinition();
		if (originatingDef != null) {
			existingDef = originatingDef;
		}
		if (isCompatible(beanDefinition, existingDef)) {
			return false;
		}
		throw new IllegalStateException("Annotation-specified bean name '" + beanName +
				"' for bean class [" + beanDefinition.getBeanClassName() + "] conflicts with existing, " +
				"non-compatible bean definition of same name and class [" + existingDef.getBeanClassName() + "]");
	}

	/**
	 * From {@link ClassPathBeanDefinitionScanner}
	 */
	protected boolean isCompatible(BeanDefinition newDefinition, BeanDefinition existingDefinition) {
		return (!(existingDefinition instanceof ScannedGenericBeanDefinition) ||  // explicitly registered overriding bean
				(newDefinition.getSource() != null && newDefinition.getSource().equals(existingDefinition.getSource())) ||  // scanned same file twice
				newDefinition.equals(existingDefinition));  // scanned equivalent class twice
	}
}