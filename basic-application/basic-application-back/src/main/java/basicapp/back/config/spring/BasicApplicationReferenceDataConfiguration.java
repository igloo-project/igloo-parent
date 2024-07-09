package basicapp.back.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import basicapp.back.business.referencedata.search.CitySearchQueryImpl;
import basicapp.back.business.referencedata.search.ICitySearchQuery;

@Configuration
public class BasicApplicationReferenceDataConfiguration {

	@Configuration
	public class SearchQuery {
		@Bean
		public ICitySearchQuery citySearchQuery() {
			return new CitySearchQueryImpl();
		}
	}

}
