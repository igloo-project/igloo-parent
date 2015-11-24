package fr.openwide.core.jpa.more.business.property.dao;

import org.springframework.stereotype.Repository;

@Repository("immutablePropertyDao")
public class ImmutablePropertyDao implements IImmutablePropertyDao {

	@Override
	public String get(String key) {
		return null;
	}

}
