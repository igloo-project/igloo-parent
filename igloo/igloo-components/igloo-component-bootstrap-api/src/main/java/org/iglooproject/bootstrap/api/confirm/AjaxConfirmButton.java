package org.iglooproject.bootstrap.api.confirm;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.iglooproject.bootstrap.api.BootstrapRequestCycle;
import org.wicketstuff.wiquery.core.events.Event;
import org.wicketstuff.wiquery.core.events.MouseEvent;
import org.wicketstuff.wiquery.core.events.WiQueryEventBehavior;
import org.wicketstuff.wiquery.core.javascript.JsScope;
import org.wicketstuff.wiquery.core.javascript.JsScopeEvent;

public abstract class AjaxConfirmButton extends AjaxButton {

	private static final long serialVersionUID = -132330109149500197L;

	public AjaxConfirmButton(String id, IModel<String> titleModel, IModel<String> textModel,
			IModel<String> yesLabelModel, IModel<String> noLabelModel,
			IModel<String> cssClassNamesModel, boolean textNoEscape) {
		this(
			id,
			titleModel,
			textModel,
			yesLabelModel,
			noLabelModel,
			new Model<String>("fa fa-fw fa-check"),
			new Model<String>("fa fa-fw fa-ban"),
			new Model<String>("btn btn-success"),
			new Model<String>("btn btn-default"),
			cssClassNamesModel,
			textNoEscape,
			null
		);
	}
	
	public AjaxConfirmButton(String id, IModel<String> titleModel, IModel<String> textModel,
			IModel<String> yesLabelModel, IModel<String> noLabelModel, IModel<String> yesIconModel,
			IModel<String> noIconModel, IModel<String> yesButtonModel, IModel<String> noButtonModel,
			IModel<String> cssClassNamesModel, boolean textNoEscape) {
		this(id, titleModel, textModel, yesLabelModel, noLabelModel, yesIconModel, noIconModel, yesButtonModel,
				noButtonModel, cssClassNamesModel, textNoEscape, null);
	}

	public AjaxConfirmButton(String id, IModel<String> titleModel, IModel<String> textModel,
			IModel<String> yesLabelModel, IModel<String> noLabelModel, IModel<String> cssClassNamesModel,
			boolean textNoEscape, Form<?> form) {
		this(
			id,
			titleModel,
			textModel,
			yesLabelModel,
			noLabelModel,
			new Model<String>("fa fa-fw fa-check"),
			new Model<String>("fa fa-fw fa-ban"),
			new Model<String>("btn btn-success"),
			new Model<String>("btn btn-default"),
			cssClassNamesModel,
			textNoEscape,
			form
		);
	}

	public AjaxConfirmButton(String id, IModel<String> titleModel, IModel<String> textModel,
			IModel<String> yesLabelModel, IModel<String> noLabelModel, IModel<String> yesIconModel,
			IModel<String> noIconModel, IModel<String> yesButtonModel, IModel<String> noButtonModel,
			IModel<String> cssClassNamesModel, boolean textNoEscape, Form<?> form) {
		super(id, null, form);
		add(new ConfirmContentBehavior(titleModel, textModel, yesLabelModel, noLabelModel, yesIconModel, noIconModel,
				yesButtonModel, noButtonModel, cssClassNamesModel, textNoEscape));

		// Lors du clic, on ouvre la popup de confirmation. Si l'action est confirmée, 
		// on délenche un évènement 'confirm'.
		// l'événement click habituel est supprimé par surcharge de newAjaxEventBehavior ci-dessous
		Event clickEvent = new Event(MouseEvent.CLICK) {
			private static final long serialVersionUID = 1L;
			@Override
			public JsScope callback() {
				return JsScopeEvent.quickScope(BootstrapRequestCycle.getSettings().confirmStatement(AjaxConfirmButton.this).append("event.preventDefault();"));
			}
		};
		add(new WiQueryEventBehavior(clickEvent) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isEnabled(Component component) {
				return AjaxConfirmButton.this.isEnabledInHierarchy();
			}
		});
	}

	/**
	 * Cette méthode fournit normalement le handler pour l'événement onclick. On le remplace par l'événement de
	 * confirmation (le onclick est géré sans ajax au-dessus).
	 */
	@Override
	protected AjaxFormSubmitBehavior newAjaxFormSubmitBehavior(String event) {
		return new AjaxFormSubmitBehavior(getForm(), "confirm") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isEnabled(Component component) {
				// On ajoute le handler seulement si le composant est activé
				return AjaxConfirmButton.this.isEnabledInHierarchy();
			}
			
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				AjaxConfirmButton.this.onSubmit(target);
			}
			
			@Override
			protected void onAfterSubmit(AjaxRequestTarget target) {
				AjaxConfirmButton.this.onAfterSubmit(target);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target) {
				AjaxConfirmButton.this.onError(target);
			}
			
			// TODO 0.10 : checker avec LAL ou YRO
//			@SuppressWarnings("deprecation")
//			@Override
//			protected AjaxChannel getChannel() {
//				return AjaxConfirmButton.this.getChannel();
//			}
			
			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				AjaxConfirmButton.this.updateAjaxAttributes(attributes);
			}
			
			@Override
			public boolean getDefaultProcessing() {
				return AjaxConfirmButton.this.getDefaultFormProcessing();
			}
		};
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		BootstrapRequestCycle.getSettings().confirmRenderHead(this, response);
	}

}
