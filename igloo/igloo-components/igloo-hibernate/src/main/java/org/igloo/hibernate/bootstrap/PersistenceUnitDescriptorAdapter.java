/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.igloo.hibernate.bootstrap;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.bytecode.enhance.spi.EnhancementContext;
import org.hibernate.bytecode.spi.ClassTransformer;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.jpa.boot.spi.PersistenceUnitDescriptor;
import org.hibernate.jpa.internal.enhance.EnhancingClassTransformerImpl;

import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.ValidationMode;
import jakarta.persistence.spi.PersistenceUnitTransactionType;

public class PersistenceUnitDescriptorAdapter implements PersistenceUnitDescriptor {
	private final String name = "persistenceUnitDescriptorAdapter@" + System.identityHashCode( this );
	private Properties properties;
	private ClassTransformer classTransformer;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isUseQuotedIdentifiers() {
		return false;
	}

	@Override
	public String getProviderClassName() {
		return HibernatePersistenceProvider.class.getName();
	}

	@Override
	public PersistenceUnitTransactionType getTransactionType() {
		return null;
	}

	@Override
	public DataSource getJtaDataSource() {
		return null;
	}

	@Override
	public DataSource getNonJtaDataSource() {
		return null;
	}

	@Override
	public List<String> getMappingFileNames() {
		return Collections.emptyList();
	}

	@Override
	public List<URL> getJarFileUrls() {
		return Collections.emptyList();
	}

	@Override
	public URL getPersistenceUnitRootUrl() {
		return null;
	}

	@Override
	public List<String> getManagedClassNames() {
		return Collections.emptyList();
	}

	@Override
	public boolean isExcludeUnlistedClasses() {
		return false;
	}

	@Override
	public SharedCacheMode getSharedCacheMode() {
		return null;
	}

	@Override
	public ValidationMode getValidationMode() {
		return null;
	}

	@Override
	public Properties getProperties() {
		if ( properties == null ) {
			properties = new Properties();
		}
		return properties;
	}

	@Override
	public ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	@Override
	public ClassLoader getTempClassLoader() {
		return this.getClassLoader();
	}

	/**
	 * Retrieved from hibernate implementation
	 */
	public void pushClassTransformer(EnhancementContext enhancementContext) {
		this.classTransformer = new EnhancingClassTransformerImpl(enhancementContext);
	}

	/**
	 * Retrieved from hibernate implementation
	 */
	public ClassTransformer getClassTransformer() {
		return this.classTransformer;
	}
}
