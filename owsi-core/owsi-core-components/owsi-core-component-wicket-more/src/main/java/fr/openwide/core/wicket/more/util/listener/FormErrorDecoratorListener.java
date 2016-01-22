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
import fr.openwide.core.wicket.more.markup.html.basic.EnclosureContainer;

/**
 * <p>Decorate fields by adding CSS class '.has-error' and traced it back to the related form-group, using Javascript.</p>
 * 
 * <p>The listener must be initialized in the Wicket application configuration: {@code FormErrorDecoratorListener.init(this);}</p>
 */
public final class FormErrorDecoratorListener
		implements IComponentOnBeforeRenderListener, IComponentOnAfterRenderListener, AjaxRequestTarget.IListener {
	
	private static final FormErrorDecoratorListener INSTANCE = new FormErrorDecoratorListener();
	
	private static final String HAS_ERROR_CSS_CLASS = "has-error";
	private static final String HAS_ERROR_REMINDER_CSS_CLASS = "has-error-reminder";

	private static final Behavior HAS_ERROR_BEHAVIOR = new ClassAttributeAppender(HAS_ERROR_CSS_CLASS) {
		private static final long serialVersionUID = 1L;
		@Override
		public boolean isTemporary(Component component) {
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
	
	public static void init(WebApplication application) {
		application.getComponentPreOnBeforeRenderListeners().add(INSTANCE);
		application.getComponentOnAfterRenderListeners().add(INSTANCE);
		application.getAjaxRequestTargetListeners().add(INSTANCE);
		FormProcessedListener.init(application);
	}
	
	private FormErrorDecoratorListener() { }

	@Override
	public void onBeforeRender(Component component) {
		if (component instanceof FormComponent) {
			FormComponent<?> formComponent = (FormComponent<?>)component;
			if (hasError(formComponent)) {
				formComponent.setMetaData(HAS_ERROR, null);
				markWithError(formComponent);
			}
		} else if (component instanceof RefreshingView<?>) {
			// Dans le cas où une RefreshingView est rendue, on en profite pour marquer tous les FormComponents invalides
			// comme tels, puisque cette information peut être perdue lors du onBeforeRender de la RefreshingView.
			// Explications : lors du onBeforeRender de la RefreshingView, les items sont indirectements détachés (detach()),
			// ce qui a pour conséquence de vider les FeedbackMessages et donc de rendre valides les inputs dans ces items.
			// Or, le onBeforeRender de la RefreshingView a lieu avant même l'appel des IComponentOnBeforeRenderListener,
			// voire même des IComponentOnConfigureListeners, sur les items, ce qui fait qu'il est impossible depuis ces
			// listeners de constater qu'un input dans un item d'une RefreshingView est invalide.
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

	private static class AjaxRenderingVisitor implements IVisitor<FormComponent<?>, Void> {
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
		return !formComponent.isValid() || formComponent.getMetaData(HAS_ERROR) != null;
	}

	public static void markWithError(FormComponent<?> formComponent) {
		for (Component componentToMarkWithError : getComponentsToMarkWithError(formComponent)) {
			if (!componentToMarkWithError.getBehaviors().contains(HAS_ERROR_BEHAVIOR)) {
				componentToMarkWithError.add(HAS_ERROR_BEHAVIOR);
			}
		}
	}

	private static List<? extends Component> getComponentsToMarkWithError(FormComponent<?> formComponent) {
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
		formGroup.component(formComponent);
		formGroup.setMetaData(FORM_GROUP, FORM_GROUP);
		return formGroup;
	}
	
	public static void propagateHasError(Form<?> form) {
		form.add(PROPAGATE_HAS_ERROR_BEHAVIOR);
	}

}