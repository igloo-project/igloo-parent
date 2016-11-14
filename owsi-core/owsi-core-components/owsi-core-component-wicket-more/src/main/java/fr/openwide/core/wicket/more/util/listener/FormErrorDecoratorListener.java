package fr.openwide.core.wicket.more.util.listener;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxRequestTarget.IJavaScriptResponse;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
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
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.wicketstuff.wiquery.core.javascript.JsStatement;
import org.wicketstuff.wiquery.core.javascript.JsUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import fr.openwide.core.wicket.behavior.ClassAttributeAppender;
import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.markup.html.basic.EnclosureContainer;

/**
 * <p>Decorate fields by adding CSS class '.has-error' and traced it back to the related form-group, using Javascript.</p>
 * 
 * <p>The listener must be initialized in the Wicket application configuration: {@code FormErrorDecoratorListener.init(this);}</p>
 */
public final class FormErrorDecoratorListener {
	
	private FormErrorDecoratorListener() { }
	
	public static void init(WebApplication application) {
		application.getComponentPreOnBeforeRenderListeners().add(PRE_ON_BEFORE_RENDER_LISTENER);
		application.getComponentPostOnBeforeRenderListeners().add(POST_ON_BEFORE_RENDER_LISTENER);
		application.getComponentOnAfterRenderListeners().add(ON_AFTER_RENDER_LISTENER);
		application.getAjaxRequestTargetListeners().add(AJAX_LISTENER);
		FormProcessedListener.init(application);
	}
	
	private static final String HAS_ERROR_CSS_CLASS = "has-error";
	private static final String HAS_ERROR_REMINDER_CSS_CLASS = "has-error-reminder";

	private static final Behavior HAS_ERROR_BEHAVIOR = new ClassAttributeAppender(HAS_ERROR_CSS_CLASS) {
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
		private Object readResolve() {
			return HAS_ERROR_BEHAVIOR;
		}
	};
	
	private static final Behavior PROPAGATE_HAS_ERROR_BEHAVIOR = new Behavior() {
		private static final long serialVersionUID = -7997289335427913596L;
		@Override
		public boolean isTemporary(Component component) {
			/*
			 * See HAS_ERROR_BEHAVIOR.isTemporary.
			 */
			return true;
		}
		@Override
		public void renderHead(Component component, IHeaderResponse response) {
			component.setOutputMarkupId(true);
			
			StringBuilder sb = new StringBuilder();
			sb
					.append(new JsStatement().$(component, ".form-group, .form-decorator-error-group")
							.removeClass(HAS_ERROR_CSS_CLASS)
							.render()
					)
					.append(new JsStatement().$(component, ".has-error")
							.chain("parentsUntil", JsUtils.quotes("#" + component.getMarkupId()), JsUtils.quotes(".form-group, .form-decorator-error-group"))
							.addClass(HAS_ERROR_CSS_CLASS)
							.render()
					)
					// Les .form-decorator-error-reminder doivent prendre une apparence particulière en cas d'erreur,
					// mais sans impacter l'apparence des sous-éléments comme le ferait un .has-error
					.append(new JsStatement().$(component, ".form-decorator-error-reminder")
							.removeClass(HAS_ERROR_REMINDER_CSS_CLASS)
							.render()
					)
					.append(new JsStatement().$(component, "." + HAS_ERROR_CSS_CLASS)
							.chain("parentsUntil", JsUtils.quotes("#" + component.getMarkupId()), JsUtils.quotes(".form-decorator-error-reminder"))
							.addClass(HAS_ERROR_REMINDER_CSS_CLASS)
							.render()
					);
			response.render(OnDomReadyHeaderItem.forScript(sb.toString()));
		}
	};
	
	private static final MetaDataKey<Serializable> HAS_ERROR = new MetaDataKey<Serializable>() {
		private static final long serialVersionUID = 1L;
		private Object readResolve() {
			return HAS_ERROR;
		}
	};

	private static final MetaDataKey<Serializable> FORM_GROUP = new MetaDataKey<Serializable>() {
		private static final long serialVersionUID = 1L;
		private Object readResolve() {
			return FORM_GROUP;
		}
	};

	/*
	 * First pass: make sure we won't lose information due to some RefreshingView weirdness.
	 * 
	 * RefreshingView triggers a detach on its children (and grandchildren and so on) when it
	 * executes onBeforeRender (because it calls onPopulate, which removes all children, which detaches
	 * each child).
	 * Since detaching a child very likely will remove temporary behaviors (such as HAS_ERROR_BEHAVIOR)
	 * and will reset error messages (which will make FormComponents valid again), we have to intervene
	 * before these detaches and save the information that some components are errored. 
	 */
	private static final IComponentOnBeforeRenderListener PRE_ON_BEFORE_RENDER_LISTENER =
			new IComponentOnBeforeRenderListener() {
				@Override
				public void onBeforeRender(Component component) {
					if (component instanceof RefreshingView<?>) {
						RefreshingView<?> form = (RefreshingView<?>)component;
						form.visitChildren(FormComponent.class, new IVisitor<FormComponent<?>, Void>() {
							@Override
							public void component(FormComponent<?> formComponent, IVisit<Void> visit) {
								if (hasError(formComponent)) {
									formComponent.setMetaData(HAS_ERROR, HAS_ERROR);
								}
							}
						});
					}
				}
			};

