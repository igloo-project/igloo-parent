package fr.openwide.core.jpa.config.spring.provider;

import java.util.List;

import javax.sql.DataSource;

import org.hibernate.dialect.Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class DefaultJpaConfigurationProvider {

	@Autowired
	private List<JpaPackageScanProvider> jpaPackageScanProviders;

	@Value("${${db.type}.db.dialect}")
	private Class<Dialect> dialect;

	@Value("${hibernate.hbm2ddl.auto}")
	private String hbm2Ddl;

	@Value("${hibernate.hbm2ddl.defaultBatchSize}")
	private Integer defaultBatchSize;

	@Value("${lucene.index.path}")
	private String hibernateSearchIndexBase;

	@Value("#{dataSource}")
	private DataSource dataSource;

	@Value("#{ehCacheConfiguration}")
	private String ehCacheConfiguration;

	public List<JpaPackageScanProvider> getJpaPackageScanProviders() {
		return jpaPackageScanProviders;
	}

	public Class<Dialect> getDialect() {
		return dialect;
	}

	public String getHbm2Ddl() {
		return hbm2Ddl;
	}

	public Integer getDefaultBatchSize() {
		return defaultBatchSize;
	}

	public String getHibernateSearchIndexBase() {
		return hibernateSearchIndexBase;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public String getEhCacheConfiguration() {
		return ehCacheConfiguration;
	}

}
