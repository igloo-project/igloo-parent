package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.scrollspy;

import org.apache.wicket.Component;
import org.odlabs.wiquery.core.javascript.JsUtils;
import org.odlabs.wiquery.core.options.Options;
import org.springframework.util.StringUtils;

public class BootstrapScrollSpyOptions extends Options {

	private static final long serialVersionUID = 5736038438319652356L;

	private String targetSelector;

	private Component targetComponent;

	private Integer offset;

	public BootstrapScrollSpyOptions() {
		super();
	}

	@Override
	public CharSequence getJavaScriptOptions() {
		if (targetComponent != null) {
			put("target", JsUtils.quotes("#" + targetComponent.getMarkupId(), true));
		} else if (StringUtils.hasText(targetSelector)) {
			put("target", JsUtils.quotes(targetSelector, true));
		}
		
		if (offset != null) {
			put("offset", offset);
		}
		
		return super.getJavaScriptOptions();
	}


	public String getTargetSelector() {
		return targetSelector;
	}

	public BootstrapScrollSpyOptions setTargetSelector(String targetSelector) {
		this.targetSelector = targetSelector;
		return this;
	}

	public Component getTargetComponent() {
		return targetComponent;
	}

	public BootstrapScrollSpyOptions setTargetComponent(Component targetComponent) {
		this.targetComponent = targetComponent;
		return this;
	}

	public Integer getOffset() {
		return offset;
	}

	public BootstrapScrollSpyOptions setOffset(Integer offset) {
		this.offset = offset;
		return this;
	}
}
