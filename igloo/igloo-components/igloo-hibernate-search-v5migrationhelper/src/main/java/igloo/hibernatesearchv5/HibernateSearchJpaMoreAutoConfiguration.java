package igloo.hibernatesearchv5;

import org.hibernate.search.mapper.orm.Search;
import org.iglooproject.jpa.autoconfigure.JpaAutoConfiguration;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

@AutoConfiguration(after = JpaAutoConfiguration.class)
@ConditionalOnClass({ Search.class, LocalContainerEntityManagerFactoryBean.class })
@ConditionalOnProperty(name = "spring.jpa.properties.hibernate.search.enabled", matchIfMissing = true, havingValue = "true")
public class HibernateSearchJpaMoreAutoConfiguration {
	@Bean
	@ConditionalOnMissingBean
	@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public IHibernateSearchLuceneQueryFactory hibernateSearchLuceneQueryFactory() {
		return new HibernateSearchLuceneQueryFactoryImpl();
	}
}
