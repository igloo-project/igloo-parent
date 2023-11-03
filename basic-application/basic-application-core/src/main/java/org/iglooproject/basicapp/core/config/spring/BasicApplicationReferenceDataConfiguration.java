package org.iglooproject.basicapp.core.config.spring;

import org.iglooproject.basicapp.core.business.referencedata.search.CitySearchQueryImpl;
import org.iglooproject.basicapp.core.business.referencedata.search.ICitySearchQuery;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
