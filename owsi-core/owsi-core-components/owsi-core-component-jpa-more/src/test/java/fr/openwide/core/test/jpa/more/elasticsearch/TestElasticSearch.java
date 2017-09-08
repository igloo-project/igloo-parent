package fr.openwide.core.test.jpa.more.elasticsearch;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.jpa.config.spring.provider.IJpaConfigurationProvider;
import fr.openwide.core.jpa.search.service.IHibernateSearchService;
import fr.openwide.core.jpa.util.EntityManagerUtils;
import fr.openwide.core.test.jpa.more.business.AbstractJpaMoreTestCase;
import fr.openwide.core.test.jpa.more.business.entity.model.TestEntity;

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
