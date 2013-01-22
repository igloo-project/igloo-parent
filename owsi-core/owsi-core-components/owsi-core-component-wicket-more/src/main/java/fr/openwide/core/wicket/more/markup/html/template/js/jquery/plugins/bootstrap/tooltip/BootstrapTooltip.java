package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.tooltip;

import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsUtils;
import org.odlabs.wiquery.core.options.Options;

/**
 * Voir documentation sur http://twitter.github.com/bootstrap/javascript.html#tooltips
 */
public class BootstrapTooltip implements IBootstrapTooltip {

	private static final long serialVersionUID = -2571067418057286728L;

	private Boolean animation;

	private Placement placement;

	private JsScope placementFunction;

	private String selector;

	private String title;

	private Trigger trigger;

	private Integer delay;

	private Integer delayShow;

	private Integer delayHide;

	private Boolean html;

	private String container;

	public Boolean getAnimation() {
		return animation;
	}

	public void setAnimation(Boolean animation) {
		this.animation = animation;
	}

	public Placement getPlacement() {
		return placement;
	}

	/**
	 * Ignoré si {@link BootstrapTooltip#placementFunction} est spécifié.
	 */
	public void setPlacement(Placement placement) {
		this.placement = placement;
	}

	public JsScope getPlacementFunction() {
		return placementFunction;
	}

	public void setPlacementFunction(JsScope placementFunction) {
		this.placementFunction = placementFunction;
	}

	@Override
	public String getSelector() {
		return selector;
	}

	public void setSelector(String selector) {
		this.selector = selector;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Trigger getTrigger() {
		return trigger;
	}

	public void setTrigger(Trigger trigger) {
		this.trigger = trigger;
	}

	public Integer getDelay() {
		return delay;
	}

	/**
	 * Ignoré si {@link BootstrapTooltip#delayHide} ou {@link BootstrapTooltip#delayShow} est spécifié.
	 */
	public void setDelay(Integer delay) {
		this.delay = delay;
	}

	public Integer getDelayShow() {
		return delayShow;
	}

	public void setDelayShow(Integer delayShow) {
		this.delayShow = delayShow;
	}

	public Integer getDelayHide() {
		return delayHide;
	}

	public void setDelayHide(Integer delayHide) {
		this.delayHide = delayHide;
	}

	public Boolean getHtml() {
		return html;
	}

	public void setHtml(Boolean html) {
		this.html = html;
	}

	public String getContainer() {
		return container;
	}

	public void setContainer(String container) {
		this.container = container;
	}

	@Override
	public String chainLabel() {
		return "tooltip";
	}

	@Override
	public CharSequence[] statementArgs() {
		Options options = new Options();
		
		if (animation != null) {
			options.put("animation", animation);
		}
		
		if (placement != null && placementFunction == null) {
			options.put("placement", JsUtils.quotes(placement.getValue()));
		}
		
		if (placementFunction != null) {
			options.put("placement", placementFunction);
		}
		
		if (selector != null) {
			options.put("selector", JsUtils.quotes(selector));
		}
		
		if (title != null) {
			options.put("title", JsUtils.quotes(title));
		}
		
		if (trigger != null) {
			options.put("trigger", JsUtils.quotes(trigger.getValue()));
		}
		
		if (delay != null && delayShow == null && delayHide != null) {
			options.put("delay", delay);
		}
		
		if (delayShow != null || delayHide != null) {
			Options delayOptions = new Options();
			if (delayShow != null) {
				delayOptions.put("show", delayShow);
			}
			if (delayHide != null) {
				delayOptions.put("hide", delayHide);
			}
			options.put("delay", delayOptions.getJavaScriptOptions().toString());
		}
		
		if (html != null) {
			options.put("html", html);
		}
		
		if (container != null) {
			options.put("container", JsUtils.quotes(container));
		}
		
		return new CharSequence[] { options.getJavaScriptOptions() };
	}

	public enum Placement {
		TOP("top"),
		BOTTOM("bottom"),
		LEFT("left"),
		RIGHT("right");
		
		private String value;
		
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
		
		private String value;
		
		private Trigger(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
	}

}
