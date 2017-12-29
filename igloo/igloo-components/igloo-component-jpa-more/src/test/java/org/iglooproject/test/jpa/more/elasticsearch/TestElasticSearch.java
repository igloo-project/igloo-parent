package org.iglooproject.test.jpa.more.elasticsearch;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.iglooproject.jpa.config.spring.provider.IJpaConfigurationProvider;
import org.iglooproject.jpa.search.service.IHibernateSearchService;
import org.iglooproject.jpa.util.EntityManagerUtils;
import org.iglooproject.test.jpa.more.business.AbstractJpaMoreTestCase;
import org.iglooproject.test.jpa.more.business.entity.model.TestEntity;

public class TestElasticSearch extends AbstractJpaMoreTestCase {
	
	@Autowired
	protected EntityManagerUtils entityManagerUtils;

	@Autowired
	protected IJpaConfigurationProvider configurationProvider;

	@Autowired
	private IHibernateSearchService hibernateSearchService;

	/**
	 * Check that elasticsearch analyzers can be mapped to Lucene ones.
	 */
	@Test
	public void testElasticSearchAnalyzer() {
		hibernateSearchService.getAnalyzer(TestEntity.class);
	}

}
