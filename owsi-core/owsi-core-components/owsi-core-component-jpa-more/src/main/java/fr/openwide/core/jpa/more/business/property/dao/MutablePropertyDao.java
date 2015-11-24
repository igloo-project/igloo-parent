package fr.openwide.core.jpa.more.business.property.dao;

import org.springframework.stereotype.Repository;

@Repository("mutablePropertyDao")
public class MutablePropertyDao implements IMutablePropertyDao {

	@Override
	public String get(String key) {
		return null;
	}

	@Override
	public void set(String key, String value) {
	}

}
