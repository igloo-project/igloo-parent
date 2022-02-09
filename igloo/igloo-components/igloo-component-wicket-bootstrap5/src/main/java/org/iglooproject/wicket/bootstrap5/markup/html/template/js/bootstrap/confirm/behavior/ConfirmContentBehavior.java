package org.iglooproject.wicket.bootstrap5.markup.html.template.js.bootstrap.confirm.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.value.IValueMap;
import org.iglooproject.wicket.api.Models;
import org.iglooproject.wicket.bootstrap5.markup.html.template.js.bootstrap.confirm.BootstrapConfirmJavaScriptResourceReference;

public class ConfirmContentBehavior extends Behavior {

	private static final long serialVersionUID = -5357415707217228299L;

	private static final String ATTRIBUTE_TITLE = "data-modal-confirm-title";
	private static final String ATTRIBUTE_TEXT = "data-modal-confirm-text";
	private static final String ATTRIBUTE_YES_LABEL = "data-modal-confirm-yes-label";
	private static final String ATTRIBUTE_NO_LABEL = "data-modal-confirm-no-label";
	private static final String ATTRIBUTE_YES_ICON = "data-modal-confirm-yes-icon";
	private static final String ATTRIBUTE_NO_ICON = "data-modal-confirm-no-icon";
	private static final String ATTRIBUTE_YES_BUTTON = "data-modal-confirm-yes-button";
	private static final String ATTRIBUTE_NO_BUTTON = "data-modal-confirm-no-button";
	private static final String ATTRIBUTE_TEXT_NO_ESCAPE = "data-modal-confirm-text-noescape";
	private static final String ATTRIBUTE_CSS_CLASS_NAMES = "data-modal-confirm-css-class-names";

	private final IModel<String> titleModel;

	private final IModel<String> textModel;

	private final IModel<String> yesLabelModel;

	private final IModel<String> noLabelModel;
	
	private final IModel<String> yesIconModel;

	private final IModel<String> noIconModel;
	
	private final IModel<String> yesButtonModel;

	private final IModel<String> noButtonModel;

	private final IModel<String> cssClassNamesModel;

	private boolean textNoEscape;

	/**
	 * @param titleModel
	 * @param textModel
	 * @param yesLabelModel
	 * @param noLabelModel
	 * @param cssClassNamesModel - ignoré si null
	 * @param textNoEscape - utiliser true si votre texte est du HTML
	 */
	public ConfirmContentBehavior(IModel<String> titleModel, IModel<String> textModel,
			IModel<String> yesLabelModel, IModel<String> noLabelModel, IModel<String> yesIconModel, IModel<String> noIconModel,
			IModel<String> yesButtonModel, IModel<String> noButtonModel,IModel<String> cssClassNamesModel, boolean textNoEscape) {
		super();
		this.titleModel = titleModel;
		this.textModel = textModel;
		this.yesLabelModel = yesLabelModel;
		this.noLabelModel = noLabelModel;
		this.yesIconModel = yesIconModel;
		this.noIconModel = noIconModel;
		this.yesButtonModel = yesButtonModel;
		this.noButtonModel = noButtonModel;
		this.textNoEscape = textNoEscape;
		this.cssClassNamesModel = cssClassNamesModel;
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
		response.render(JavaScriptHeaderItem.forReference(BootstrapConfirmJavaScriptResourceReference.get()));
	}

	@Override
	public void onComponentTag(Component component, ComponentTag tag) {
		super.onComponentTag(component, tag);
		IValueMap attributes = tag.getAttributes();
		addAttribute(component, attributes, ATTRIBUTE_TITLE, titleModel);
		addAttribute(component, attributes, ATTRIBUTE_TEXT, textModel);
		addAttribute(component, attributes, ATTRIBUTE_YES_LABEL, yesLabelModel);
		addAttribute(component, attributes, ATTRIBUTE_NO_LABEL, noLabelModel);
		addAttribute(component, attributes, ATTRIBUTE_YES_ICON, yesIconModel);
		addAttribute(component, attributes, ATTRIBUTE_NO_ICON, noIconModel);
		addAttribute(component, attributes, ATTRIBUTE_YES_BUTTON, yesButtonModel);
		addAttribute(component, attributes, ATTRIBUTE_NO_BUTTON, noButtonModel);
		addAttribute(component, attributes, ATTRIBUTE_CSS_CLASS_NAMES, cssClassNamesModel);
		
		if (textNoEscape) {
			attributes.put(ATTRIBUTE_TEXT_NO_ESCAPE, textNoEscape);
		}
	}

	@Override
	public void detach(Component component) {
		super.detach(component);
		
		if (titleModel != null) {
			titleModel.detach();
		}
		if (textModel != null) {
			textModel.detach();
		}
		if (yesLabelModel != null) {
			yesLabelModel.detach();
		}
		if (noLabelModel != null) {
			noLabelModel.detach();
		}
		if (yesIconModel != null) {
			yesIconModel.detach();
		}
		if (noIconModel != null) {
			noIconModel.detach();
		}
		if (yesButtonModel != null) {
			yesButtonModel.detach();
		}
		if (noButtonModel != null) {
			noButtonModel.detach();
		}
		if (cssClassNamesModel != null) {
			cssClassNamesModel.detach();
		}
	}

	private void addAttribute(Component component, IValueMap attributes, String attributeName, IModel<String> model) {
		String label = getLabel(component, model);
		if (label != null) {
			attributes.put(attributeName, label);
		}
	}

	private String getLabel(Component component, IModel<String> labelModel) {
		if (labelModel != null) {
			IModel<String> model = Models.wrap(labelModel, component);
			return model.getObject();
		} else {
			return null;
		}
	}

}
