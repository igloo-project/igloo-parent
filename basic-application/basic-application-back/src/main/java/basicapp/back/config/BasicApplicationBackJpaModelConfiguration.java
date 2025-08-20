package basicapp.back.config;

import basicapp.back.business.BasicApplicationBackCommonBusinessPackage;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(BasicApplicationBackManifestConfiguration.class)
@EntityScan(basePackageClasses = BasicApplicationBackCommonBusinessPackage.class)
public class BasicApplicationBackJpaModelConfiguration {}
