package org.iglooproject.wicket.more.markup.html.template.js.bootstrap.popover;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.text.StrSubstitutor;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.SimpleOptions;
import org.iglooproject.wicket.more.util.component.ComponentUtils;
import org.iglooproject.wicket.more.util.model.Detachables;
import org.wicketstuff.wiquery.core.javascript.JsScope;
import org.wicketstuff.wiquery.core.javascript.JsScopeContext;
import org.wicketstuff.wiquery.core.javascript.JsStatement;
import org.wicketstuff.wiquery.core.javascript.JsUtils;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

public class BootstrapPopoverOptions extends SimpleOptions {

	private static final long serialVersionUID = 680573363463468690L;

	private static final String TEMPLATE_POPOVER_CSS_CLASS = "POPOVER-CSS-CLASS";

	private static final String TEMPLATE = ""
			+ "<div class=\"popover ${" + TEMPLATE_POPOVER_CSS_CLASS + "}\" role=\"tooltip\">"
			+ 	"<div class=\"arrow\"></div>"
			+ 	"<h3 class=\"popover-header\"></h3>"
			+ 	"<div class=\"popover-body\"></div>"
			+ "</div>";

	private static final String POPOVER_CSS_CLASS_POPOVER_MODAL = "popover-modal";

	// Igloo options
	private IModel<Boolean> closableModel = Condition.alwaysTrue();

	// Bootstrap popover options
	private IModel<Boolean> animationModel;

	private IModel<Boolean> htmlModel;

	private IModel<? extends Collection<PopoverPlacement>> placementsModel;

	private IModel<PopoverTrigger> triggerModel;

	private IModel<String> titleModel;

	private Component titleComponent;

	private JsScope titleFunction;

	private IModel<String> contentModel;

	private Component contentComponent;
	
	private JsScope contentFunction;

	private IModel<Integer> delayModel;

	private IModel<String> containerModel;

	private Component containerComponent;

	private IModel<String> selectorModel;

	private IModel<String> cssClassModel;

	public static final BootstrapPopoverOptions get() {
		return new BootstrapPopoverOptions()
				.closable()
				.animation()
				.html()
				.placement(PopoverPlacement.AUTO)
				.trigger(PopoverTrigger.CLICK);
	}

	public BootstrapPopoverOptions() {
		super();
	}

