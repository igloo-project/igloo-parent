package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.popover;

import org.apache.wicket.Component;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsScopeContext;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.javascript.JsUtils;
import org.odlabs.wiquery.core.options.Options;

/**
 * Options du plugin <a href="http://twitter.github.com/bootstrap/javascript.html#popovers">Bootstrap Popover</a>.
 */
public class BootstrapPopoverOptions extends Options {

	private static final long serialVersionUID = 680573363463468690L;

	private Boolean animation;

	private Boolean html;

	private PopoverPlacement placement;

	private String selector;

	private PopoverTrigger trigger;

	private String titleText;

	private JsScope titleFunction;

	private String contentText;

	private JsScope contentFunction;

	private Integer delay;

	private String container;

	public BootstrapPopoverOptions() {
		super();
	}

	@Override
	public CharSequence getJavaScriptOptions() {
		if (animation != null) {
			put("animation", animation);
		}
		if (html != null) {
			put("html", html);
		}
		if (placement != null) {
			put("placement", JsUtils.quotes(placement.getValue()));
		}
		if (selector != null) {
			put("selector", JsUtils.quotes(selector));
		}
		if (trigger != null) {
			put("trigger", JsUtils.quotes(trigger.getValue()));
		}
		if (titleText != null) {
			put("title", JsUtils.quotes(titleText));
		} else if (titleFunction != null) {
			put("title", titleFunction.render().toString());
		}
		if (contentText != null) {
			put("content", JsUtils.quotes(contentText));
		} else if (contentFunction != null) {
			put("content", contentFunction.render().toString());
		}
		if (delay != null) {
			put("delay", delay);
		}
		if (container != null) {
			put("container", JsUtils.quotes(container));
		}
		
		return super.getJavaScriptOptions();
	}

	public Boolean getAnimation() {
		return animation;
	}

	public void setAnimation(Boolean animation) {
		this.animation = animation;
	}

	public Boolean getHtml() {
		return html;
	}

	public void setHtml(Boolean html) {
		this.html = html;
	}

	public PopoverPlacement getPlacement() {
		return placement;
	}

	public void setPlacement(PopoverPlacement placement) {
		this.placement = placement;
	}

	public String getSelector() {
		return selector;
	}

	public void setSelector(String selector) {
		this.selector = selector;
	}

	public PopoverTrigger getTrigger() {
		return trigger;
	}

	public void setTrigger(PopoverTrigger trigger) {
		this.trigger = trigger;
	}

	public String getTitleText() {
		return titleText;
	}

	public void setTitleText(String titleText) {
		this.titleText = titleText;
	}

	public JsScope getTitleFunction() {
		return titleFunction;
	}

	public void setTitleFunction(JsScope titleFunction) {
		this.titleFunction = titleFunction;
	}

	public void setTitleComponent(final Component titleComponent) {
		titleComponent.setOutputMarkupId(true);
		setTitleFunction(new JsScope() {
			private static final long serialVersionUID = 1L;
			@Override
			protected void execute(JsScopeContext scopeContext) {
				scopeContext.append("return " + new JsStatement().$(titleComponent).chain("html").render());
			}
		});
	}

	public String getContentText() {
		return contentText;
	}

	public void setContentText(String contentText) {
		this.contentText = contentText;
	}

	public JsScope getContentFunction() {
		return contentFunction;
	}

	public void setContentFunction(JsScope contentFunction) {
		this.contentFunction = contentFunction;
	}

	public void setContentComponent(final Component contentComponent) {
		contentComponent.setOutputMarkupId(true);
		setContentFunction(new JsScope() {
			private static final long serialVersionUID = 1L;
			@Override
			protected void execute(JsScopeContext scopeContext) {
				scopeContext.append("return " + new JsStatement().$(contentComponent).chain("html").render());
			}
		});
	}

	public Integer getDelay() {
		return delay;
	}

	public void setDelay(Integer delay) {
		this.delay = delay;
	}

	public String getContainer() {
		return container;
	}

	public void setContainer(String container) {
		this.container = container;
	}

}
