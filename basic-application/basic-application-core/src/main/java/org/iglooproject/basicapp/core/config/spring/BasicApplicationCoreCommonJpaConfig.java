package org.iglooproject.basicapp.core.config.spring;

import org.apache.lucene.search.SortField;
import org.hibernate.boot.model.TypeContributions;
import org.hibernate.boot.model.TypeContributor;
import org.hibernate.service.ServiceRegistry;
import org.iglooproject.basicapp.core.business.BasicApplicationCoreCommonBusinessPackage;
import org.iglooproject.basicapp.core.business.common.model.EmailAddress;
import org.iglooproject.basicapp.core.business.common.model.PhoneNumber;
import org.iglooproject.basicapp.core.business.common.model.PostalCode;
import org.iglooproject.basicapp.core.business.referencedata.model.ReferenceData;
import org.iglooproject.basicapp.core.business.referencedata.search.BasicReferenceDataSearchQueryImpl;
import org.iglooproject.basicapp.core.business.referencedata.search.IBasicReferenceDataSearchQuery;
import org.iglooproject.basicapp.core.config.hibernate.HibernateConfigPackage;
import org.iglooproject.basicapp.core.config.hibernate.type.EmailAddressType;
import org.iglooproject.basicapp.core.config.hibernate.type.PhoneNumberType;
import org.iglooproject.basicapp.core.config.hibernate.type.PostalCodeType;
import org.iglooproject.jpa.config.spring.provider.JpaPackageScanProvider;
import org.iglooproject.jpa.hibernate.usertype.StringClobType;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Scope;

@Configuration
@EnableAspectJAutoProxy
public class BasicApplicationCoreCommonJpaConfig {

	/**
	 * Déclaration des packages de scan pour l'application.
	 */
	@Bean
	public JpaPackageScanProvider applicationJpaPackageScanProvider() {
		return new JpaPackageScanProvider(
			BasicApplicationCoreCommonBusinessPackage.class.getPackage(),
			HibernateConfigPackage.class.getPackage() // Typedef config
		);
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public <T extends ReferenceData<? super T>, S extends ISort<SortField>> IBasicReferenceDataSearchQuery<T, S> basicReferenceDataSearchQuery(Class<T> clazz) {
		return new BasicReferenceDataSearchQueryImpl<>(clazz);
	}

	@Bean
	public TypeContributor applicationTypeContributor() {
		return new TypeContributor() {
			
			@Override
			public void contribute(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
				typeContributions.contributeType(new StringClobType(), "string", String.class.getName());
				typeContributions.contributeType(new PostalCodeType(), PostalCode.class.getName());
				typeContributions.contributeType(new EmailAddressType(), EmailAddress.class.getName());
				typeContributions.contributeType(new PhoneNumberType(), PhoneNumber.class.getName());
			}
		};
	}

}
