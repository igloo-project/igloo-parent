package fr.openwide.core.jpa.more.business.property.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.openwide.core.spring.config.CorePropertyPlaceholderConfigurer;

@Repository("immutablePropertyDao")
public class ImmutablePropertyDaoImpl implements IImmutablePropertyDao {

	@Autowired
	private CorePropertyPlaceholderConfigurer configurer;

	@Override
	public String get(String key) {
		return configurer.getPropertyAsString(key);
	}

}
