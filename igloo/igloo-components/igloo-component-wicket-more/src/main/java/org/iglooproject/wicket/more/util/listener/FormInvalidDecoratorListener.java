package org.iglooproject.wicket.more.util.listener;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.application.IComponentOnAfterRenderListener;
import org.apache.wicket.application.IComponentOnBeforeRenderListener;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.visit.IVisitor;
import org.iglooproject.wicket.behavior.ClassAttributeAppender;
import org.iglooproject.wicket.component.EnclosureContainer;
import org.iglooproject.wicket.condition.Condition;
import org.wicketstuff.wiquery.core.javascript.JsStatement;
import org.wicketstuff.wiquery.core.javascript.JsUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

public final class FormInvalidDecoratorListener {

	private FormInvalidDecoratorListener() { }

	public static void init(WebApplication application) {
		application.getComponentPreOnBeforeRenderListeners().add(PRE_ON_BEFORE_RENDER_LISTENER);
		application.getComponentPostOnBeforeRenderListeners().add(POST_ON_BEFORE_RENDER_LISTENER);
		application.getComponentOnAfterRenderListeners().add(ON_AFTER_RENDER_LISTENER);
		// TODO FLA : supprimer ce listener ajax ?
		application.getAjaxRequestTargetListeners().add(AJAX_LISTENER);
		FormProcessedListener.init(application);
	}

	private static final String IS_INVALID_CSS_CLASS = "is-invalid";
	private static final String IS_INVALID_REMINDER_CSS_CLASS = "is-invalid-reminder";

	private static final Behavior IS_INVALID_BEHAVIOR = new ClassAttributeAppender(IS_INVALID_CSS_CLASS) {
		private static final long serialVersionUID = 1L;
		@Override
		public boolean isTemporary(Component component) {
			/*
			 * Don't use this Wicket feature.
			 * "Temporary" behaviors are removed upon detach, and components within a
			 * RefreshingView are detached as part of the rendering process (when the view's items are all removed,
			 * then re-added).
			 * Thus "temporary" behaviors are never executed for components within a RefreshingView.
			 * Thus we use a IComponentOnAfterRenderListener for cleaning up this behavior.
			 */
			return true;
		}
		protected Object readResolve() {
			return IS_INVALID_BEHAVIOR;
		}
	};
	
	private static final Behavior PROPAGATE_IS_INVALID_BEHAVIOR = new Behavior() {
		private static final long serialVersionUID = 1L;
		@Override
		public boolean isTemporary(Component component) {
			/*
			 * See IS_INVALID_BEHAVIOR.isTemporary.
			 */
			return true;
		}
		@Override
		public void renderHead(Component component, IHeaderResponse response) {
			component.setOutputMarkupId(true);
			StringBuilder sb = new StringBuilder();
			sb
				.append(
					new JsStatement().$(component, ".form-group, .form-floating, .form-decorator-invalid-group, .select2")
						.removeClass(IS_INVALID_CSS_CLASS)
						.render()
				)
				.append(
					new JsStatement().$(component,  "." + IS_INVALID_CSS_CLASS)
						.chain("parentsUntil", JsUtils.quotes("#" + component.getMarkupId()), JsUtils.quotes(".form-group, .form-floating, .form-decorator-invalid-group"))
						.addClass(IS_INVALID_CSS_CLASS)
						.render()
				)
				.append(
					new JsStatement().$(component,  "." + IS_INVALID_CSS_CLASS)
						.chain("next", JsUtils.quotes(".select2"))
						.addClass(IS_INVALID_CSS_CLASS)
						.render()
				)
				// Les .form-decorator-invalid-reminder doivent prendre une apparence particulière en cas d'erreur,
				// mais sans impacter l'apparence des sous-éléments comme le ferait un .is-invalid
				.append(
					new JsStatement().$(component, ".form-decorator-invalid-reminder")
						.removeClass(IS_INVALID_REMINDER_CSS_CLASS)
						.render()
				)
				.append(
					new JsStatement().$(component, "." + IS_INVALID_CSS_CLASS)
						.chain("parentsUntil", JsUtils.quotes("#" + component.getMarkupId()), JsUtils.quotes(".form-decorator-invalid-reminder"))
						.addClass(IS_INVALID_REMINDER_CSS_CLASS)
						.render()
				);
			response.render(OnDomReadyHeaderItem.forScript(sb.toString()));
		}
	};
	
	private static final MetaDataKey<Serializable> IS_INVALID = new MetaDataKey<Serializable>() {
		private static final long serialVersionUID = 1L;
		protected Object readResolve() {
			return IS_INVALID;
		}
	};

	private static final MetaDataKey<Serializable> FORM_GROUP = new MetaDataKey<Serializable>() {
		private static final long serialVersionUID = 1L;
		protected Object readResolve() {
			return FORM_GROUP;
		}
	};

