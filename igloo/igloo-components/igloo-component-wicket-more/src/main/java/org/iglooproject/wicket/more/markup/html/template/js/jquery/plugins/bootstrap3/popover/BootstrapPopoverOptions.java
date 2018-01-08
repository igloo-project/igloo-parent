package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap3.popover;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap3.SimpleOptions;
import org.wicketstuff.wiquery.core.javascript.JsScope;
import org.wicketstuff.wiquery.core.javascript.JsScopeContext;
import org.wicketstuff.wiquery.core.javascript.JsStatement;
import org.wicketstuff.wiquery.core.javascript.JsUtils;

import com.google.common.collect.ImmutableMap;

/**
 * @deprecated Use Bootstrap 4 CSS et JS from now on.
 */
@Deprecated
public class BootstrapPopoverOptions extends SimpleOptions {

	private static final long serialVersionUID = 680573363463468690L;

	// Igloo options
	private boolean addCloseButton = true;

	// Bootstrap popover options
	private Boolean animation;

	private Boolean html;

	private PopoverPlacement placement;

	private String selector;

	private PopoverTrigger trigger;

	private String titleText;
	
	private IModel<String> titleModel;

	private Component titleComponent;

	private JsScope titleFunction;

	private String contentText;
	
	private IModel<String> contentModel;

	private Component contentComponent;
	
	private JsScope contentFunction;

	private Integer delay;

	private String container;

	private IModel<String> cssClassModel;

	public BootstrapPopoverOptions() {
		super();
	}

	public CharSequence getJavaScriptOptions(Component popoverComponent) {
		ImmutableMap.Builder<String, Object> options = ImmutableMap.builder();
		
		if (animation != null) {
			options.put("animation", animation);
		}
		if (html != null) {
			options.put("html", html);
		}
		if (placement != null) {
			options.put("placement", JsUtils.quotes(placement.getValue()));
		}
		if (selector != null) {
			options.put("selector", JsUtils.quotes(selector));
		}
		if (trigger != null) {
			options.put("trigger", JsUtils.quotes(trigger.getValue()));
		}
		String computedTitleText = (titleModel != null ? titleModel.getObject() : titleText);
		if (computedTitleText != null) {
			// Si on veut ajouter un bouton de fermeture, on doit passer par une fonction,
			// sinon on passe par l'option titleText classique.
			if (addCloseButton) {
				options.put("title", getTitleFunction(popoverComponent, new JsStatement().append(JsUtils.quotes(computedTitleText, true))));
			} else {
				options.put("title", JsUtils.quotes(computedTitleText, true));
			}
		} else if (titleComponent != null) {
			options.put("title", getTitleFunction(popoverComponent, new JsStatement().$(titleComponent).chain("html")));
		} else if (titleFunction != null) {
			options.put("title", titleFunction.render().toString());
		}
		if (contentModel != null) {
			options.put("content", JsUtils.quotes(contentModel.getObject(), true));
		} else if (contentText != null) {
			options.put("content", JsUtils.quotes(contentText, true));
		} else if (contentComponent != null) {
			options.put("content", new JsScope() {
				private static final long serialVersionUID = 1L;
				@Override
				protected void execute(JsScopeContext scopeContext) {
					scopeContext.append("return " + new JsStatement().$(contentComponent).chain("html").render());
				}
			}.render().toString());
		} else if (contentFunction != null) {
			options.put("content", contentFunction.render().toString());
		}
		if (delay != null) {
			options.put("delay", delay);
		}
		if (container != null) {
			options.put("container", JsUtils.quotes(container));
		}
		if (cssClassModel != null) {
			options.put("cssClass", JsUtils.quotes(cssClassModel.getObject()));
		}
		
		return super.getJavaScriptOptions(options.build());
	}

	private JsScope getTitleFunction(final Component popoverComponent, final JsStatement titleComponentStatement) {
		return new JsScope() {
			private static final long serialVersionUID = 1L;
			@Override
			protected void execute(JsScopeContext scopeContext) {
				if (addCloseButton) {
					scopeContext
							.append("var titleHtml = " + titleComponentStatement.render())
							.append("titleHtml = titleHtml.concat(")
							.append(
									JsUtils.quotes(
											"<a class=\"close\""
											// Note : c'est moche, mais au moins ça marche. On renvoie bien du *html* ici,
											// ajouter des bindings jquery n'aura aucun effet.
											+ " onclick=\"new function() {"
											+ new JsStatement().$(popoverComponent).chain("popover", "'toggle'").render() + " return false;"
											+ "}\""
											+ ">&times;</a>",
											true
									)
							)
							.append(");");
				}
				scopeContext.append("return titleHtml;");
			}
		};
	}

	// Igloo options
	public boolean isAddCloseButton() {
		return addCloseButton;
	}

	public void setAddCloseButton(boolean addCloseButton) {
		this.addCloseButton = addCloseButton;
	}

	// Bootstrap popover options
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
	
	public IModel<String> getTitleModel() {
		return titleModel;
	}

	public void setTitleModel(IModel<String> titleModel) {
		this.titleModel = titleModel;
	}

	public Component getTitleComponent() {
		return titleComponent;
	}

	public void setTitleComponent(Component titleComponent) {
		this.titleComponent = titleComponent;
	}

	public JsScope getTitleFunction() {
		return titleFunction;
	}

	public void setTitleFunction(JsScope titleFunction) {
		this.titleFunction = titleFunction;
	}

	public String getContentText() {
		return contentText;
	}

	public void setContentText(String contentText) {
		this.contentText = contentText;
	}
	
	public IModel<String> getContentModel() {
		return contentModel;
	}

	public void setContentModel(IModel<String> contentModel) {
		this.contentModel = contentModel;
	}

	public Component getContentComponent() {
		return contentComponent;
	}

	public void setContentComponent(Component contentComponent) {
		this.contentComponent = contentComponent;
	}

	public JsScope getContentFunction() {
		return contentFunction;
	}

	public void setContentFunction(JsScope contentFunction) {
		this.contentFunction = contentFunction;
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
	
	public IModel<String> getCssClassModel() {
		return cssClassModel;
	}

	public void setCssClass(String cssClass) {
		setCssClass(Model.of(cssClass));
	}

	public void setCssClass(IModel<String> cssClassModel) {
		this.cssClassModel = cssClassModel;
	}

	@Override
	public void detach() {
		if (contentModel != null) {
			contentModel.detach();
		}
		if (titleModel != null) {
			titleModel.detach();
		}
		if (cssClassModel != null) {
			cssClassModel.detach();
		}
	}

}
