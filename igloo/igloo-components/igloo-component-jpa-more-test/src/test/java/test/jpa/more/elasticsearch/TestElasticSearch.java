package test.jpa.more.elasticsearch;

import org.springframework.beans.factory.annotation.Autowired;

import test.jpa.more.business.AbstractJpaMoreTestCase;
import test.jpa.more.business.entity.model.TestEntity;

import org.iglooproject.jpa.config.spring.provider.IJpaConfigurationProvider;
import org.iglooproject.jpa.search.service.IHibernateSearchService;
import org.iglooproject.jpa.util.EntityManagerUtils;
import org.junit.jupiter.api.Test;

class TestElasticSearch extends AbstractJpaMoreTestCase {
	
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
	void testElasticSearchAnalyzer() {
		hibernateSearchService.getAnalyzer(TestEntity.class);
	}

}
