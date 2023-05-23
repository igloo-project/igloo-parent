package org.iglooproject.jpa.config.spring;

import org.iglooproject.jpa.search.dao.HibernateSearchDaoImpl;
import org.iglooproject.jpa.search.dao.IHibernateSearchDao;
import org.iglooproject.jpa.search.service.HibernateSearchServiceImpl;
import org.iglooproject.jpa.search.service.IHibernateSearchService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IglooHibernateSearchConfiguration {
	@Bean
	public IHibernateSearchService hibernateSearchService() {
		return new HibernateSearchServiceImpl();
	}
	@Bean
	public IHibernateSearchDao hibernateSearchDao() {
		return new HibernateSearchDaoImpl();
	}
}
