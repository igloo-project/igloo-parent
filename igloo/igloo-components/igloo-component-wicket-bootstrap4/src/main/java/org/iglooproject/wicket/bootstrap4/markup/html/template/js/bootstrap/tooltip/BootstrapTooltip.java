package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.tooltip;

import java.util.Collection;
import java.util.stream.Collectors;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.CollectionModel;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.wicket.more.util.model.Detachables;
import org.iglooproject.wicket.more.util.model.Models;
import org.wicketstuff.wiquery.core.javascript.JsScope;
import org.wicketstuff.wiquery.core.javascript.JsUtils;
import org.wicketstuff.wiquery.core.options.Options;

import com.google.common.collect.Lists;

public class BootstrapTooltip implements IBootstrapTooltip {

	private static final long serialVersionUID = -2571067418057286728L;

	private IModel<Boolean> animationModel;

	private IModel<String> containerModel;

	private IModel<Integer> delayShowModel;

	private IModel<Integer> delayHideModel;

	private IModel<Boolean> htmlModel;

	private IModel<Placement> placementModel;

	private JsScope placementFunction;

	private IModel<String> selectorModel;

	private IModel<String> templateModel;

	private IModel<String> titleModel;

	private JsScope titleFunction;

	private IModel<? extends Collection<? extends Trigger>> triggerModel;

	private IModel<String> offsetModel;

	private IModel<String> boundaryModel;

	public BootstrapTooltip() {
		super();
		boundary("window"); // https://github.com/FezVrasta/popper.js/issues/611
	}

	public IModel<Boolean> getAnimationModel() {
		return animationModel;
	}

	public BootstrapTooltip animation(IModel<Boolean> animationModel) {
		this.animationModel = animationModel;
		return this;
	}

	public BootstrapTooltip animation(Boolean animation) {
		return animation(Model.of(animation));
	}

	public IModel<String> getContainerModel() {
		return containerModel;
	}

	public BootstrapTooltip container(IModel<String> containerModel) {
		this.containerModel = containerModel;
		return this;
	}

	public BootstrapTooltip container(String container) {
		return container(Model.of(container));
	}

	public IModel<Integer> getDelayShowModel() {
		return delayShowModel;
	}

	public BootstrapTooltip delayShow(IModel<Integer> delayShowModel) {
		this.delayShowModel = delayShowModel;
		return this;
	}

	public BootstrapTooltip delayShow(Integer delayShow) {
		return delayShow(Model.of(delayShow));
	}

	public IModel<Integer> getDelayHideModel() {
		return delayHideModel;
	}

	public BootstrapTooltip delayHide(IModel<Integer> delayHideModel) {
		this.delayHideModel = delayHideModel;
		return this;
	}

	public BootstrapTooltip delayHide(Integer delayHide) {
		return delayHide(Model.of(delayHide));
	}

	public BootstrapTooltip delay(IModel<Integer> delayModel) {
		delayShow(delayModel);
		delayHide(delayModel);
		return this;
	}

	public BootstrapTooltip delay(Integer delay) {
		return delay(Model.of(delay));
	}

	public IModel<Boolean> getHtml() {
		return htmlModel;
	}

	public BootstrapTooltip html(IModel<Boolean> htmlModel) {
		this.htmlModel = htmlModel;
		return this;
	}

	public BootstrapTooltip html(Boolean html) {
		return html(Model.of(html));
	}

	public IModel<Placement> getPlacementModel() {
		return placementModel;
	}

	public BootstrapTooltip placement(IModel<Placement> placementModel) {
		this.placementModel = placementModel;
		return this;
	}

	public BootstrapTooltip placement(Placement placement) {
		return placement(Model.of(placement));
	}

	public JsScope getPlacementFunction() {
		return placementFunction;
	}

	public BootstrapTooltip placement(JsScope placementFunction) {
		this.placementFunction = placementFunction;
		return this;
	}

	@Override
	public IModel<String> getSelectorModel() {
		return selectorModel;
	}

	public BootstrapTooltip selector(IModel<String> selectorModel) {
		this.selectorModel = selectorModel;
		return this;
	}

	public BootstrapTooltip selector(String selector) {
		return selector(Model.of(selector));
	}

	public IModel<String> getTemplateModel() {
		return templateModel;
	}

	public BootstrapTooltip template(IModel<String> templateModel) {
		this.templateModel = templateModel;
		return this;
	}

	public BootstrapTooltip template(String template) {
		return template(Model.of(template));
	}

	public IModel<String> getTitleModel() {
		return titleModel;
	}

	public BootstrapTooltip title(IModel<String> titleModel) {
		this.titleModel = titleModel;
		return this;
	}

