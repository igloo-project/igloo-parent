package fr.openwide.core.spring.config.spring.annotation;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * <p>Ce {@link BeanFactoryPostProcessor} permet d'extraire des métadonnées sur l'application et de les utiliser pour
 * constituer la liste des fichiers de configuration à utiliser pour sa configuration.</p>
 * 
 * <p>L'application doit initialiser un {@link PropertySourcesPlaceholderConfigurer} sans indiquer de valeur pour le
 * champ {@link PropertySourcesPlaceholderConfigurer#setLocations(Resource[])}.</p>
 */
public class ApplicationConfigurerBeanFactoryPostProcessor implements BeanFactoryPostProcessor, ApplicationContextAware,
		PriorityOrdered {

	private ApplicationContext applicationContext;

	/**
	 * Parcourt les beans du contexte pour extraire les informations sur l'application et les emplacements de
	 * configuration par la prise en compte des annotations {@link ApplicationDescription} et
	 * {@link ConfigurationLocations}
	 */
	@Override
	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {
		PropertySourcesPlaceholderConfigurer configurer = beanFactory.getBean(PropertySourcesPlaceholderConfigurer.class);

		Map<Integer, List<Resource>> locationGroups = Maps.newHashMap();
		String applicationName = null;
		
		// parcours des définitions de bean
		for (String beanName : beanFactory.getBeanDefinitionNames()) {
			// les beans @Configuration sont généralement wrappés par cglib ; on doit donc utiliser ClassUtils
			// pour récupérer le type du bean
			Class<?> beanType = ClassUtils.getUserClass(beanFactory.getType(beanName));
			
			// récupération des informations sur l'application
			// on récupère le bean annoté à la fois @ApplicationDescription et @Configuration dans le contexte
			// si plusieurs beans respectant ces conditions sont trouvés, cela provoque une erreur
			if (beanType != null && beanType.isAnnotationPresent(Configuration.class) &&
					beanType.isAnnotationPresent(ApplicationDescription.class)) {
				if (applicationName != null) {
					throw new ApplicationContextException("@" + ApplicationDescription.class + " ne doit être utilisé qu'une seule fois");
				}
				applicationName = beanType.getAnnotation(ApplicationDescription.class).name();
			}
		}
		
		// vérification qu'un nom a bien été déterminé pour l'application
		if (applicationName == null) {
			throw new ApplicationContextException("Au moins un bean doit fournir l'annotation @"
				+ ApplicationDescription.class.getSimpleName() + " avec un nom pour l'application.");
		}
		
		// parcours des définitions de bean
		for (String beanName : beanFactory.getBeanDefinitionNames()) {
			// les beans @Configuration sont généralement wrappés par cglib ; on doit donc utiliser ClassUtils
			// pour récupérer le type du bean
			Class<?> beanType = ClassUtils.getUserClass(beanFactory.getType(beanName));
			
			// récupération des locations pour la configuration de l'application ; tous les beans annotés
			// @Configuration et @ConfigurationLocations sont pris en compte.
			if (beanType != null && beanType.isAnnotationPresent(Configuration.class) &&
					beanType.isAnnotationPresent(ConfigurationLocations.class)) {
				ConfigurationLocations annotation = beanType.getAnnotation(ConfigurationLocations.class);
				
				// récupération du provider et instanciation
				Class<? extends IConfigurationLocationProvider> configurationLocationProviderClass =
						annotation.configurationLocationProvider();
				IConfigurationLocationProvider provider = BeanUtils.instantiateClass(configurationLocationProviderClass,
						IConfigurationLocationProvider.class);
				
				// récupération des locations et répartition par numéro d'ordre
				for (String location : provider.getLocations(
						applicationName,
						applicationContext.getEnvironment().getProperty("environment", "default"),
						annotation.locations())) {
					List<Resource> locationGroup;
					if (locationGroups.containsKey(annotation.order())) {
						locationGroup = locationGroups.get(annotation.order());
					} else {
						locationGroup = Lists.newArrayList();
						locationGroups.put(annotation.order(), locationGroup);
					}
					locationGroup.add(applicationContext.getResource(location));
				}
			}
		}
		
		// constitution de la liste ordonnée des locations et insertion dans le configurer
		List<Resource> locations = Lists.newArrayList();
		List<Integer> orders = Lists.newArrayList(locationGroups.keySet());
		Collections.sort(orders);
		for (Integer order : orders) {
			locations.addAll(locationGroups.get(order));
		}
		
		// ce sont les fichiers qui doivent être pris en compte avant l'environnement
		configurer.setFileEncoding(Charsets.UTF_8.name());
		configurer.setLocalOverride(true);
		configurer.setIgnoreResourceNotFound(true);
		configurer.setLocations(locations.toArray(new Resource[locations.size()]));
	}

	/**
	 * Nécessaire pour la récupération des {@link Resource}
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * Ce {@link BeanFactoryPostProcessor} doit être exécuté avant celui du {@link PropertySourcesPlaceholderConfigurer}
	 */
	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE - 1;
	}

}
