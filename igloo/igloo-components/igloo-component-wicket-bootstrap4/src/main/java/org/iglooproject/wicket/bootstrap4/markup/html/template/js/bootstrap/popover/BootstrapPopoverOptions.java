package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.popover;

import java.util.Collection;
import java.util.Locale;
import java.util.stream.Collectors;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.CollectionModel;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.SimpleOptions;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.popover.IBootstrapPopoverOptions;
import org.iglooproject.wicket.more.util.component.ComponentUtils;
import org.iglooproject.wicket.more.util.model.Detachables;
import org.iglooproject.wicket.more.util.model.Models;
import org.wicketstuff.wiquery.core.javascript.JsScope;
import org.wicketstuff.wiquery.core.javascript.JsScopeContext;
import org.wicketstuff.wiquery.core.javascript.JsStatement;
import org.wicketstuff.wiquery.core.javascript.JsUtils;
import org.wicketstuff.wiquery.core.options.Options;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

public class BootstrapPopoverOptions extends SimpleOptions implements IBootstrapPopoverOptions {

	private static final long serialVersionUID = 680573363463468690L;

	private static final String POPOVER_CSS_CLASS_POPOVER_MODAL = "popover-modal";

	// Igloo options
	private IModel<Boolean> closableModel = Condition.alwaysTrue();

	// Bootstrap popover options
	private IModel<Boolean> animationModel;

	private IModel<String> containerModel;

	private Component containerComponent;

	private IModel<String> contentModel;

	private Component contentComponent;
	
	private JsScope contentFunction;

	private IModel<Integer> delayShowModel;

	private IModel<Integer> delayHideModel;

	private IModel<Boolean> htmlModel;

	private IModel<? extends Collection<? extends Placement>> placementModel;

	private IModel<String> selectorModel;

	private IModel<String> titleModel;

	private Component titleComponent;

	private JsScope titleFunction;

	private IModel<? extends Collection<? extends Trigger>> triggerModel;

	private IModel<String> offsetModel;

	private IModel<? extends Collection<String>> customClassModel;

	private IModel<String> boundaryModel;

	public static final BootstrapPopoverOptions get() {
		return new BootstrapPopoverOptions()
			.closable(true)
			.animation(true)
			.html(true)
			.placement(Placement.AUTO)
			.trigger(Trigger.CLICK);
	}

	public BootstrapPopoverOptions() {
		super();
		boundary("window");
	}

	public IModel<Boolean> getClosableModel() {
		return closableModel;
	}

	public BootstrapPopoverOptions closable(IModel<Boolean> closableModel) {
		this.closableModel = closableModel;
		return this;
	}

	public BootstrapPopoverOptions closable(Boolean closable) {
		return closable(Model.of(closable));
	}

	public IModel<Boolean> getAnimationModel() {
		return animationModel;
	}

	public BootstrapPopoverOptions animation(IModel<Boolean> animationModel) {
		this.animationModel = animationModel;
		return this;
	}

