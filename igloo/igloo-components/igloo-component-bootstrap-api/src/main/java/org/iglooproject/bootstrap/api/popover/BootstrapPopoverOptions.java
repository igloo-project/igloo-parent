package org.iglooproject.bootstrap.api.popover;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import org.iglooproject.functional.Predicates2;
import org.wicketstuff.wiquery.core.javascript.JsScope;
import org.wicketstuff.wiquery.core.javascript.JsStatement;
import org.wicketstuff.wiquery.core.options.ArrayItemOptions;
import org.wicketstuff.wiquery.core.options.IComplexOption;
import org.wicketstuff.wiquery.core.options.LiteralOption;
import org.wicketstuff.wiquery.core.options.Options;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class BootstrapPopoverOptions extends Options implements IBootstrapPopoverOptions {

	private static final long serialVersionUID = 904537124671042670L;

	public static final String TEMPLATE = ""
		+ "<div class=\"popover\" role=\"tooltip\">"
		+ 	"<div class=\"popover-arrow\"></div>"
		+ 	"<div class=\"popover-header\"></div>"
		+ 	"<div class=\"popover-body\"></div>"
		+ "</div>";

	public static final BootstrapPopoverOptions get() {
		return new BootstrapPopoverOptions()
			.html(true)
			.placement(Placement.AUTO);
	}

	public BootstrapPopoverOptions() {
		template(TEMPLATE);
	}

	public BootstrapPopoverOptions animation(boolean animation) {
		put("animation", animation);
		return this;
	}

	public BootstrapPopoverOptions container(String container) {
		Objects.requireNonNull(container);
		putLiteral("container", container);
		return this;
	}

	public BootstrapPopoverOptions container(boolean container) {
		Preconditions.checkArgument(!container);
		put("container", container);
		return this;
	}

	public BootstrapPopoverOptions content(String content) {
		Objects.requireNonNull(content);
		putLiteral("content", content);
		return this;
	}

	public BootstrapPopoverOptions content(JsStatement content) {
		Objects.requireNonNull(content);
		put("content", content.render(false).toString());
		return this;
	}

	public BootstrapPopoverOptions content(JsScope content) {
		Objects.requireNonNull(content);
		put("content", content);
		return this;
	}

	public BootstrapPopoverOptions delay(int delay) {
		return delay(delay, delay);
	}

	public BootstrapPopoverOptions delay(int show, int hide) {
		put("delay", new DelayOptions(show, hide));
		return this;
	}

	public BootstrapPopoverOptions html(boolean html) {
		put("html", html);
		return this;
	}

	public BootstrapPopoverOptions placement(Placement placement) {
		Objects.requireNonNull(placement);
		putLiteral("placement", placement.getValue());
		return this;
	}

	public BootstrapPopoverOptions placement(JsScope placement) {
		Objects.requireNonNull(placement);
		put("placement", placement);
		return this;
	}

	public BootstrapPopoverOptions selector(String selector) {
		Objects.requireNonNull(selector);
		putLiteral("selector", selector);
		return this;
	}

	public BootstrapPopoverOptions selector(boolean selector) {
		Preconditions.checkArgument(!selector);
		put("selector", selector);
		return this;
	}

	public BootstrapPopoverOptions template(String template) {
		Objects.requireNonNull(template);
		putLiteral("template", template);
		return this;
	}

	public BootstrapPopoverOptions title(String title) {
		Objects.requireNonNull(title);
		putLiteral("title", title);
		return this;
	}

	public BootstrapPopoverOptions title(JsStatement title) {
		Objects.requireNonNull(title);
		put("title", title.render(false).toString());
		return this;
	}

	public BootstrapPopoverOptions title(JsScope title) {
		Objects.requireNonNull(title);
		put("title", title);
		return this;
	}

	public BootstrapPopoverOptions trigger(Trigger trigger, Trigger ... rest) {
		return trigger(Lists.asList(trigger, rest));
	}

	public BootstrapPopoverOptions trigger(Collection<Trigger> triggers) {
		Objects.requireNonNull(triggers);
		putLiteral(
			"trigger",
			triggers
				.stream()
				.map(Trigger::getValue)
				.filter(Predicates2.hasText())
				.collect(Collectors.joining(" "))
		);
		return this;
	}

	public BootstrapPopoverOptions fallbackPlacements(Placement placement, Placement ... rest) {
		return fallbackPlacements(Lists.asList(placement, rest));
	}

	public BootstrapPopoverOptions fallbackPlacements(Collection<Placement> placements) {
		Objects.requireNonNull(placements);
		put(
			"fallbackPlacements",
			placements
				.stream()
				.map(Placement::getValue)
				.filter(Predicates2.hasText())
				.map(LiteralOption::new)
				.collect(Collectors.toCollection(ArrayItemOptions<LiteralOption>::new))
		);
		return this;
	}

	public BootstrapPopoverOptions boundary(Boundary boundary) {
		Objects.requireNonNull(boundary);
		putLiteral("boundary", boundary.getValue());
		return this;
	}

	public BootstrapPopoverOptions boundary(JsStatement boundary) {
		Objects.requireNonNull(boundary);
		put("boundary", boundary.render(false).toString());
		return this;
	}

	public BootstrapPopoverOptions customClass(String customClass) {
		Objects.requireNonNull(customClass);
		putLiteral("customClass", customClass);
		return this;
	}

	public BootstrapPopoverOptions customClass(JsScope customClass) {
		Objects.requireNonNull(customClass);
		put("customClass", customClass);
		return this;
	}

	public BootstrapPopoverOptions sanitize(boolean sanitize) {
		Objects.requireNonNull(sanitize);
		put("sanitize", sanitize);
		return this;
	}

	public static class DelayOptions extends Options implements IComplexOption {
		
		private static final long serialVersionUID = 1L;
		
		private int show;
		
		private int hide;
		
		public DelayOptions() {
		}
		
		public DelayOptions(int show, int hide) {
			this.show = show;
			this.hide = hide;
		}
		
		public DelayOptions show(int show) {
			this.show = show;
			return this;
		}
		
		public DelayOptions hide(int hide) {
			this.hide = hide;
			return this;
		}
		
		@Override
		public CharSequence getJavascriptOption() {
			return new Options()
				.put("show", show)
				.put("hide", hide)
				.getJavaScriptOptions();
		}
	}

	public enum Placement {
		AUTO("auto"),
		TOP("top"),
		BOTTOM("bottom"),
		LEFT("left"),
		RIGHT("right");
		
		private final String value;
		
		private Placement(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
	}

	public enum Trigger {
		CLICK("click"),
		HOVER("hover"),
		FOCUS("focus"),
		MANUAL("manual");
		
		private final String value;
		
		private Trigger(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
	}

	public enum Boundary {
		CLIPPING_PARENTS("clippingParents");
		
		private final String value;
		
		private Boundary(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
	}

}