	public BootstrapTooltip title(String title) {
		return title(Model.of(title));
	}

	public JsScope getTitleFunction() {
		return titleFunction;
	}

	public BootstrapTooltip title(JsScope titleFunction) {
		this.titleFunction = titleFunction;
		return this;
	}

	public IModel<? extends Collection<? extends Trigger>> getTriggerModel() {
		return triggerModel;
	}

	public BootstrapTooltip trigger(IModel<? extends Collection<? extends Trigger>> triggerModel) {
		this.triggerModel = triggerModel;
		return this;
	}

	public BootstrapTooltip trigger(Collection<Trigger> trigger) {
		return trigger(new CollectionModel<Trigger>(trigger));
	}

	public BootstrapTooltip trigger(Trigger firstTrigger, Trigger ... otherTriggers) {
		return trigger(Lists.asList(firstTrigger, otherTriggers));
	}

	public IModel<String> getOffsetModel() {
		return offsetModel;
	}

	public BootstrapTooltip offset(IModel<String> offsetModel) {
		this.offsetModel = offsetModel;
		return this;
	}

	public BootstrapTooltip offset(String offset) {
		return offset(Model.of(offset));
	}

	public BootstrapTooltip offsetNumber(IModel<Integer> offsetNumberModel) {
		if (offsetNumberModel == null || offsetNumberModel.getObject() == null) {
			return offset((IModel<String>) null);
		}
		return offset(offsetNumberModel.map(offsetNumber -> offsetNumber + "px"));
	}

	public BootstrapTooltip offsetNumber(Integer offsetNumber) {
		return offsetNumber(Model.of(offsetNumber));
	}

	public IModel<String> getBoundaryModel() {
		return boundaryModel;
	}

	public BootstrapTooltip boundary(IModel<String> boundaryModel) {
		this.boundaryModel = boundaryModel;
		return this;
	}

	public BootstrapTooltip boundary(String boundary) {
		return boundary(Model.of(boundary));
	}

	@Override
	public CharSequence[] statementArgs() {
		Options options = new Options();
		
		Boolean animation = Models.getObject(animationModel);
		if (animation != null) {
			options.put("animation", animation);
		}
		
		String container = Models.getObject(containerModel);
		if (container != null) {
			options.put("container", JsUtils.quotes(container));
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
		
		Placement placement = Models.getObject(placementModel);
		if (placement != null) {
			options.put("placement", JsUtils.quotes(placement.getValue()));
		}
		if (placementFunction != null) {
			options.put("placement", placementFunction);
		}
		
		String selector = Models.getObject(selectorModel);
		if (selector != null) {
			options.put("selector", JsUtils.quotes(selector));
		}
		
		String template = Models.getObject(templateModel);
		if (template != null) {
			options.put("template", JsUtils.quotes(template));
		}
		
		String title = Models.getObject(titleModel);
		if (title != null) {
			options.put("title", JsUtils.quotes(title));
		}
		
		if (titleFunction != null) {
			options.put("title", titleFunction);
		}
		
		Collection<? extends Trigger> trigger = Models.getObject(triggerModel);
		if (trigger != null && !trigger.isEmpty()) {
			options.put("trigger", JsUtils.quotes(trigger.stream().filter(Predicates2.notNull()).map(Trigger::getValue).collect(Collectors.joining(" "))));
		}
		
		String offset = Models.getObject(offsetModel);
		if (offset != null) {
			options.put("offset", JsUtils.quotes(offset));
		}
		
		String boundary = Models.getObject(boundaryModel);
		if (boundary != null) {
			options.put("boundary", JsUtils.quotes(boundary));
		}
		
		return new CharSequence[] { options.getJavaScriptOptions() };
	}

	public enum Placement implements IBootstrapTooltip.IPlacement {
		AUTO("auto"),
		TOP("top"),
		BOTTOM("bottom"),
		LEFT("left"),
		RIGHT("right");
		
		private final String value;
		
		private Placement(String value) {
			this.value = value;
		}
		
		@Override
		public String getValue() {
			return value;
		}
	}

	public enum Trigger implements IBootstrapTooltip.ITrigger {
		CLICK("click"),
		HOVER("hover"),
		FOCUS("focus"),
		MANUAL("manual");
		
		private final String value;
		
		private Trigger(String value) {
			this.value = value;
		}
		
		@Override
		public String getValue() {
			return value;
		}
	}

	@Override
	public void detach() {
		IBootstrapTooltip.super.detach();
		Detachables.detach(
			animationModel,
			containerModel,
			delayShowModel,
			delayHideModel,
			htmlModel,
			placementModel,
			selectorModel,
			templateModel,
			titleModel,
			triggerModel,
			offsetModel,
			boundaryModel
		);
	}

}
