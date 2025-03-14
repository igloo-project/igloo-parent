package basicapp.back.config.spring;

import basicapp.back.business.BasicApplicationCoreCommonBusinessPackage;
import basicapp.back.business.common.model.EmailAddress;
import basicapp.back.business.common.model.PhoneNumber;
import basicapp.back.business.common.model.PostalCode;
import basicapp.back.config.hibernate.type.EmailAddressType;
import basicapp.back.config.hibernate.type.PhoneNumberType;
import basicapp.back.config.hibernate.type.PostalCodeType;
import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.spi.MetadataBuilderContributor;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackageClasses = BasicApplicationCoreCommonBusinessPackage.class)
public class BasicApplicationJpaModelConfiguration {

  @Bean
  public MetadataBuilderContributor basicApplicationMetadataBuilderContributor() {
    return new TypeMetadataBuilderContributor();
  }

  public static class TypeMetadataBuilderContributor implements MetadataBuilderContributor {
    @Override
    public void contribute(MetadataBuilder metadataBuilder) {
      metadataBuilder.applyBasicType(new EmailAddressType(), EmailAddress.class.getName());
      metadataBuilder.applyBasicType(new PhoneNumberType(), PhoneNumber.class.getName());
      metadataBuilder.applyBasicType(new PostalCodeType(), PostalCode.class.getName());
    }
  }
}
