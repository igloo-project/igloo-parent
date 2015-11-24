package fr.openwide.core.jpa.more.business.property.dao;

public interface IMutablePropertyDao {

	String get(String key);

	void set(String key, String value);

}
