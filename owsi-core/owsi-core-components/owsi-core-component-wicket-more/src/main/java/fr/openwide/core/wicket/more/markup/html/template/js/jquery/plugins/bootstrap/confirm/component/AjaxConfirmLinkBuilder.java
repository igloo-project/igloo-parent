package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.wicket.more.markup.html.action.AjaxActions;
import fr.openwide.core.wicket.more.markup.html.action.IAjaxAction;
import fr.openwide.core.wicket.more.markup.html.action.IOneParameterAjaxAction;
import fr.openwide.core.wicket.more.markup.html.basic.ComponentBooleanProperty;
import fr.openwide.core.wicket.more.markup.html.basic.EnclosureBehavior;
import fr.openwide.core.wicket.more.markup.html.factory.AbstractDetachableFactory;
import fr.openwide.core.wicket.more.markup.html.factory.IDetachableFactory;
import fr.openwide.core.wicket.more.markup.html.factory.ModelFactories;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.fluid.IAjaxConfirmLinkBuilderStepContent;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.fluid.IAjaxConfirmLinkBuilderStepEndContent;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.fluid.IAjaxConfirmLinkBuilderStepNo;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.fluid.IAjaxConfirmLinkBuilderStepOnclick;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.fluid.IAjaxConfirmLinkBuilderStepOneParameterTerminal;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.fluid.IAjaxConfirmLinkBuilderStepStart;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.fluid.IAjaxConfirmLinkBuilderStepTerminal;
import fr.openwide.core.wicket.more.util.model.Detachables;

