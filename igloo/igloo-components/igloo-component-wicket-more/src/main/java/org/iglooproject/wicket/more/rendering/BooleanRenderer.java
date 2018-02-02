package org.iglooproject.wicket.more.rendering;

import java.util.Locale;

import org.apache.wicket.model.Model;
import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRenderer;
import org.iglooproject.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRendererInformation;

public class BooleanRenderer extends BootstrapRenderer<Boolean> {

	private static final long serialVersionUID = -6934415690685574154L;
	
	private static final BooleanRenderer INSTANCE = BooleanRenderer.withPrefix("common.boolean");
	
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
	
	private BooleanRenderer() {
		super();
	}
	
	private BooleanRenderer(String prefix, String suffix) {
		this();
		this.prefix = prefix;
		this.suffix = suffix;
	}

	@Override
	protected BootstrapRendererInformation doRender(Boolean value, Locale locale) {
		if (value == null) {
			return null;
		}
		if (value) {
			return BootstrapRendererInformation.builder()
					.label(renderLabel(value, locale))
					.icon("fa fa-check")
					// No color
					.build();
		} else {
			return BootstrapRendererInformation.builder()
					.label(renderLabel(value, locale))
					.icon("fa fa-times")
					// No color
					.build();
		}
	}

	private String renderLabel(Boolean value, Locale locale) {
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