	public BootstrapPopoverOptions animation(Boolean animation) {
		return animation(Model.of(animation));
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

	public Component getContainerComponent() {
		return containerComponent;
	}

	public BootstrapPopoverOptions container(Component containerComponent) {
		this.containerComponent = containerComponent;
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

	public IModel<Integer> getDelayShowModel() {
		return delayShowModel;
	}

	public BootstrapPopoverOptions delayShow(IModel<Integer> delayShowModel) {
		this.delayShowModel = delayShowModel;
		return this;
	}

	public BootstrapPopoverOptions delayShow(Integer delayShow) {
		return delayShow(Model.of(delayShow));
	}

	public IModel<Integer> getDelayHideModel() {
		return delayHideModel;
	}

	public BootstrapPopoverOptions delayHide(IModel<Integer> delayHideModel) {
		this.delayHideModel = delayHideModel;
		return this;
	}

	public BootstrapPopoverOptions delayHide(Integer delayHide) {
		return delayHide(Model.of(delayHide));
	}

	public BootstrapPopoverOptions delay(IModel<Integer> delayModel) {
		delayShow(delayModel);
		delayHide(delayModel);
		return this;
	}

	public BootstrapPopoverOptions delay(Integer delay) {
		return delay(Model.of(delay));
	}

	public IModel<Boolean> getHtmlModel() {
		return htmlModel;
	}

	public BootstrapPopoverOptions html(IModel<Boolean> htmlModel) {
		this.htmlModel = htmlModel;
		return this;
	}

	public BootstrapPopoverOptions html(Boolean html) {
		return html(Model.of(html));
	}

	public IModel<? extends Collection<? extends Placement>> getPlacementModel() {
		return placementModel;
	}

	public BootstrapPopoverOptions placement(IModel<? extends Collection<? extends Placement>> placementModel) {
		this.placementModel = placementModel;
		return this;
	}

	public BootstrapPopoverOptions placement(Collection<Placement> placement) {
		return placement(new CollectionModel<Placement>(placement));
	}

	public BootstrapPopoverOptions placement(Placement firstPlacement, Placement ... otherPlacements) {
		return placement(Lists.asList(firstPlacement, otherPlacements));
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

	public IModel<? extends Collection<? extends Trigger>> getTriggerModel() {
		return triggerModel;
	}

	public BootstrapPopoverOptions trigger(IModel<? extends Collection<? extends Trigger>> triggerModel) {
		this.triggerModel = triggerModel;
		return this;
	}

	public BootstrapPopoverOptions trigger(Collection<Trigger> trigger) {
		return trigger(new CollectionModel<Trigger>(trigger));
	}

	public BootstrapPopoverOptions trigger(Trigger firstTrigger, Trigger ... otherTriggers) {
		return trigger(Lists.asList(firstTrigger, otherTriggers));
	}

	public IModel<String> getOffsetModel() {
		return offsetModel;
	}

	public BootstrapPopoverOptions offset(IModel<String> offsetModel) {
		this.offsetModel = offsetModel;
		return this;
	}

	public BootstrapPopoverOptions offset(String offset) {
		return offset(Model.of(offset));
	}

	public BootstrapPopoverOptions offsetNumber(IModel<Integer> offsetNumberModel) {
		if (offsetNumberModel == null || offsetNumberModel.getObject() == null) {
			return offset((IModel<String>) null);
		}
		return offset(offsetNumberModel.map(offsetNumber -> offsetNumber + "px"));
	}

	public BootstrapPopoverOptions offsetNumber(Integer offsetNumber) {
		return offsetNumber(Model.of(offsetNumber));
	}

	public IModel<? extends Collection<String>> getCustomClassModel() {
		return customClassModel;
	}

	public BootstrapPopoverOptions customClass(IModel<? extends Collection<String>> customClassModel) {
		this.customClassModel = customClassModel;
		return this;
	}

	public BootstrapPopoverOptions customClass(Collection<String> customClass) {
		return customClass(new CollectionModel<>(customClass));
	}

	public BootstrapPopoverOptions customClass(String firstCustomClass, String ... otherCustomClass) {
		return customClass(Lists.asList(firstCustomClass, otherCustomClass));
	}

	public IModel<String> getBoundaryModel() {
		return boundaryModel;
	}

	public BootstrapPopoverOptions boundary(IModel<String> boundaryModel) {
		this.boundaryModel = boundaryModel;
		return this;
	}

	public BootstrapPopoverOptions boundary(String boundary) {
		return boundary(Model.of(boundary));
	}

	@Override
	public CharSequence getJavaScriptOptions(Component component) {
		ImmutableMap.Builder<String, Object> options = ImmutableMap.builder();
		
		Boolean animation = Models.getObject(animationModel);
		if (animation != null) {
			options.put("animation", animation);
		}
		
		String container = Models.getObject(containerModel);
		if (container != null) {
			options.put("container", JsUtils.quotes(container));
		}
		
		if (containerComponent != null) {
			options.put("container", JsUtils.quotes(containerComponent.getMarkupId()));
		}
		
		String content = Models.getObject(contentModel);
		if (content != null) {
			options.put("content", JsUtils.quotes(content, true));
		}
		
		if (contentComponent != null) {
			options.put("content", new JsScope() {
				private static final long serialVersionUID = 1L;
				@Override
				protected void execute(JsScopeContext scopeContext) {
					scopeContext.append("return " + new JsStatement().$(contentComponent).chain("html").render());
				}
			}.render().toString());
		}
		
		if (contentFunction != null) {
			options.put("content", contentFunction.render().toString());
		}
		
		Integer delayShow = Models.getObject(delayShowModel);
		Integer delayHide = Models.getObject(delayHideModel);
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
		
		Boolean html = Models.getObject(htmlModel);
		if (html != null) {
			options.put("html", html);
		}
		
		Collection<? extends Placement> placement = Models.getObject(placementModel);
		if (placement != null && !placement.isEmpty()) {
			options.put("placement", JsUtils.quotes(placement.stream().filter(Predicates2.notNull()).map(Placement::getValue).collect(Collectors.joining(" "))));
		}
		
		String selector = Models.getObject(selectorModel);
		if (selector != null) {
			options.put("selector", JsUtils.quotes(selector));
		}
		
		String title = Models.getObject(titleModel);
		if (title != null) {
			options.put("title", getTitleFunction(component, new JsStatement().append(JsUtils.quotes(title, true))));
		}
		
		if (titleComponent != null) {
			options.put("title", getTitleFunction(component, new JsStatement().$(titleComponent).chain("html")));
		}
		
		if (titleFunction != null) {
			options.put("title", getTitleFunction(component, new JsStatement().append(titleFunction.render().toString())));
		}
		
		Collection<? extends Trigger> trigger = Models.getObject(triggerModel);
		if (trigger != null && !trigger.isEmpty()) {
			options.put("trigger", JsUtils.quotes(trigger.stream().filter(Predicates2.notNull()).map(Trigger::getValue).collect(Collectors.joining(" "))));
		}
		
		String offset = Models.getObject(offsetModel);
		if (offset != null) {
			options.put("offset", JsUtils.quotes(offset));
		}
		
		Collection<String> customClass = MoreObjects.firstNonNull(Models.getObject(customClassModel), Lists.newArrayList());
		
		if (ComponentUtils.anyParentModal(component)) {
			customClass.add(POPOVER_CSS_CLASS_POPOVER_MODAL);
		}
		
		String customClassAsString = customClass.stream()
			.filter(Predicates2.notNull())
			.collect(Collectors.joining(" "));
		
		if (StringUtils.hasText(customClassAsString)) {
			options.put("customClass", JsUtils.quotes(customClassAsString));
		}
		
		String boundary = Models.getObject(boundaryModel);
		if (boundary != null) {
			options.put("boundary", JsUtils.quotes(boundary));
		}
		
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
							.append(JsUtils.quotes("<a href=\"#\" class=\"popover-dismiss close\">&times;</a>", true))
							.append(");");
				}
				scopeContext.append("return titleHtml;");
			}
		};
	}

