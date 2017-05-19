package fr.openwide.core.spring.config.spring.annotation;

import java.util.List;

public interface IConfigurationLocationProvider {

	List<String> getLocations(String applicationName, String environnement, String... locationPatterns);

}
