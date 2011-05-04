package fr.openwide.core.jpa.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;

import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor;

import fr.openwide.core.spring.util.ReflectionUtils;

/**
 * By default JPA explores the same classpath unit (folder/jar file) where the persistence.xml file
 * was found. To add another jar file to the same persistence unit,
 * the persistence.xml file should be modified to add new classes.
 * 
 * @see https://jira.springsource.org/browse/SPR-3073
 * 
 * @author icoloma
 * @author gsmet
 */
public class PackagesToScanPersistenceUnitPostProcessor implements PersistenceUnitPostProcessor {

	/**
	 * the path of packages to search for persistent classes (e.g. org.springframework).
	 * Subpackages will be visited, too
	 */
	private List<String> packagesToScan;
	
	/**
	 * the calculated list of additional persistent classes
	 */
	private Set<Class<? extends Object>> persistentClasses;

	/**
	 * Looks for any persistent class in the classpath under the specified packages
	 */
	public void afterPropertiesSet() throws Exception {
		if (packagesToScan == null || packagesToScan.isEmpty()) {
			throw new IllegalArgumentException("packages property must be set");
		}
		
		persistentClasses = new HashSet<Class<? extends Object>>();
		
		for (String packageToScan : packagesToScan) {
			persistentClasses.addAll(ReflectionUtils.findAnnotatedClasses(packageToScan, Entity.class));
		}
		
		if (persistentClasses.isEmpty()) {
			throw new IllegalArgumentException("No class annotated with @Entity found in: " + packagesToScan);
		}
	}

	/**
	 * Add all the persistent classes found to the PersistentUnit
	 */
	public void postProcessPersistenceUnitInfo(MutablePersistenceUnitInfo persistenceUnitInfo) {
		for (Class<? extends Object> c : persistentClasses) {
			persistenceUnitInfo.addManagedClassName(c.getName());
		}
	}

	public void setPackagesToScan(List<String> packagesToScan) {
		this.packagesToScan = packagesToScan;
	}

}