	public enum Placement implements IBootstrapPopoverOptions.IPlacement {
		
		AUTO("auto"),
		TOP("top"),
		BOTTOM("bottom"),
		LEFT("left"),
		RIGHT("right");
		
		private String value;
		
		private Placement(String value) {
			this.value = value;
		}
		
		@Override
		public String getValue() {
			return value;
		}
		
		private static String normalize(String string) {
			if (string == null) {
				return null;
			}
			return string.toLowerCase(Locale.ROOT);
		}
		
		public static Placement fromString(String value) {
			for (Placement type : values()) {
				if (type.getValue().equals(normalize(value))) {
					return type;
				}
			}
			return null;
		}
	}

	public enum Trigger implements IBootstrapPopoverOptions.ITrigger {
		
		CLICK("click"),
		HOVER("hover"),
		FOCUS("focus"),
		MANUAL("manual");
		
		private String value;
		
		private Trigger(String value) {
			this.value = value;
		}
		
		@Override
		public String getValue() {
			return value;
		}
		
		private static String normalize(String string) {
			if (string == null) {
				return null;
			}
			return string.toLowerCase(Locale.ROOT);
		}
		
		public static Trigger fromString(String value) {
			for (Trigger type : values()) {
				if (type.getValue().equals(normalize(value))) {
					return type;
				}
			}
			return null;
		}
	}

	@Override
	public void detach() {
		Detachables.detach(
			closableModel,
			animationModel,
			containerModel,
			contentModel,
			delayShowModel,
			delayHideModel,
			htmlModel,
			placementModel,
			selectorModel,
			titleModel,
			triggerModel,
			offsetModel,
			customClassModel,
			boundaryModel
		);
	}

}
