package org.iglooproject.wicket.more.markup.html.template.js.bootstrap.confirm.component;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.confirm.IBootstrapConfirmModule;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.confirm.behavior.ConfirmContentBehavior;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.confirm.fluid.IConfirmLinkBuilderStepStart;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.confirm.statement.BootstrapConfirmStatement;
import org.wicketstuff.wiquery.core.events.Event;
import org.wicketstuff.wiquery.core.events.MouseEvent;
import org.wicketstuff.wiquery.core.events.WiQueryEventBehavior;
import org.wicketstuff.wiquery.core.javascript.JsScope;
import org.wicketstuff.wiquery.core.javascript.JsScopeEvent;

import com.google.common.collect.Lists;

public abstract class AjaxConfirmLink<O> extends AjaxLink<O> {

	private static final long serialVersionUID = -645345859108195615L;

	@SpringBean(required = false)
	private List<IBootstrapConfirmModule> modules = Lists.newArrayList();

	public static <O> IConfirmLinkBuilderStepStart<AjaxConfirmLink<O>, O> build() {
		return new AjaxConfirmLinkBuilder<>();
	}

	private final Form<?> form;

	protected AjaxConfirmLink(String id, IModel<O> model, IModel<String> titleModel, IModel<String> textModel,
			IModel<String> yesLabelModel, IModel<String> noLabelModel, IModel<String> cssClassNamesModel,
			boolean textNoEscape) {
		this(
			id,
			model,
			null,
			titleModel,
			textModel,
			yesLabelModel,
			noLabelModel,
			new Model<String>("fa fa-fw fa-check"),
			new Model<String>("fa fa-fw fa-ban"),
			new Model<String>("btn btn-success"),
			new Model<String>("btn btn-default"),
			cssClassNamesModel,
			textNoEscape
		);
	}
	
	protected AjaxConfirmLink(String id, IModel<O> model, Form<?> form, IModel<String> titleModel,
			IModel<String> textModel, IModel<String> yesLabelModel, IModel<String> noLabelModel,
			IModel<String> yesIconModel, IModel<String> noIconModel, IModel<String> yesButtonModel,
			IModel<String> noButtonModel, IModel<String> cssClassNamesModel, boolean textNoEscape) {
		super(id, model);
		setOutputMarkupId(true);
		this.form = form;
		add(new ConfirmContentBehavior(titleModel, textModel, yesLabelModel, noLabelModel, yesIconModel, noIconModel,
				yesButtonModel, noButtonModel, cssClassNamesModel, textNoEscape));
		
		// Lors du clic, on ouvre la popup de confirmation. Si l'action est confirmée, 
		// on délenche un évènement 'confirm'.
		// l'événement click habituel est supprimé par surcharge de newAjaxEventBehavior ci-dessous
		Event clickEvent = new Event(MouseEvent.CLICK) {
			private static final long serialVersionUID = 1L;
			@Override
			public JsScope callback() {
				return JsScopeEvent.quickScope(BootstrapConfirmStatement.confirm(AjaxConfirmLink.this).append("event.preventDefault();"));
			}
		};
		add(new WiQueryEventBehavior(clickEvent) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isEnabled(Component component) {
				return AjaxConfirmLink.this.isEnabledInHierarchy();
			}
		});
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		modules.forEach(module -> module.renderHead(this, response));
	}

	/**
	 * Cette méthode fournit normalement le handler pour l'événement onclick. On le remplace par l'événement de
	 * confirmation (le onclick est géré sans ajax au-dessus).
	 */
	@Override
	protected AjaxEventBehavior newAjaxEventBehavior(String event) {
		if (form != null) {
			return new AjaxFormSubmitBehavior(form, "confirm") {
				private static final long serialVersionUID = 4405251450215656630L;
				
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					onClick(target);
				}
				
				@Override
				protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
					super.updateAjaxAttributes(attributes);
					AjaxConfirmLink.this.updateAjaxAttributes(attributes);
				}
			};
		} else {
			// Lorsque l'évènement 'confirm' est détecté, on déclenche l'action à proprement parler.
			return new AjaxEventBehavior("confirm") {
				private static final long serialVersionUID = 1L;
				
				@Override
				public boolean isEnabled(Component component) {
					// On ajoute le handler seulement si le lien est activé
					return AjaxConfirmLink.this.isEnabledInHierarchy();
				}
				
				@Override
				protected void onEvent(AjaxRequestTarget target) {
					onClick(target);
				}
				
				@Override
				protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
					super.updateAjaxAttributes(attributes);
					AjaxConfirmLink.this.updateAjaxAttributes(attributes);
				}
			};
		}
	}
}
