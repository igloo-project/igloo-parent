package igloo.hibernateconfig.api;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum HibernateCacheRegionFactory {

	JCACHE_CAFFEINE("jcache-caffeine", "jcache", "com.github.benmanes.caffeine.jcache.spi.CaffeineCachingProvider"),
	EHCACHE("ehcache", "ehcache", null),
	EHCACHE_SINGLETON("ehcache-singleton", "ehcache-singleton", null),
	NONE("none", null, null);

	private static final  Map<String, HibernateCacheRegionFactory> MAPPING;
	static {
		MAPPING = Map.copyOf(Arrays.stream(HibernateCacheRegionFactory.values())
			.collect(Collectors.toMap(HibernateCacheRegionFactory::getAlias, Function.identity())));
	};

	public static HibernateCacheRegionFactory fromString(String value) {
		// null/empty values are mapped to none
		String notNullNotEmptyValue = Optional.ofNullable(value)
				.filter(i -> !i.isBlank())
				.orElse(NONE.getAlias());
		return Optional.ofNullable(MAPPING.get(notNullNotEmptyValue))
				.orElseThrow(() -> new IllegalStateException(
						String.format("Value %s is not a valid HibernateCacheRegionFactory alias", value)));
	}

	private final String alias;
	private final String regionFactoryAlias;
	private final String jcacheProvider;

	private HibernateCacheRegionFactory(String alias, String regionFactoryAlias, String jcacheProvider) {
		this.alias = alias;
		this.regionFactoryAlias = regionFactoryAlias;
		this.jcacheProvider = jcacheProvider;
	}

	public String getAlias() {
		return alias;
	}

	public String getRegionFactoryAlias() {
		return regionFactoryAlias;
	}

	public boolean isDisabled() {
		return NONE.equals(this);
	}

	public boolean isEhcache2() {
		return EHCACHE.equals(this) || EHCACHE_SINGLETON.equals(this);
	}

	public boolean isJcache() {
		return JCACHE_CAFFEINE.equals(this);
	}

	public String getJcacheProvider() {
		return jcacheProvider;
	}

	@Override
	public String toString() {
		return getAlias();
	}

}
