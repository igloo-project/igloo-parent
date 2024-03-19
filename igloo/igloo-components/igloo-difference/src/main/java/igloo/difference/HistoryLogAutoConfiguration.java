package igloo.difference;

import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.spi.MetadataBuilderContributor;
import org.iglooproject.commons.util.fieldpath.FieldPath;
import org.iglooproject.jpa.more.autoconfigure.JpaMoreAutoConfiguration;
import org.iglooproject.jpa.more.business.difference.factory.DefaultHistoryDifferenceFactory;
import org.iglooproject.jpa.more.business.history.hibernate.FieldPathType;
import org.iglooproject.jpa.more.business.history.transaction.HistoryLogTransactionSynchronizationTaskMerger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@AutoConfiguration(after = JpaMoreAutoConfiguration.class)
@ConditionalOnBean(JpaMoreAutoConfiguration.class)
public class HistoryLogAutoConfiguration {

	@SuppressWarnings("rawtypes")
	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public DefaultHistoryDifferenceFactory<?> defaultHistoryDifferenceFactory() {
		return new DefaultHistoryDifferenceFactory();
	}

	@Bean
	public HistoryLogTransactionSynchronizationTaskMerger historyLogTransactionSynchronizationTaskMerger() {
		return new HistoryLogTransactionSynchronizationTaskMerger();
	}

	@Bean
	public MetadataBuilderContributor historyLogMetadataBuilderContributor() {
		return new TypeMetadataBuilderContributor();
	}

	public static class TypeMetadataBuilderContributor implements MetadataBuilderContributor {
		@Override
		public void contribute(MetadataBuilder metadataBuilder) {
			metadataBuilder.applyBasicType(new FieldPathType(), FieldPath.class.getName());
		}
	}

}