		/*
		 * Second pass: add the HAS_ERROR_BEHAVIOR.
		 * 
		 * Here we are sure that, even if the components to decorate are nested deep in the current component's
		 * child hierarchy, and even if these components to decorate have a parent RefreshingView (see above),
		 * those RefreshingViews already have been populated and thus will not trigger a detach that would remove
		 * temporary beavhiors.
		 */
	private static final IComponentOnBeforeRenderListener POST_ON_BEFORE_RENDER_LISTENER =
			new IComponentOnBeforeRenderListener() {
				@Override
				public void onBeforeRender(Component component) {
					if (component instanceof FormComponent) {
						FormComponent<?> formComponent = (FormComponent<?>)component;
						if (hasError(formComponent)) {
							formComponent.setMetaData(HAS_ERROR, null);
							for (Component componentToMarkWithError : getComponentsToDecorateWithCSS(formComponent)) {
								if (!componentToMarkWithError.getBehaviors().contains(HAS_ERROR_BEHAVIOR)) {
									componentToMarkWithError.add(HAS_ERROR_BEHAVIOR);
								}
							}
						}
					}
				}
			};

	private static final IComponentOnAfterRenderListener ON_AFTER_RENDER_LISTENER =
			new IComponentOnAfterRenderListener() {
				@Override
				public void onAfterRender(Component component) {
					if (component instanceof RefreshingView<?>) {
						// Nettoyage des metadonnées ajoutées dans onBeforeRender
						RefreshingView<?> form = (RefreshingView<?>)component;
						form.visitChildren(FormComponent.class, new IVisitor<FormComponent<?>, Void>() {
							@Override
							public void component(FormComponent<?> formComponent, IVisit<Void> visit) {
								formComponent.setMetaData(HAS_ERROR, null);
							}
						});
					}
				}
			};

	private static final AjaxRequestTarget.IListener AJAX_LISTENER =
			new AjaxRequestTarget.IListener() {
				@Override
				public void updateAjaxAttributes(AbstractDefaultAjaxBehavior behavior, AjaxRequestAttributes attributes) {
					// Rien à faire de particulier.
				}
			
				@Override
				public void onBeforeRespond(Map<String, Component> map, AjaxRequestTarget target) {
					target.getPage().visitChildren(FormComponent.class, new AjaxRenderingVisitor(target));
				}
			
				@Override
				public void onAfterRespond(Map<String, Component> map, IJavaScriptResponse response) {
					// Rien à faire de particulier après la réponse.
				}
			};

	private static final class AjaxRenderingVisitor implements IVisitor<FormComponent<?>, Void> {
		private final AjaxRequestTarget target;
		
		public AjaxRenderingVisitor(AjaxRequestTarget target) {
			this.target = target;
		}
		
		@Override
		public void component(FormComponent<?> formComponent, IVisit<Void> visit) {
			if (hasError(formComponent)) {
				Form<?> formToRefresh = FormProcessedListener.findProcessedForm(formComponent);
				// If the form has not been processed, isValid() always returns false, but
				// in our case we'd like to consider the formComponent as valid.
				if (formToRefresh != null) {
					target.add(formToRefresh);
					if (!formToRefresh.getBehaviors().contains(PROPAGATE_HAS_ERROR_BEHAVIOR)) {
						formToRefresh.add(PROPAGATE_HAS_ERROR_BEHAVIOR);
					}
				}
			}
		}
	}

	private static boolean hasError(FormComponent<?> formComponent) {
		return formComponent.getMetaData(HAS_ERROR) != null || !formComponent.isValid();
	}

	public static void markWithError(FormComponent<?> formComponent) {
		formComponent.setMetaData(HAS_ERROR, HAS_ERROR);
	}

	private static List<? extends Component> getComponentsToDecorateWithCSS(FormComponent<?> formComponent) {
		if (formComponent.getParent().getMetaData(FORM_GROUP) != null) {
			return ImmutableList.of(formComponent.getParent());
		} else if (formComponent instanceof RadioGroup) {
			return collect(formComponent, Radio.class);
		} else if (formComponent instanceof CheckGroup) {
			return collect(formComponent, Check.class);
		} else {
			return ImmutableList.of(formComponent);
		}
	}
	
	private static <T extends Component> List<T> collect(FormComponent<?> formComponent, Class<T> clazz) {
		final List<T> result = Lists.newArrayList();
		formComponent.visitChildren(clazz, new IVisitor<T, Void>() {
			@Override
			public void component(T object, IVisit<Void> visit) {
				result.add(object);
			}
		});
		return result;
	}

	/**
	 * Pour le moment, on traite tout en JavaScript automatiquement donc ce n'est pas la peine de wrapper les form-group.
	 * 
	 * On le laisse quand même là vu qu'on a tout géré correctement pour ce cas et que ça pourra peut-être servir dans
	 * des cas très spécifiques.
	 */
	public static Component wrapFormGroup(FormComponent<?> formComponent) {
		EnclosureContainer formGroup = new EnclosureContainer(formComponent.getId() + "FormGroup");
		formGroup.add(formComponent);
		formGroup.condition(Condition.componentVisible(formComponent));
		formGroup.setMetaData(FORM_GROUP, FORM_GROUP);
		return formGroup;
	}
	
	public static void propagateHasError(Form<?> form) {
		form.add(PROPAGATE_HAS_ERROR_BEHAVIOR);
	}

}