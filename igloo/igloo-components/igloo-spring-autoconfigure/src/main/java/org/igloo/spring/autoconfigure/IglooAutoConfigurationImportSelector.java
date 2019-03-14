package org.igloo.spring.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfigurationImportSelector;

public class IglooAutoConfigurationImportSelector extends AutoConfigurationImportSelector {

	@Override
	protected Class<?> getAnnotationClass() {
		return EnableIglooAutoConfiguration.class;
	}

	@Override
	protected Class<?> getSpringFactoriesLoaderFactoryClass() {
		return EnableIglooAutoConfiguration.class;
	}

}
