package fr.openwide.core.jpa.migration.rowmapper;

import java.util.Map;

public abstract class AbstractEntityMapRowMapper<K,V> extends AbstractEntityRowMapper<Map<K, V>> {

	protected AbstractEntityMapRowMapper(Map<K,V> results) {
		super(results);
	}
}
