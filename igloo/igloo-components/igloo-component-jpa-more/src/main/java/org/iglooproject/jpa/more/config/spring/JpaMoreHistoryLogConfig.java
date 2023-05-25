package org.iglooproject.jpa.more.config.spring;

import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.spi.MetadataBuilderContributor;
import org.iglooproject.commons.util.fieldpath.FieldPath;
import org.iglooproject.jpa.more.business.history.hibernate.FieldPathType;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaMoreHistoryLogConfig {

	@Bean
	public HibernatePropertiesCustomizer typeCustomizer() {
		return hp -> {
			hp.put("hibernate.metadata_builder_contributor", new TypeMetadataBuilderContributor());
		};
	}

	public static class TypeMetadataBuilderContributor implements MetadataBuilderContributor {
		@Override
		public void contribute(MetadataBuilder metadataBuilder) {
			metadataBuilder.applyBasicType(new FieldPathType(), FieldPath.class.getName());
		}
	}

}