	public CharSequence getJavaScriptOptions(Component component) {
		ImmutableMap.Builder<String, Object> options = ImmutableMap.builder();
		
		if (animationModel != null && animationModel.getObject()) {
			options.put("animation", animationModel.getObject());
		}
		
		if (htmlModel != null && htmlModel.getObject() != null) {
			options.put("html", htmlModel.getObject());
		}
		
		if (placementsModel != null && placementsModel.getObject() != null && !placementsModel.getObject().isEmpty()) {
			options.put("placement", JsUtils.quotes(placementsModel.getObject().stream().map(p -> p.getValue()).collect(Collectors.joining(" "))));
		}
		
		if (triggerModel != null && triggerModel.getObject() != null) {
			options.put("trigger", JsUtils.quotes(triggerModel.getObject().getValue()));
		}
		
		if (titleModel != null && titleModel.getObject() != null) {
			options.put("title", getTitleFunction(component, new JsStatement().append(JsUtils.quotes(titleModel.getObject(), true))));
		} else if (titleComponent != null) {
			options.put("title", getTitleFunction(component, new JsStatement().$(titleComponent).chain("html")));
		} else if (titleFunction != null) {
			options.put("title", getTitleFunction(component, new JsStatement().append(titleFunction.render().toString())));
		}
		
		if (contentModel != null) {
			options.put("content", JsUtils.quotes(contentModel.getObject(), true));
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
		
		if (delayModel != null && delayModel.getObject() != null) {
			options.put("delay", delayModel.getObject());
		}
		
		if (containerModel != null && containerModel.getObject() != null) {
			options.put("container", JsUtils.quotes(containerModel.getObject()));
		} else if (containerComponent != null) {
			options.put("container", JsUtils.quotes(containerComponent.getMarkupId()));
		}
		
		if (selectorModel != null && selectorModel.getObject() != null) {
			options.put("selector", JsUtils.quotes(selectorModel.getObject()));
		}
		
		String cssClass = Stream.of(
				cssClassModel != null ? cssClassModel.getObject() : null,
				ComponentUtils.anyParentModal(component) ? POPOVER_CSS_CLASS_POPOVER_MODAL : null
		)
				.filter(s -> StringUtils.hasText(s))
				.collect(Collectors.joining(" "));
		
		options.put("template", JsUtils.quotes(
				new StrSubstitutor(
						ImmutableMap.<String, String>builder()
								.put(TEMPLATE_POPOVER_CSS_CLASS, cssClass)
								.build()
				)
						.replace(TEMPLATE)
		));
		
		return super.getJavaScriptOptions(options.build());
	}

	private JsScope getTitleFunction(final Component component, final JsStatement titleComponentStatement) {
		return new JsScope() {
			private static final long serialVersionUID = 1L;
			@Override
			protected void execute(JsScopeContext scopeContext) {
				if (Boolean.TRUE.equals(closableModel.getObject())) {
					scopeContext
							.append("var titleHtml = " + titleComponentStatement.render())
							.append("titleHtml = titleHtml.concat(")
							.append(
									JsUtils.quotes(
											"<a class=\"close\""
											// Note : c'est moche, mais au moins ça marche. On renvoie bien du *html* ici,
											// ajouter des bindings jquery n'aura aucun effet.
											+ " onclick=\"new function() {"
											+ new JsStatement().$(component).chain("popover", "'hide'").render() + " return false;"
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

	public IModel<Boolean> getClosableModel() {
		return closableModel;
	}

	public BootstrapPopoverOptions closable(IModel<Boolean> closableModel) {
		this.closableModel = closableModel;
		return this;
	}

	public BootstrapPopoverOptions closable(boolean closable) {
		return closable(Model.of(closable));
	}

	public BootstrapPopoverOptions closable() {
		return closable(true);
	}

	public IModel<Boolean> getAnimationModel() {
		return animationModel;
	}

	public BootstrapPopoverOptions animation(IModel<Boolean> animationModel) {
		this.animationModel = animationModel;
		return this;
	}

	public BootstrapPopoverOptions animation(boolean animation) {
		return animation(Model.of(animation));
	}

	public BootstrapPopoverOptions animation() {
		return animation(true);
	}

	public IModel<Boolean> getHtmlModel() {
		return htmlModel;
	}

	public BootstrapPopoverOptions html(IModel<Boolean> htmlModel) {
		this.htmlModel = htmlModel;
		return this;
	}

	public BootstrapPopoverOptions html(boolean html) {
		return html(Model.of(html));
	}

	public BootstrapPopoverOptions html() {
		return html(true);
	}

	public IModel<? extends Collection<PopoverPlacement>> getPlacementsModel() {
		return placementsModel;
	}

	public BootstrapPopoverOptions placement(IModel<? extends Collection<PopoverPlacement>> placementsModel) {
		this.placementsModel = placementsModel;
		return this;
	}

	public BootstrapPopoverOptions placement(PopoverPlacement placement) {
		return placement(Lists.newArrayList(placement));
	}

	public BootstrapPopoverOptions placement(PopoverPlacement firstPlacement, PopoverPlacement ... otherPlacements) {
		return placement(Lists.asList(firstPlacement, otherPlacements));
	}

	public BootstrapPopoverOptions placement(Collection<PopoverPlacement> placements) {
		return placement(Model.of(placements));
	}

	public IModel<PopoverTrigger> getTriggerModel() {
		return triggerModel;
	}

	public BootstrapPopoverOptions trigger(IModel<PopoverTrigger> triggerModel) {
		this.triggerModel = triggerModel;
		return this;
	}

	public BootstrapPopoverOptions trigger(PopoverTrigger trigger) {
		return trigger(Model.of(trigger));
	}

	public IModel<String> getTitleModel() {
		return titleModel;
	}

	public BootstrapPopoverOptions title(IModel<String> titleModel) {
		this.titleModel = titleModel;
		return this;
	}

	public BootstrapPopoverOptions title(String title) {
		return title(Model.of(title));
	}

	public Component getTitleComponent() {
		return titleComponent;
	}

	public BootstrapPopoverOptions title(Component titleComponent) {
		this.titleComponent = titleComponent;
		return this;
	}

	public JsScope getTitleFunction() {
		return titleFunction;
	}

	public BootstrapPopoverOptions title(JsScope titleFunction) {
		this.titleFunction = titleFunction;
		return this;
	}

	public IModel<String> getContentModel() {
		return contentModel;
	}

	public BootstrapPopoverOptions content(IModel<String> contentModel) {
		this.contentModel = contentModel;
		return this;
	}

	public BootstrapPopoverOptions content(String content) {
		return content(Model.of(content));
	}

	public Component getContentComponent() {
		return contentComponent;
	}

	public BootstrapPopoverOptions content(Component contentComponent) {
		this.contentComponent = contentComponent;
		return this;
	}

	public JsScope getContentFunction() {
		return contentFunction;
	}

	public BootstrapPopoverOptions content(JsScope contentFunction) {
		this.contentFunction = contentFunction;
		return this;
	}

	public IModel<Integer> getDelayModel() {
		return delayModel;
	}

	public BootstrapPopoverOptions delay(IModel<Integer> delayModel) {
		this.delayModel = delayModel;
		return this;
	}

	public BootstrapPopoverOptions delay(Integer delay) {
		return delay(Model.of(delay));
	}

	public IModel<String> getContainerModel() {
		return containerModel;
	}

	public BootstrapPopoverOptions container(IModel<String> containerModel) {
		this.containerModel = containerModel;
		return this;
	}

	public BootstrapPopoverOptions container(String container) {
		return container(Model.of(container));
	}

	public BootstrapPopoverOptions container(Component containerComponent) {
		this.containerComponent = containerComponent;
		return this;
	}

	public IModel<String> getSelectorModel() {
		return selectorModel;
	}

	public BootstrapPopoverOptions selector(IModel<String> selectorModel) {
		this.selectorModel = selectorModel;
		return this;
	}

	public BootstrapPopoverOptions selector(String selector) {
		return selector(Model.of(selector));
	}

	public IModel<String> getCssClassModel() {
		return cssClassModel;
	}

	public BootstrapPopoverOptions cssClass(IModel<String> cssClassModel) {
		this.cssClassModel = cssClassModel;
		return this;
	}

	public BootstrapPopoverOptions cssClass(String cssClass) {
		return cssClass(Model.of(cssClass));
	}

	@Override
	public void detach() {
		Detachables.detach(
				closableModel,
				animationModel,
				htmlModel,
				placementsModel,
				selectorModel,
				triggerModel,
				titleModel,
				contentModel,
				delayModel,
				containerModel,
				cssClassModel
		);
	}

}
