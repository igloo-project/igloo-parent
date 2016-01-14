package fr.openwide.core.wicket.more.rendering;

import java.util.Locale;

import org.apache.wicket.model.Model;

import fr.openwide.core.spring.util.StringUtils;

public class BooleanRenderer extends Renderer<Boolean> {

	private static final long serialVersionUID = -6934415690685574154L;
	
	private static BooleanRenderer INSTANCE = BooleanRenderer.withPrefix("common.boolean");
	
	public static BooleanRenderer get() {
		return INSTANCE;
	}
	
	public static BooleanRenderer withPrefix(String prefix) {
		return with(prefix, null);
	}
	
	public static BooleanRenderer withSuffix(String suffix) {
		return with(null, suffix);
	}
	
	public static BooleanRenderer with(String prefix, String suffix) {
		return new BooleanRenderer(prefix, suffix);
	}
	
	private String prefix = null;
	
	private String suffix = null;
	
	/**
	 * @deprecated Use the static factory methods instead.
	 */
	@Deprecated
	protected BooleanRenderer() { }

	/**
	 * @deprecated Use the static factory methods instead.
	 */
	@Deprecated
	protected BooleanRenderer(String prefix, String suffix) {
		this();
		this.prefix = prefix;
		this.suffix = suffix;
	}

	@Override
	public String render(Boolean value, Locale locale) {
		if (value == null) {
			return null;
		}
		
		StringBuilder key = new StringBuilder();
		
		if (StringUtils.hasText(prefix)) {
			key.append(prefix).append(".");
		}
		
		key.append(resourceKey(value));
		
		if (StringUtils.hasText(suffix)) {
			key.append(".").append(suffix);
		}
		return getString(key.toString(), locale, Model.of(value));
	}

	/**
	 * @deprecated Do not override this.
	 */
	@Deprecated
	protected String resourceKey(Boolean value) {
		return value.toString();
	}

}