public class AjaxConfirmLinkBuilder<O> implements IAjaxConfirmLinkBuilderStepStart<O>, IAjaxConfirmLinkBuilderStepContent<O>,
		IAjaxConfirmLinkBuilderStepEndContent<O>, IAjaxConfirmLinkBuilderStepNo<O>, IAjaxConfirmLinkBuilderStepOnclick<O>,
		IAjaxConfirmLinkBuilderStepOneParameterTerminal<O>, IAjaxConfirmLinkBuilderStepTerminal<O> {

	private static final long serialVersionUID = 365949870142796149L;

	private IDetachableFactory<? super IModel<O>, ? extends IModel<String>> titleModelFactory;
	
	private IDetachableFactory<? super IModel<O>, ? extends IModel<String>> contentModelFactory;
	
	private IModel<String> yesLabelModel;
	
	private IModel<String> noLabelModel;
	
	private IModel<String> cssClassNamesModel;
	
	private boolean keepMarkup = false;
	
	private IOneParameterAjaxAction<? super IModel<O>> onClick;
	
	protected AjaxConfirmLinkBuilder() {
	}
	
	@Override
	public IAjaxConfirmLinkBuilderStepContent<O> title(IModel<String> titleModel) {
		return title(ModelFactories.constant(titleModel));
	}
	
	@Override
	public IAjaxConfirmLinkBuilderStepContent<O> title(IDetachableFactory<? super IModel<O>, ? extends IModel<String>> titleModelFactory) {
		this.titleModelFactory = titleModelFactory;
		return this;
	}
	
	@Override
	public IAjaxConfirmLinkBuilderStepEndContent<O> content(IModel<String> contentModel) {
		return content(ModelFactories.constant(contentModel));
	}
	
	@Override
	public IAjaxConfirmLinkBuilderStepEndContent<O> content(IDetachableFactory<? super IModel<O>, ? extends IModel<String>> contentModelFactory) {
		this.contentModelFactory = contentModelFactory;
		return this;
	}
	
	@Override
	public IAjaxConfirmLinkBuilderStepEndContent<O> cssClassNamesModel(IModel<String> cssClassNamesModel) {
		this.cssClassNamesModel = cssClassNamesModel;
		return this;
	}
	
	@Override
	public IAjaxConfirmLinkBuilderStepOnclick<O> deleteConfirmation() {
		confirm();
		title(new ResourceModel("common.confirmTitle"));
		content(new AbstractDetachableFactory<IModel<O>, IModel<String>>() {
			private static final long serialVersionUID = 1L;
			@Override
			public IModel<String> create(IModel<O> parameter) {
				if (parameter != null && parameter.getObject() instanceof GenericEntity<?, ?>) {
					GenericEntity<?, ?> genericEntity = (GenericEntity<?, ?>) parameter.getObject();
					return new StringResourceModel("common.deleteConfirmation.object").setParameters(genericEntity.getDisplayName());
				} else {
					return new ResourceModel("common.deleteConfirmation");
				}
			}
		});
		return this;
	}

	@Override
	public IAjaxConfirmLinkBuilderStepEndContent<O> keepMarkup() {
		this.keepMarkup = true;
		return this;
	}

	@Override
	public IAjaxConfirmLinkBuilderStepNo<O> yes(IModel<String> yesLabelModel) {
		this.yesLabelModel = yesLabelModel;
		return this;
	}
	
	@Override
	public IAjaxConfirmLinkBuilderStepOnclick<O> no(IModel<String> noLabelModel) {
		this.noLabelModel = noLabelModel;
		return this;
	}

	@Override
	public IAjaxConfirmLinkBuilderStepOnclick<O> yesNo() {
		this.yesLabelModel = new ResourceModel("common.yes");
		this.noLabelModel = new ResourceModel("common.no");
		return this;
	}

	@Override
	public IAjaxConfirmLinkBuilderStepOnclick<O> confirm() {
		this.yesLabelModel = new ResourceModel("common.confirm");
		this.noLabelModel = new ResourceModel("common.cancel");
		return this;
	}

	@Override
	public IAjaxConfirmLinkBuilderStepOnclick<O> validate() {
		this.yesLabelModel = new ResourceModel("common.validate");
		this.noLabelModel = new ResourceModel("common.cancel");
		return this;
	}

	@Override
	public IAjaxConfirmLinkBuilderStepOnclick<O> save() {
		this.yesLabelModel = new ResourceModel("common.save");
		this.noLabelModel = new ResourceModel("common.cancel");
		return this;
	}

	@Override
	public IAjaxConfirmLinkBuilderStepOneParameterTerminal<O> onClick(IOneParameterAjaxAction<? super IModel<O>> onClick) {
		this.onClick = onClick;
		return this;
	}

	@Override
	public IAjaxConfirmLinkBuilderStepTerminal<O> onClick(IAjaxAction onClick) {
		this.onClick = AjaxActions.ignoreParameter(onClick);
		return this;
	}

	@Override
	public AjaxConfirmLink<O> create(String wicketId) {
		return create(wicketId, null);
	}

	@Override
	public AjaxConfirmLink<O> create(String wicketId, IModel<O> model) {
		AjaxConfirmLink<O> confirmLink = new FunctionalAjaxConfirmLink<O>(
				wicketId, model, titleModelFactory, contentModelFactory,
				yesLabelModel, noLabelModel, cssClassNamesModel, keepMarkup, onClick
		);
		confirmLink.add(
				new EnclosureBehavior(ComponentBooleanProperty.VISIBLE)
				.condition(onClick.getActionAvailableCondition(model))
		);
		return confirmLink;
	}

	private static class FunctionalAjaxConfirmLink<O> extends AjaxConfirmLink<O> {
		private static final long serialVersionUID = -2098954474307467112L;
		
		private final IOneParameterAjaxAction<? super IModel<O>> onClick;
		
		public FunctionalAjaxConfirmLink(String id, IModel<O> model,
				IDetachableFactory<? super IModel<O>, ? extends IModel<String>> titleModelFactory,
				IDetachableFactory<? super IModel<O>, ? extends IModel<String>> textModelFactory, IModel<String> yesLabelModel,
				IModel<String> noLabelModel, IModel<String> cssClassNamesModel, boolean textNoEscape,
				IOneParameterAjaxAction<? super IModel<O>> onClick) {
			super(id, model, titleModelFactory.create(model), textModelFactory.create(model), yesLabelModel,
					noLabelModel, cssClassNamesModel, textNoEscape);
			this.onClick = onClick;
		}
		
		@Override
		public void onClick(AjaxRequestTarget target) {
			this.onClick.execute(target, getModel());
		}
		
		@Override
		protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
			super.updateAjaxAttributes(attributes);
			this.onClick.updateAjaxAttributes(attributes, getModel());
		}
		
		@Override
		protected void onDetach() {
			super.onDetach();
			Detachables.detach(onClick);
		}
	
	}

	@Override
	public void detach() {
		Detachables.detach(titleModelFactory, contentModelFactory, yesLabelModel, noLabelModel, cssClassNamesModel, onClick);
	}

}