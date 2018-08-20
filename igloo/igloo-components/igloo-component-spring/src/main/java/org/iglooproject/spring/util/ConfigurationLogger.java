package org.iglooproject.spring.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.AbstractApplicationContext;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import org.iglooproject.spring.property.model.PropertyId;
import org.iglooproject.spring.property.service.IConfigurablePropertyService;
import org.iglooproject.spring.property.service.IPropertyService;

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
	
	private String logPattern = "%1$30s : %2$s";
	
	private List<String> propertyIdsKeysForInfoLogLevel = Lists.newArrayList();
	
	public void setPropertyNamesForInfoLogLevel(String propertyIds) {
		propertyIdsKeysForInfoLogLevel.addAll(StringUtils.splitAsList(propertyIds, ","));
	}
	
	public void setLogPattern(String logPattern) {
		this.logPattern = logPattern;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent refresh) {
		
		if (refresh.getSource() instanceof AbstractApplicationContext &&
				((AbstractApplicationContext) refresh.getSource()).getParent() == null) {
			LOGGER.info("Configuration logging");
			
			ApplicationContext context = refresh.getApplicationContext();
			String[] propertyServiceNames = context.getBeanNamesForType(IConfigurablePropertyService.class);
			
			if (propertyServiceNames.length > 0) {
				String propertyServiceName = propertyServiceNames[0];
				
				if (propertyServiceNames.length > 1) {
					LOGGER.warn(String.format("Multiple %1$s found. We only log the configuration of the first instance.", IPropertyService.class.getSimpleName()));
				}
				
				LOGGER.info("Configuration found, start logging");

				IConfigurablePropertyService propertyService = (IConfigurablePropertyService) context.getBean(propertyServiceName);
				
				List<String> loggedProperties = Lists.newArrayList();
				@SuppressWarnings("unchecked")
				Iterable<PropertyId<?>> registeredPropertyIds =
						(Iterable<PropertyId<?>>) (Object) Iterables.filter(propertyService.listRegistered(), PropertyId.class);
				
				/* On logge les informations qu'on a configurées dans le contexte Spring */
				for (String propertyIdKey : propertyIdsKeysForInfoLogLevel) {
					if (loggedProperties.contains(propertyIdKey)) {
						continue;
					}
					
					for (PropertyId<?> propertyId : registeredPropertyIds) {
						if (propertyId.getKey().equals(propertyIdKey)) {
							logPropertyAsInfo(propertyIdKey, propertyService.get(propertyId));
							loggedProperties.add(propertyIdKey);
							break;
						}
					}
				}
				
				/* Si jamais on est en mode TRACE, on logge aussi les autres propriétés */
				if (LOGGER.isTraceEnabled()) {
					for (PropertyId<?> propertyId : registeredPropertyIds) {
						if (!propertyIdsKeysForInfoLogLevel.contains(propertyId.getKey())) {
							logPropertyAsTrace(propertyId.getKey(), propertyService.get(propertyId));
						}
					}
				}
			} else {
				LOGGER.warn(String.format("No %1$s found. Unable to log the configuration.", IConfigurablePropertyService.class.getSimpleName()));
			}
			
			LOGGER.info("Configuration logging end");
		}
	}
	
	private void logPropertyAsInfo(String propertyName, Object value) {
		LOGGER.info(String.format(logPattern, propertyName, value));
	}
	
	private void logPropertyAsTrace(String propertyName, Object value) {
		LOGGER.trace(String.format(logPattern, propertyName, value));
	}

}
