package fr.openwide.core.spring.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.util.MethodInvoker;

import fr.openwide.core.spring.config.CoreConfigurer;

/**
 * <p>Ce listener Spring permet de logguer la configuration Spring lors de l'émission
 * de l'événement REFRESH.</p>
 * 
 * <p>Ce listener n'est déclenché que pour les événements émis par un contexte racine.</p>
 * 
 * <p>La propriété <i>propertyNamesForInfoLogLevel</i> permet de spécifier, par une liste
 * de noms de propriétés, quels sont les éléments de configuration à logguer au
 * niveau INFO. De plus, elle permet de forcer le logging de propriétés ne
 * possédant pas d'accesseur (tels que les informations d'accès à la base de données.</p>
 * 
 * <p>Les propriétés listées dans <i>propertyNamesForInfoLogLevel</i> sont affichées
 * dans l'ordre de déclaration, avec un log level INFO, puis l'ensemble des
 * propriétés possédant un accesseur sont logguées avec un log level TRACE.</p>
 * 
 * <p>Les autres éléments sont loggués au niveau TRACE.</p>
 * 
 * <p>La propriété <i>logPattern</i> permet de spécifier le formattage des messages
 * de log émis pour chaque item de configuration. Deux arguments, le nom de la propriété
 * et sa valeur, sont passés en paramètre de String.format sur ce pattern.</p>
 */
public class ConfigurationLogger implements ApplicationListener<ContextRefreshedEvent> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationLogger.class);
	
	private static final String FORCE_LOG_METHOD = "getPropertyAsString";
	
	private static final String GET_PROPERTY_AS_PREFIX = "propertyAs";
	
	private String logPattern = "%1$30s : %2$s";
	
	private List<String> propertyNamesForInfoLogLevel = new ArrayList<String>();
	
	public void setPropertyNamesForInfoLogLevel(String names) {
		propertyNamesForInfoLogLevel.addAll(StringUtils.splitAsList(names, ","));
	}
	
	public void setLogPattern(String logPattern) {
		this.logPattern = logPattern;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent refresh) {
		
		if (refresh.getSource() != null &&
				refresh.getSource() instanceof AbstractApplicationContext &&
				((AbstractApplicationContext) refresh.getSource()).getParent() == null) {
			LOGGER.info("Configuration logging");
			
			ApplicationContext context = refresh.getApplicationContext();
			String[] configurerNames = context.getBeanNamesForType(CoreConfigurer.class);
			
			if (configurerNames.length > 0) {
				String configurerName = configurerNames[0];
				
				if (configurerNames.length > 1) {
					LOGGER.warn(String.format("Multiple %1$s found. We only log the configuration of the first instance.", CoreConfigurer.class.getSimpleName()));
				}
				
				LOGGER.info("Configuration found, start logging");
				
				CoreConfigurer configurer = (CoreConfigurer) context.getBean(configurerName);
				BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(configurer);
				List<String> loggedProperties = new ArrayList<String>();
				
				/* On logge les informations qu'on a configurées dans le contexte Spring */
				for (String name : propertyNamesForInfoLogLevel) {
					if (!loggedProperties.contains(name)) {
						if (wrapper.isReadableProperty(name)) {
							loggedProperties.add(name);
							logPropertyAsInfo(wrapper, name);
						} else {
							try {
								loggedProperties.add(name);
								logPropertyStringValueAsInfo(configurer, name);
							} catch (Exception e) {
								LOGGER.error(String.format("Error accessing %1$s property", name), e);
							}
						}
					}
				}
				
				/* Si jamais on est en mode TRACE, on logge aussi les autres propriétés */
				if (LOGGER.isTraceEnabled()) {
					for (PropertyDescriptor descriptor : wrapper.getPropertyDescriptors()) {
						String name = descriptor.getName();
						
						if (!propertyNamesForInfoLogLevel.contains(name)
								&& !loggedProperties.contains(name)
								&& wrapper.isReadableProperty(name)
								&& !descriptor.isHidden() && !name.startsWith(GET_PROPERTY_AS_PREFIX)) {
							loggedProperties.add(name);
							logPropertyAsTrace(wrapper, name);
						}
					}
				}
			} else {
				LOGGER.warn(String.format("No %1$s found. Unable to log the configuration.", CoreConfigurer.class.getSimpleName()));
			}
			
			LOGGER.info("Configuration logging end");
		}
	}
	
	private void logPropertyAsInfo(BeanWrapper wrapper, String propertyName) {
		logPropertyAsInfo(propertyName, wrapper.getPropertyValue(propertyName));
	}
	
	private void logPropertyAsInfo(String propertyName, Object value) {
		LOGGER.info(String.format(logPattern, propertyName, value));
	}
	
	private void logPropertyStringValueAsInfo(CoreConfigurer configurer, String name)
			throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		MethodInvoker invoker = new MethodInvoker();
		invoker.setTargetObject(configurer);
		invoker.setTargetMethod(FORCE_LOG_METHOD);
		invoker.setArguments(new String[] { name });
		Object value;
		invoker.prepare();
		value = invoker.invoke();
		
		logPropertyAsInfo(name, value);
	}
	
	private void logPropertyAsTrace(BeanWrapper wrapper, String propertyName) {
		logPropertyAsTrace(propertyName, wrapper.getPropertyValue(propertyName));
	}
	
	private void logPropertyAsTrace(String propertyName, Object value) {
		LOGGER.trace(String.format(logPattern, propertyName, value));
	}

}
