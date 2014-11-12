package fr.openwide.core.jpa.hibernate.cache.ehcache;

import java.net.MalformedURLException;
import java.net.URL;

import org.hibernate.boot.registry.classloading.spi.ClassLoaderService;
import org.hibernate.cache.ehcache.EhCacheMessageLogger;
import org.hibernate.cache.ehcache.EhCacheRegionFactory;
import org.hibernate.service.spi.InjectService;
import org.jboss.logging.Logger;

public class EhCache285RegionFactory extends EhCacheRegionFactory {

	private static final long serialVersionUID = -3457500226062047670L;
	
	private static final EhCacheMessageLogger LOG = Logger.getMessageLogger(
			EhCacheMessageLogger.class,
			EhCache285RegionFactory.class.getName()
	);
	
	@Override
	@InjectService
	public void setClassLoaderService(ClassLoaderService classLoaderService) {
		super.setClassLoaderService(classLoaderService);
		this.classLoaderService = classLoaderService;
	}

	private ClassLoaderService classLoaderService;
	
	@Override
	protected URL loadResource(String configurationResourceName) {
		URL url = null;
		if ( classLoaderService != null ) {
			url = classLoaderService.locateResource( configurationResourceName );
		}
		if ( url == null ) {
			final ClassLoader standardClassloader = Thread.currentThread().getContextClassLoader();
			if ( standardClassloader != null ) {
				url = standardClassloader.getResource( configurationResourceName );
			}
			if ( url == null ) {
				url = EhCache285RegionFactory.class.getResource( configurationResourceName );
			}
			if ( url == null ) {
				try {
					url = new URL( configurationResourceName );
				}
				catch ( MalformedURLException e ) {
					// ignore
				}
			}
		}
		if ( LOG.isDebugEnabled() ) {
			LOG.debugf(
					"Creating EhCacheRegionFactory from a specified resource: %s.  Resolved to URL: %s",
					configurationResourceName,
					url
			);
		}
		if ( url == null ) {

			LOG.unableToLoadConfiguration( configurationResourceName );
		}
		return url;
	}

}