	/*
	 * First pass: make sure we won't lose information due to some RefreshingView weirdness.
	 * 
	 * RefreshingView triggers a detach on its children (and grandchildren and so on) when it
	 * executes onBeforeRender (because it calls onPopulate, which removes all children, which detaches
	 * each child).
	 * Since detaching a child very likely will remove temporary behaviors (such as IS_INVALID_BEHAVIOR)
	 * and will reset error messages (which will make FormComponents valid again), we have to intervene
	 * before these detaches and save the information that some components are errored. 
	 */
	private static final IComponentOnBeforeRenderListener PRE_ON_BEFORE_RENDER_LISTENER = component -> {
		if (!(component instanceof RefreshingView<?>)) {
			return;
		}
		
		RefreshingView<?> refreshingView = (RefreshingView<?>) component;
		refreshingView.visitChildren(FormComponent.class, (IVisitor<FormComponent<?>, Void>) (formComponent, visit) -> {
			if (!formComponent.isValid()) {
				markIsInvalid(formComponent);
			}
		});
	};

	/*
	 * Second pass: add the IS_INVALID_BEHAVIOR.
	 * 
	 * Here we are sure that, even if the components to decorate are nested deep in the current component's
	 * child hierarchy, and even if these components to decorate have a parent RefreshingView (see above),
	 * those RefreshingViews already have been populated and thus will not trigger a detach that would remove
	 * temporary beavhiors.
	 */
	private static final IComponentOnBeforeRenderListener POST_ON_BEFORE_RENDER_LISTENER = component -> {
		if (!(component instanceof FormComponent)) {
			return;
		}
		
		FormComponent<?> formComponent = (FormComponent<?>)component;
		
		if (!isInvalid(formComponent)) {
			return;
		}
		
		for (Component componentToMarkInvalid: getComponentsToDecorateWithCSS(formComponent)) {
			if (!componentToMarkInvalid.getBehaviors().contains(IS_INVALID_BEHAVIOR)) {
				componentToMarkInvalid.add(IS_INVALID_BEHAVIOR);
			}
		}
		
		Form<?> form = FormProcessedListener.findProcessedForm(formComponent);
		
		if (!form.getBehaviors().contains(PROPAGATE_IS_INVALID_BEHAVIOR)) {
			form.add(PROPAGATE_IS_INVALID_BEHAVIOR);
		}
	};

	private static final IComponentOnAfterRenderListener ON_AFTER_RENDER_LISTENER = component -> {
		if (!(component instanceof FormComponent)) {
			return;
		}
		
		FormComponent<?> formComponent = (FormComponent<?>) component;
		
		unmarkIsInvalid(formComponent);
	};

	private static final AjaxRequestTarget.IListener AJAX_LISTENER = new AjaxRequestTarget.IListener() {
		@Override
		public void onBeforeRespond(Map<String, Component> map, AjaxRequestTarget target) {
			target.getPage().visitChildren(FormComponent.class, (IVisitor<FormComponent<?>, Void>) (formComponent, visit) -> {
				if (!isInvalid(formComponent)) {
					return;
				}
				
				Form<?> formToRefresh = FormProcessedListener.findProcessedForm(formComponent);
				if (formToRefresh != null) {
					target.add(formToRefresh);
				}
			});
		}
	};

	private static List<? extends Component> getComponentsToDecorateWithCSS(FormComponent<?> formComponent) {
		if (formComponent.getParent().getMetaData(FORM_GROUP) != null) {
			return List.of(formComponent.getParent());
		} else if (formComponent instanceof RadioGroup) {
			return collect(formComponent, Radio.class);
		} else if (formComponent instanceof CheckGroup) {
			return collect(formComponent, Check.class);
		} else {
			return List.of(formComponent);
		}
	}
	
	private static <T extends Component> List<T> collect(FormComponent<?> formComponent, Class<T> clazz) {
		Builder<T> resultBuilder = ImmutableList.builder();
		formComponent.visitChildren(clazz, (IVisitor<T, Void>) (object, visit) -> resultBuilder.add(object));
		return resultBuilder.build();
	}

	public static boolean isInvalid(FormComponent<?> formComponent) {
		return formComponent.getMetaData(IS_INVALID) != null || !formComponent.isValid();
	}

	public static void markIsInvalid(FormComponent<?> formComponent) {
		formComponent.setMetaData(IS_INVALID, IS_INVALID);
	}

	public static void unmarkIsInvalid(FormComponent<?> formComponent) {
		formComponent.setMetaData(IS_INVALID, null);
	}

	public static void propagateIsInvalid(Form<?> form) {
		form.add(PROPAGATE_IS_INVALID_BEHAVIOR);
	}

	public static Component wrapFormGroup(FormComponent<?> formComponent) {
		return new EnclosureContainer(formComponent.getId() + "FormGroup")
			.condition(Condition.componentVisible(formComponent))
			.add(formComponent)
			.setMetaData(FORM_GROUP, FORM_GROUP);
	}

}
